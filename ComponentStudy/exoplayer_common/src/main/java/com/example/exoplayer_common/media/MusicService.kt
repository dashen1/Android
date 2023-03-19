package com.example.exoplayer_common.media

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.MediaBrowserServiceCompat.BrowserRoot.EXTRA_RECENT
import com.example.exoplayer_common.R
import com.example.exoplayer_common.media.extensions.*
import com.example.exoplayer_common.media.library.*
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.Util.constrainValue
import com.google.android.gms.cast.framework.CastContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

open class MusicService : MediaBrowserServiceCompat() {


    private lateinit var notificationManager: UampNotificationManager
    private lateinit var mediaSource: MusicSource
    private lateinit var packageValidator: PackageValidator

    // The current player will either be an Exoplayer (for local playback) or a CastPlayer(for
    // remote playback through a Cast device.)
    private lateinit var currentPlayer: Player

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    protected lateinit var mediaSession: MediaSessionCompat
    protected lateinit var mediaSessionConnector: MediaSessionConnector
    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()
    private var currentMediaItemIndex: Int = 0

    private lateinit var storage: PersistentStorage

    private val browseTree: BrowseTree by lazy {
        BrowseTree(applicationContext, mediaSource)
    }

    private var isForegroundService = false

    private val remoteJsonSource: Uri =
        Uri.parse("https://storage.googleapis.com/uamp/catalog.json")

    private val uAmpAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    private val playerListener = PlayerEventListener()

