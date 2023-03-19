package com.example.exoplayer_music.viewmodels

import android.media.session.PlaybackState
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.*
import com.example.exoplayer_common.common.EMPTY_PLAYBACK_STATE
import com.example.exoplayer_common.common.MusicServiceConnection
import com.example.exoplayer_common.common.NOTHING_PLAYING
import com.example.exoplayer_common.media.extensions.id
import com.example.exoplayer_common.media.extensions.isPlaying
import com.example.exoplayer_music.MediaItemData
import com.example.exoplayer_music.R

/**
 * [ViewModel] for [MediaItemFragment].
 */
class MediaItemFragmentViewModel(
    private val mediaId:String,
    musicServiceConnection: MusicServiceConnection
):ViewModel() {
    /**
     * Use a backing property so consumers of mediaItems only get a [LiveData] instance so
     * they don't inadvertently modify it.
     */
    private val mediaItemsPrivate = MutableLiveData<List<MediaItemData>>()
    val mediaItems: LiveData<List<MediaItemData>> = mediaItemsPrivate

    /**
     * Pass the status of the [MusicServiceConnection.networkFailure] through.
     */
    val networkError = Transformations.map(musicServiceConnection.networkFailure) { it }

    private val subscriptionCallback = object : SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaItem>
        ) {
            val itemLists = children.map { child ->
                val subtitle = child.description.subtitle ?: ""
                MediaItemData(
                    child.mediaId!!,
                    child.description.title.toString(),
                    subtitle.toString(),
                    child.description.iconUri!!,
                    child.isBrowsable,
                    getResourceForMediaId(child.mediaId!!)
                )
            }
            mediaItemsPrivate.postValue(itemLists)
        }
    }

    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        val playbackState = it ?: EMPTY_PLAYBACK_STATE
        val metadata = musicServiceConnection.nowPlaying.value ?: NOTHING_PLAYING
        if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) != null) {
            mediaItemsPrivate.postValue(updateState(playbackState, metadata))
        }
    }

    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        val playbackState = musicServiceConnection.playbackState.value ?: EMPTY_PLAYBACK_STATE
        val metadata = it ?: NOTHING_PLAYING
        if (metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) != null) {
            mediaItemsPrivate.postValue(updateState(playbackState, metadata))
        }
    }

    private val musicServiceConnection = musicServiceConnection.also {
        it.subscribe(mediaId,subscriptionCallback)

        it.playbackState.observeForever(playbackStateObserver)
        it.nowPlaying.observeForever(mediaMetadataObserver)
    }

    override fun onCleared() {
        super.onCleared()

        // Remove the permanent observers from the MusicServiceConnection.
        musicServiceConnection.playbackState.removeObserver(playbackStateObserver)
        musicServiceConnection.nowPlaying.removeObserver(mediaMetadataObserver)

        // And then, finally, unsubscribe the media ID that was being watched.
        musicServiceConnection.unsubscribe(mediaId,subscriptionCallback)
    }

    private fun getResourceForMediaId(mediaId: String):Int{
        val isActive = mediaId==musicServiceConnection.nowPlaying.value?.id
        val isPlaying = musicServiceConnection.playbackState.value?.isPlaying?:false
        return when{
            !isActive -> NO_RES
            isPlaying-> R.drawable.ic_pause_black_24dp
            else -> R.drawable.ic_play_arrow_black_24dp
        }
    }

    private fun updateState(
        playbackState: PlaybackStateCompat,
        mediaMetadata: MediaMetadataCompat
    ):List<MediaItemData>{
        val newResId = when(playbackState.isPlaying){
            true -> R.drawable.ic_pause_black_24dp
            else -> R.drawable.ic_play_arrow_black_24dp
        }

        return mediaItems.value?.map {
            val useResId = if (it.mediaId == mediaMetadata.id) newResId else NO_RES
            it.copy(playbackRes =useResId)
        }?: emptyList()
    }

    class Factory(
        private val mediaId:String,
        private val musicServiceConnection: MusicServiceConnection
    ):ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MediaItemFragmentViewModel(mediaId, musicServiceConnection) as T
        }
    }
}

private const val TAG = "MediaItemFragmentVM"
private const val NO_RES = 0
