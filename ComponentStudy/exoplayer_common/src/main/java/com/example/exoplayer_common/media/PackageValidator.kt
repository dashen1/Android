package com.example.exoplayer_common.media

import android.Manifest.permission.MEDIA_CONTENT_CONTROL
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED
import android.content.pm.PackageManager
import android.content.res.XmlResourceParser
import android.os.Process
import android.util.Base64
import android.util.Log
import androidx.annotation.XmlRes
import androidx.core.app.NotificationManagerCompat
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


internal class PackageValidator(context: Context, @XmlRes xmlResId: Int) {

    private val context: Context
    private val packageManager: PackageManager

    private val certificateAllowList: Map<String, KnownCallerInfo>
    private val platformSignature: String

    private val callerChecked = mutableMapOf<String, Pair<Int, Boolean>>()

    init {
        val parser = context.resources.getXml(xmlResId)
        this.context = context.applicationContext
        this.packageManager = this.context.packageManager

        certificateAllowList = buildCertificateAllowList(parser)
        platformSignature = getSystemSignature()
    }

    fun isKnownCaller(callingPackage: String, callingUid: Int): Boolean {
        //  If the caller has already been checked, return the previous result here.
        val (checkedUid, checkResult) = callerChecked[callingPackage] ?: Pair(0, false)
        if (checkedUid == callingUid) {
            return checkResult
        }
        /**
         * Because some of these checks can be slow, we save the results in [callerChecked] after
         * this code is run.
         *
         * In particular, there's little reason to recompute the calling package's certificate
         * signature (SHA-256) each call.
         *
         * This is safe to do as we know the UID matches the package's UID (from the check above),
         * and app UIDs are set at install time. Additionally, a package name + UID is guaranteed to
         * be constant until a reboot. (After a reboot then a previously assigned UID could be
         * reassigned.)
         */
        // Build the caller info for the rest of the checks here.
        val callerPackageInfo = buildCallerInfo(callingPackage)
            ?: throw IllegalStateException("Caller's wasn't found in the system?")

        // Verify that things aren't ... broken.(This test should always pass.)
        if (callerPackageInfo.uid != callingUid) {
            throw IllegalStateException("Caller's package UID doesn't match caller's actual UID?")
        }
        val callerSignature = callerPackageInfo.signature
        val isPackageInAllowList = certificateAllowList[callingPackage]?.signature?.first {
            it.signature == callerSignature
        } != null

        val isCallerKnown = when {
            // If it's our own app making the call, allow it.
            callingUid == Process.myUid() -> true
            // If it's one of the apps on the allow list, allow it.
            isPackageInAllowList -> true
            // If the system is making the call, allow it.
            callingUid == Process.SYSTEM_UID -> true
            // If the app was signed by the same certificate as the platform itself,allow it.
            callerSignature == platformSignature -> true

            callerPackageInfo.permissions.contains(MEDIA_CONTENT_CONTROL) -> true

            NotificationManagerCompat.getEnabledListenerPackages(this.context)
                .contains(callerPackageInfo.packageName) -> true

            else -> false
        }

        if (!isCallerKnown) {

        }

        // Save our work for next time.
        callerChecked[callingPackage] = Pair(callingUid, isCallerKnown)
        return isCallerKnown
    }

    private fun logUnknownCaller(callerPackageInfor: CallerPackageInfo) {

    }

    /**
     * Builds a [CallerPackageInfo] for a given package that can be used for all the
     * various checks that are performed before allowing an app to connect to
     * [MediaBrowserServiceCompat]
     */
    private fun buildCallerInfo(callingPackage: String): CallerPackageInfo? {
        val packageInfo = getPackageInfo(callingPackage) ?: return null

        val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
        val uid = packageInfo.applicationInfo.uid
        val signature = getSignature(packageInfo)

        val requestPermissions = packageInfo.requestedPermissions
        val permissionFlag = packageInfo.requestedPermissionsFlags
        val actionPermissions = mutableSetOf<String>()
        requestPermissions?.forEachIndexed{index, permission->
            if (permissionFlag[index] and REQUESTED_PERMISSION_GRANTED!=0){
                actionPermissions += permission
            }
        }

        return CallerPackageInfo(appName,callingPackage,uid,signature,actionPermissions.toSet())
    }