    private val exoPlayer:ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(uAmpAudioAttributes,true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    /**
     * If Cast is available, create a CastPlayer to handler communication with a Cast session.
     */

    private val castPlayer:CastPlayer?by lazy {
        try {
            val castContext = CastContext.getSharedInstance(this)
            CastPlayer(castContext,CastMediaItemConverter()).apply {
                setSessionAvailabilityListener(UampCastSessionAvailabilityListener())
                addListener(playerListener)
            }
        }catch (e:Exception){
            Log.i(TAG, "Cast is not available on this device. " +
                    "Exception thrown when attempting to obtain CastContext. " + e.message)
            null
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent->
                PendingIntent.getActivity(this,0,sessionIntent,0)
            }

        // Create a new MediaSession.
        mediaSession = MediaSessionCompat(this,"MusicService")
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }
        sessionToken = mediaSession.sessionToken

        notificationManager = UampNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        mediaSource = JsonSource(source = remoteJsonSource)
        serviceScope.launch {
            mediaSource.load()
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(UampPlaybackPreparer())
        mediaSessionConnector.setQueueNavigator(UampQueueNavigator(mediaSession))

        switchToPlayer(
            previousPlayer = null,
            newPlayer = if (castPlayer?.isCastSessionAvailable==true) castPlayer!! else exoPlayer
        )
        notificationManager.showNotificationForPlayer(currentPlayer)

        packageValidator = PackageValidator(this,R.xml.allowed_media_browser_callers)

        storage = PersistentStorage.getInstance(applicationContext)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        saveRecentSongToStorage()
        super.onTaskRemoved(rootIntent)
        currentPlayer.stop(true)
    }

    override fun onDestroy() {

        mediaSession.run {
            isActive = false
            release()
        }

        // Cancel coroutines when the service is going away.
        serviceJob.cancel()

        // Free ExoPlayer resources.
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    private fun saveRecentSongToStorage(){
        if (currentPlaylistItems.isEmpty()){
            return
        }
        val description = currentPlaylistItems[currentMediaItemIndex].description
        val position = currentPlayer.currentPosition

        serviceScope.launch {
            storage.saveRecentSong(
                description,
                position
            )
        }
    }



    private fun switchToPlayer(previousPlayer:Player?,newPlayer:Player){
        if (previousPlayer==newPlayer){
            return
        }
        currentPlayer = newPlayer
        if (previousPlayer!=null){
            val playbackState = previousPlayer.playbackState
            if (currentPlaylistItems.isEmpty()){
                currentPlayer.clearMediaItems()
                currentPlayer.stop()
            }else if (playbackState!=Player.STATE_IDLE&&playbackState!=Player.STATE_ENDED){
                preparePlaylist(
                    metadataList = currentPlaylistItems,
                    itemToPlay = currentPlaylistItems[currentMediaItemIndex],
                    playWhenReady = previousPlayer.playWhenReady,
                    playbackStartPositionMs = previousPlayer.currentPosition
                )
            }
        }
        mediaSessionConnector.setPlayer(newPlayer)
        previousPlayer?.stop(true)
    }

    private inner class UampQueueNavigator(
        mediaSession: MediaSessionCompat
    ):TimelineQueueNavigator(mediaSession){
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            if (windowIndex<currentPlaylistItems.size){
                return currentPlaylistItems[windowIndex].description
            }
            return MediaDescriptionCompat.Builder().build()
        }

    }

    private inner class UampPlaybackPreparer:MediaSessionConnector.PlaybackPreparer{

        override fun onCommand(
            player: Player,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?
        ): Boolean = false

        override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

        override fun onPrepare(playWhenReady: Boolean) {
            val recentSong = storage.loadRecentSong()?:return
            onPrepareFromMediaId(
                recentSong.mediaId!!,
                playWhenReady,
                recentSong.description.extras
            )
        }

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
            mediaSource.whenReady {
                val itemToPlay: MediaMetadataCompat? =mediaSource.find { item->
                    item.id ==mediaId
                }
                if (itemToPlay==null){
                    Log.w(TAG, "Content not found: MediaID=$mediaId")
                    // TODO: Notify caller of the error.
                }else{

                    val playbackStartPositionMs =
                        extras?.getLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS,C.TIME_UNSET)
                            ?:C.TIME_UNSET
                    preparePlaylist(
                        buildPlaylist(itemToPlay),
                        itemToPlay,
                        playWhenReady,
                        playbackStartPositionMs
                    )
                }
            }
        }


        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
            mediaSource.whenReady {
                val metadataList = mediaSource.search(query,extras?:Bundle.EMPTY)
                if (metadataList.isNotEmpty()){
                    preparePlaylist(
                        metadataList,
                        metadataList[0],
                        playWhenReady,
                        playbackStartPositionMs = C.TIME_UNSET
                    )
                }
            }
        }

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

    }

    private fun buildPlaylist(item:MediaMetadataCompat):List<MediaMetadataCompat> =
        mediaSource.filter { it.album==item.album }.sortedBy { it.trackNumber }

    private fun preparePlaylist(
        metadataList:List<MediaMetadataCompat>,
        itemToPlay:MediaMetadataCompat?,
        playWhenReady: Boolean,
        playbackStartPositionMs:Long
    ){
        val initialWindowIndex = if (itemToPlay==null) 0 else metadataList.indexOf(itemToPlay)
        currentPlaylistItems = metadataList

        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.stop()
        currentPlayer.setMediaItems(
            metadataList.map { it.toMediaItem() },
            initialWindowIndex,playbackStartPositionMs
        )
        currentPlayer.prepare()
    }

    private inner class PlayerNotificationListener:
    PlayerNotificationManager.NotificationListener{
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing&&!isForegroundService){
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext,this@MusicService.javaClass)
                )

                startForeground(notificationId,notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    private inner class UampCastSessionAvailabilityListener:SessionAvailabilityListener{
        override fun onCastSessionAvailable() {
            switchToPlayer(currentPlayer,castPlayer!!)
        }

        override fun onCastSessionUnavailable() {
            switchToPlayer(currentPlayer,exoPlayer)
        }

    }

    /**
     * Listen for events from ExoPlayer.
     */
    private inner class PlayerEventListener : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
                    notificationManager.showNotificationForPlayer(currentPlayer)
                    if (playbackState==Player.STATE_READY){
                        saveRecentSongToStorage()

                        if (!playWhenReady){
                            stopForeground(false)
                            isForegroundService = false
                        }
                    }
                }
                else ->{
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)
                || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
                || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)){
                currentMediaItemIndex = if (currentPlaylistItems.isNotEmpty()){
                    constrainValue(
                        player.currentMediaItemIndex,
                        0,
                        currentPlaylistItems.size -1
                    )
                }else 0
            }
        }

        override fun onPlayerError(error: PlaybackException) {

            var message = R.string.generic_error;
            Log.e(TAG, "Player error: " + error.errorCodeName + " (" + error.errorCode + ")");
            if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND) {
                message = R.string.error_media_not_found;
            }
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        val isKnownCaller = packageValidator.isKnownCaller(clientPackageName,clientUid)
        val rootExtras = Bundle().apply {
            putBoolean(
                MEDIA_SEARCH_SUPPORTED,
                isKnownCaller || browseTree.searchableByUnknownCaller
            )
            putBoolean(CONTENT_STYLE_SUPPORTED,true)
            putInt(CONTENT_STYLE_BROWSABLE_HINT, CONTENT_STYLE_GRID)
            putInt(CONTENT_STYLE_PLAYABLE_HINT, CONTENT_STYLE_LIST)
        }

        return if(isKnownCaller){
            val isRecentRequest = rootHints?.getBoolean(EXTRA_RECENT)?:false
            val browserRootPath = if (isRecentRequest) UAMP_RECENT_ROOT else UAMP_BROWSABLE_ROOT
            BrowserRoot(browserRootPath,rootExtras)
        }else{
            BrowserRoot(UAMP_EMPTY_ROOT,rootExtras)
        }
    }

    override fun onLoadChildren(
        parentMediaId: String,
        result: Result<List<MediaItem>>
    ) {
        if (parentMediaId== UAMP_RECENT_ROOT){
            result.sendResult(storage.loadRecentSong()?.let { song-> listOf(song) })
        }else{
            val resultSent = mediaSource.whenReady { successfullyInitialized ->
                if(successfullyInitialized){
                    val children = browseTree[parentMediaId]?.map { item->
                        MediaItem(item.description,item.flag)
                    }
                    result.sendResult(children)
                }else{
                    mediaSession.sendSessionEvent(NETWORK_FAILURE,null)
                    result.sendResult(null)
                }
            }
            if (!resultSent){
                result.detach()
            }
        }
    }

    override fun onSearch(query: String, extras: Bundle?, result: Result<List<MediaItem>>) {

        val resultsSent = mediaSource.whenReady { successfullyInitialized->
            if (successfullyInitialized){
                val resultsList = mediaSource.search(query,extras?:Bundle.EMPTY)
                    .map { mediaMetadata->
                        MediaItem(mediaMetadata.description,mediaMetadata.flag)
                    }
                result.sendResult(resultsList)
            }
        }

        if (!resultsSent){
            result.detach()
        }
    }

}

/*
 * (Media) Session events
 */
const val NETWORK_FAILURE = "com.example.android.uamp.media.session.NETWORK_FAILURE"

/** Content styling constants */
private const val CONTENT_STYLE_BROWSABLE_HINT = "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT"
private const val CONTENT_STYLE_PLAYABLE_HINT = "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT"
private const val CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED"
private const val CONTENT_STYLE_LIST = 1
private const val CONTENT_STYLE_GRID = 2

private const val UAMP_USER_AGENT = "uamp.next"

val MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS = "playback_start_position_ms"

private const val TAG = "MusicService"