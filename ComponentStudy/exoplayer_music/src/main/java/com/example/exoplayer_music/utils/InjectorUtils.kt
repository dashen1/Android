package com.example.exoplayer_music.utils

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.example.exoplayer_common.common.MusicServiceConnection
import com.example.exoplayer_common.media.MusicService
import com.example.exoplayer_music.viewmodels.MainActivityViewModel
import com.example.exoplayer_music.viewmodels.MediaItemFragmentViewModel
import com.example.exoplayer_music.viewmodels.NowPlayingFragmentViewModel

object InjectorUtils {
    private fun provideMusicServiceConnection(context: Context): MusicServiceConnection {
        Log.d(TAG,"context package :${context.packageName}")
        return MusicServiceConnection.getInstance(
            context,
            ComponentName(context, MusicService::class.java)
        )
    }

    fun provideMainActivityViewModel(context: Context): MainActivityViewModel.Factory {
        val applicationContext = context.applicationContext
        val musicServiceConnection = provideMusicServiceConnection(applicationContext)
        return MainActivityViewModel.Factory(musicServiceConnection)
    }

    fun provideMediaItemFragmentViewModel(context: Context, mediaId: String)
            : MediaItemFragmentViewModel.Factory {
        val applicationContext = context.applicationContext
        val musicServiceConnection = provideMusicServiceConnection(applicationContext)
        return MediaItemFragmentViewModel.Factory(mediaId, musicServiceConnection)
    }

    fun provideNowPlayingFragmentViewModel(context: Context)
    : NowPlayingFragmentViewModel.Factory{
        val applicationContext = context.applicationContext
        val musicServiceConnection = provideMusicServiceConnection(applicationContext)
        return NowPlayingFragmentViewModel.Factory(
            applicationContext as Application,musicServiceConnection
        )
    }
}

private const val TAG = "InjectorUtils"