    private fun getPackageInfo(callingPackage: String): PackageInfo? =
        packageManager.getPackageInfo(
            callingPackage,
            PackageManager.GET_SIGNATURES or PackageManager.GET_PERMISSIONS
        )

    private fun getSignature(packageInfo: PackageInfo): String? =
        if (packageInfo.signatures == null || packageInfo.signatures.size != 1) {
            null
        } else {
            val certificate = packageInfo.signatures[0].toByteArray()
            getSignatureSha256(certificate)
        }

    private fun buildCertificateAllowList(parser: XmlResourceParser): Map<String, KnownCallerInfo> {

        val certificateAllowList = LinkedHashMap<String, KnownCallerInfo>()
        try {
            var eventType = parser.next()
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    val callerInfo = when (parser.name) {
                        "signing_certificate" -> parserV1Tag(parser)
                        "signature" -> parseV2Tag(parser)
                        else -> null
                    }

                    callerInfo?.let { info ->
                        val packageName = info.packageName
                        val existingCallerInfo = certificateAllowList[packageName]
                        if (existingCallerInfo != null) {
                            existingCallerInfo.signature += callerInfo.signature
                        } else {
                            certificateAllowList[packageName] = callerInfo
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (xmlException: XmlPullParserException) {
            Log.e(TAG, "Could not read allowed callers from XML.", xmlException)
        } catch (ioException: IOException) {
            Log.e(TAG, "Could not read allowed callers from XML.", ioException)
        }

        return certificateAllowList
    }

    /**
     * Parses a V1 format tag. See allowed_browser_callers.xml for more details.
     */
    private fun parserV1Tag(parser: XmlResourceParser): KnownCallerInfo {
        val name = parser.getAttributeValue(null, "name")
        val packageName = parser.getAttributeValue(null, "package")
        val isRelease = parser.getAttributeBooleanValue(null, "release", false)
        val certificate = parser.nextText().replace(WHITESPACE_REGEX, "")
        val signature = getSignatureSha256(certificate)

        val callerSignature = KnownSignature(signature, isRelease)
        return KnownCallerInfo(name, packageName, mutableSetOf(callerSignature))
    }

    private fun parseV2Tag(parser: XmlResourceParser): KnownCallerInfo {
        val name = parser.getAttributeValue(null, "name")
        val packageName = parser.getAttributeValue(null, "package")

        val callerSignatures = mutableSetOf<KnownSignature>()
        var eventType = parser.next()
        while (eventType != XmlResourceParser.END_TAG) {
            val isRelease = parser.getAttributeBooleanValue(null, "release", false)
            val signature = parser.nextText().replace(WHITESPACE_REGEX, "").toLowerCase()
            callerSignatures += KnownSignature(signature, isRelease)

            eventType = parser.next()
        }

        return KnownCallerInfo(name, packageName, callerSignatures)
    }

    private fun getSystemSignature(): String =
        getPackageInfo(ANDROID_PLATFORM)?.let { platformInfo ->
            getSignature(platformInfo)
        } ?: throw IllegalStateException("Platform signature not found.")


    private fun getSignatureSha256(certificate: String): String {
        return getSignatureSha256(Base64.decode(certificate, Base64.DEFAULT))
    }

    private fun getSignatureSha256(certificate: ByteArray): String {
        val md: MessageDigest
        try {
            md = MessageDigest.getInstance("SHA256")
        } catch (noSuchAlgorithmException: NoSuchAlgorithmException) {
            Log.e(TAG, "No such algorithm:$noSuchAlgorithmException")
            throw RuntimeException("Could not find SHA256 hash algorithm", noSuchAlgorithmException)
        }
        md.update(certificate)
        return md.digest().joinToString(":") { String.format("%02x", it) }
    }

    private data class KnownCallerInfo(
        internal val name: String,
        internal val packageName: String,
        internal val signature: MutableSet<KnownSignature>
    )

    private data class KnownSignature(
        internal val signature: String,
        internal val release: Boolean
    )

    /**
     * Convenience class to hold all of the information about an app that's being  checked
     * to see if it's a known caller
     */
    private data class CallerPackageInfo(
        internal val name: String,
        internal val packageName: String,
        internal val uid: Int,
        internal val signature: String?,
        internal val permissions: Set<String>
    )
}

private const val TAG = "PackageValidator"
private const val ANDROID_PLATFORM = "android"
private val WHITESPACE_REGEX = "\\s|\\n".toRegex()