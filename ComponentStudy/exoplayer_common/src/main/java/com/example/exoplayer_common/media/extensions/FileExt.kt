package com.example.exoplayer_common.media.extensions

import android.content.ContentResolver
import android.net.Uri
import java.io.File

/**
 * Returns a Content Uri for the AlbumArtContentProvider
 */

fun File.asAlbumArtContentUri(): Uri {
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT)
        .authority(AUTHORITY)
        .appendPath(this.path)
        .build()
}

private const val AUTHORITY = "com.example.android.uamp"