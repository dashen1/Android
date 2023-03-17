package com.example.exoplayer_common.common

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData

/**
 * Class that manages a connection to a [MediaBrowserServiceCompat] instance, typically a
 * [MusicService] or ome of its subclasses.
 */

class MusicServiceConnection(context: Context, serviceComponent: ComponentName) {

    val isConnected = MutableLiveData<Boolean>()
        .apply { postValue(false) }
    val networkFailure = MutableLiveData<Boolean>()
        .apply { postValue(false) }

    val rootMediaId: String get() = mediaBrowser.root

    val playbackState = MutableLiveData<PlaybackStateCompat>()
        .apply { postValue(EMPTY_PLAYBACK_STATE) }
    val nowPlaying = MutableLiveData<MediaMetadataCompat>()
        .apply { postValue(NOTHING_PLAYING) }

    val transportControls:MediaControllerCompat.TransportControls
    get() = mediaController.transportControls

    private lateinit var mediaController:MediaControllerCompat

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        serviceComponent,
        mediaBrowserConnectionCallback,null
    ).apply { connect() }

    fun subscribe(parentId:String,callback: MediaBrowserCompat.SubscriptionCallback){
        mediaBrowser.subscribe(parentId,callback)
    }

    fun unsubscribe(parentId: String,callback: MediaBrowserCompat.SubscriptionCallback){
        mediaBrowser.unsubscribe(parentId, callback)
    }

    fun sendCommand(command:String,parameters:Bundle?) =
        sendCommand(command,parameters){_,_->}

    fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback:((Int,Bundle?)->Unit)
    )= if (mediaBrowser.isConnected){

    }

    private inner class MediaBrowserConnectionCallback(private val context: Context):MediaBrowserCompat.ConnectionCallback(){
        /**
         * Invoked after [MediaBrowserCompat.connect] when the request has successfully
         * completed.
         */
        override fun onConnected() {
            // Get a MediaController for the MediaSession
            mediaController = MediaControllerCompat(context)
        }
    }
}

val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0)
    .build()

val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()