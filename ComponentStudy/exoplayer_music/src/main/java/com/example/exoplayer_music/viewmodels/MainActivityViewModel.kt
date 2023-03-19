package com.example.exoplayer_music.viewmodels

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.exoplayer_common.common.MusicServiceConnection
import com.example.exoplayer_common.media.extensions.id
import com.example.exoplayer_common.media.extensions.isPlayEnabled
import com.example.exoplayer_common.media.extensions.isPlaying
import com.example.exoplayer_common.media.extensions.isPrepared
import com.example.exoplayer_music.MediaItemData
import com.example.exoplayer_music.utils.Event

/**
 * Small [ViewModel] that watches a [MusicServiceConnection] to become connected
 * and provides the root/initial media ID of the underlying [MediaBrowserCompat]
 */

class MainActivityViewModel(private val musicServiceConnection: MusicServiceConnection) :
    ViewModel() {

    val rootMediaId: LiveData<String> =
        Transformations.map(musicServiceConnection.isConnected) { isConnected ->
            if (isConnected) {
                musicServiceConnection.rootMediaId
            } else {
                null
            }
        }

    /**
     *
     */

    private val navigateToMediaItemPrivate = MutableLiveData<Event<String>>()
    val navigateToMediaItem: LiveData<Event<String>> get() = navigateToMediaItemPrivate

    private val navigateToFragmentPrivate = MutableLiveData<Event<FragmentNavigationRequest>>()
    val navigateToFragment: LiveData<Event<FragmentNavigationRequest>> get() = navigateToFragmentPrivate

    fun mediaItemClicked(clickedItem: MediaItemData) {
        if (clickedItem.browsable) {
            browserToItem(clickedItem)
        }else{

        }
    }

    fun showFragment(fragment: Fragment, backStack: Boolean = true, tag: String? = null) {
        navigateToFragmentPrivate.value = Event(FragmentNavigationRequest(fragment, backStack, tag))
    }

    private fun browserToItem(mediaItem: MediaItemData) {
        navigateToMediaItemPrivate.value = Event(mediaItem.mediaId)
    }

    fun playMedia(mediaItem: MediaItemData,pauseAllowed:Boolean=true){
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val transportControls = musicServiceConnection.transportControls

        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared?:false
        if (isPrepared&&mediaItem.mediaId==nowPlaying?.id){
            musicServiceConnection.playbackState.value?.let { playbackState->
                when{
                    playbackState.isPlaying->
                        if (pauseAllowed) transportControls.pause() else Unit
                    playbackState.isPlayEnabled -> transportControls.play()
                    else ->{
                        Log.w(
                            TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                    " (mediaId=${mediaItem.mediaId})"
                        )
                    }
                }
            }
        }else{
            transportControls.playFromMediaId(mediaItem.mediaId,null)
        }
    }

    fun playMediaId(mediaId:String){
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val transportControls = musicServiceConnection.transportControls

        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared?:false
        if (isPrepared&&mediaId==nowPlaying?.id){
            musicServiceConnection.playbackState.value?.let { playbackState->
                when{
                    playbackState.isPlaying ->transportControls.pause()
                    playbackState.isPlayEnabled->transportControls.play()
                    else ->{
                        Log.w(
                            TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                    " (mediaId=$mediaId)"
                        )
                    }
                }
            }
        }else{
            transportControls.playFromMediaId(mediaId,null)
        }
    }

    class Factory(
        private val musicServiceConnection: MusicServiceConnection
    ):ViewModelProvider.NewInstanceFactory(){

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel(musicServiceConnection) as T
        }
    }
}

data class FragmentNavigationRequest(
    val fragment: Fragment,
    val backStack: Boolean = false,
    val tag: String? = null
)

private const val TAG = "MainActivityVM"