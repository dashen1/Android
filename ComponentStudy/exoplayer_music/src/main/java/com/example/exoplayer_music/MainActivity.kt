package com.example.exoplayer_music

import android.media.AudioManager
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.exoplayer_music.fragment.MediaItemFragment
import com.example.exoplayer_music.utils.InjectorUtils
import com.example.exoplayer_music.viewmodels.MainActivityViewModel
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel> {
        InjectorUtils.provideMainActivityViewModel(this)
    }

    private var castContext: CastContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the Cast context. This is required so that the media route button can be
        // created in the AppBar
        castContext = CastContext.getSharedInstance(this)

        setContentView(R.layout.activity_main)

        // Since App is a music player, the volume controls should adjust the music volume while
        // in the app.
        volumeControlStream = AudioManager.STREAM_MUSIC

        /**
         *
         */
        viewModel.navigateToFragment.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { fragmentRequest ->
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragmentContainer, fragmentRequest.fragment, fragmentRequest.tag
                )
                if (fragmentRequest.backStack) transaction.addToBackStack(null)
                transaction.commit()
            }
        })

        viewModel.rootMediaId.observe(this,
            Observer<String> { rootMediaId ->
                rootMediaId?.let { navigateToMediaItem(it) }
            })

        viewModel.navigateToMediaItem.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { mediaId ->
                navigateToMediaItem(mediaId)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(com.example.exoplayer_common.R.menu.main_activity_menu, menu)

        /**
         * Set up a MediaRouteButton to allow the user to control the current media playback route.
         */

        CastButtonFactory.setUpMediaRouteButton(
            this,
            menu,
            com.example.exoplayer_common.R.id.media_route_menu_item
        )
        return true
    }

    private fun navigateToMediaItem(mediaId: String) {
        var fragment: MediaItemFragment? = getBrowserFragment(mediaId)
        if (fragment == null) {
            fragment = MediaItemFragment.newInstance(mediaId)
            // If this is not the top level media (root), we add it to the fragment
            // back stack, so that actionbar toggle and back will work appropriately.
            viewModel.showFragment(fragment, !isRooted(mediaId), mediaId)
        }
    }

    private fun isRooted(mediaId: String) = mediaId == viewModel.rootMediaId.value

    private fun getBrowserFragment(mediaId: String): MediaItemFragment? {
        return supportFragmentManager.findFragmentByTag(mediaId) as? MediaItemFragment
    }

}