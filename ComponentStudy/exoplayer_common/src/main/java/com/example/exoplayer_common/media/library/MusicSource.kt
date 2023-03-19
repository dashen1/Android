package com.example.exoplayer_common.media.library

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.annotation.IntDef
import com.example.exoplayer_common.media.extensions.*

/**
 * Interface used by [MusicService] for looking up [MediaMetadataCompat] objects.
 *
 * Because Kotlin provides methods such as [Iterable.find] and [Iterable.filter],
 * this is a convenient interface to have on sources.
 */

interface MusicSource : Iterable<MediaMetadataCompat> {

    /**
     * Begins loading the data for this music source.
     */
    suspend fun load()

    /**
     * Methods which will perform a given action after this [MusicSOurce] is ready to be used.
     *
     * @param performAction A lambda expression to be called with a boolean parameter when
     * the source is ready. `true` indicates the source was successfully prepared, `false`
     * indicates an error occurred.
     * */
    fun whenReady(performAction: (Boolean) -> Unit): Boolean

    fun search(query: String, extra: Bundle): List<MediaMetadataCompat>
}

@IntDef(
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class State

/**
 * State indicating the source was created, but no initialization has performed.
 */
const val STATE_CREATED = 1

/**
 * State indicating initialization og the source is in the progress.
 */
const val STATE_INITIALIZING = 2

/**
 * State indicating the source has been initialized and is ready to be used.
 */
const val STATE_INITIALIZED = 3

/**
 * State indicating an error has occurred.
 */
const val STATE_ERROR = 4

/**
 * Basic class for music sources in this app.
 */

abstract class AbstractMusicSource : MusicSource {
    @State
    var state: Int = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListener) {
                    field = value
                    onReadyListener.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    private val onReadyListener = mutableListOf<(Boolean) -> Unit>()

    /**
     * Performs an action when this MusicSource is ready.
     *
     * This method is *not* threadsafe. Ensure actions and state changes are only performed
     * on a single thread.
     */

    override fun whenReady(performAction: (Boolean) -> Unit): Boolean =
        when(state){
            STATE_CREATED, STATE_INITIALIZING->{
                onReadyListener+=performAction
                false
            }
            else ->{
                performAction(state!= STATE_ERROR)
                true
            }
        }

    /**
     * Handles searching a [MusicSource] from a focused voice search, often
     * coming from the Google Assistant.
     */

    override fun search(query: String, extras: Bundle): List<MediaMetadataCompat> {
        // First attempt to search with the "focus"that's provided in the extra.
        val focusSearchResult = when(extras[MediaStore.EXTRA_MEDIA_FOCUS]){
            MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE ->{
                // For a Genre focused search, only genre is set.
                val genre = extras[EXTRA_MEDIA_GENRE]
                Log.d(TAG,"Focused genre search:'$genre'")
                filter { song->
                    song.genre == genre
                }
            }
            MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE->{
                // For an Artist search, only the artist is set.
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                Log.d(TAG,"Focused artist search:'$artist'")
                filter { song->
                    (song.artist==artist||song.albumArtist==artist)
                }
            }
            MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE->{
                // For an Album focused search, album and artist are set.
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                Log.d(TAG, "Focused album search: album='$album' artist='$artist")
                filter { song->
                    (song.artist==artist||song.albumArtist==album)
                }
            }
            MediaStore.Audio.Media.ENTRY_CONTENT_TYPE -> {
                // For a Song (aka Media) focused search, title, album, and artist are set.
                val title = extras[MediaStore.EXTRA_MEDIA_TITLE]
                val album = extras[MediaStore.EXTRA_MEDIA_ALBUM]
                val artist = extras[MediaStore.EXTRA_MEDIA_ARTIST]
                Log.d(TAG, "Focused media search: title='$title' album='$album' artist='$artist")
                filter { song ->
                    (song.artist == artist || song.albumArtist == artist) && song.album == album
                            && song.title == title
                }
            }
            else ->{
                // There isn't a focus, so no results yet.
                emptyList()
            }
        }

        if (focusSearchResult.isEmpty()){
            return if (query.isNotBlank()){
                Log.d(TAG, "Unfocused search for '$query'")
                filter { song->
                    song.title.containsCaseInsensitive(query)
                            || song.genre.containsCaseInsensitive(query)
                }
            }else{
                // If the user asked to "play music", or something similar, the quey also will
                // be blank, Given the small datalog of songs in the sample, just return them all,
                // shuffled,as something to play.
                Log.d(TAG, "Unfocused search without keyword")
                return shuffled()
            }
        }else{
            return focusSearchResult
        }
    }

    /**
     * [MediaStore.EXTRA_MEDIA_GENRE] is missing on API 19. Hide this fact by using our
     * own version of it.
     */
    private val EXTRA_MEDIA_GENRE
    get() = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
        MediaStore.EXTRA_MEDIA_GENRE
    }else{
        "android.intent.extra.genre"
    }
}

private const val TAG = "MusicSource"

