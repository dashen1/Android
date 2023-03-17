package com.example.exoplayer_common.common

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.MutableLiveData

/**
 * Class that manages a connection to a [MediaBrowserServiceCompat] instance, typically a
 * [MusicService] or ome of its subclasses.
 */

class MusicServiceConnection(context: Context,serviceComponent:ComponentName){

    val isConnected = MutableLiveData<Boolean>()
        .apply { postValue(false) }
    val networkFailure = MutableLiveData<Boolean>()
        .apply { postValue(false) }
//
//    val rootMediaId:String get() = mediaBrowser.root
//
//    val playbackState = MutableLiveData<PlaybackStat>
}