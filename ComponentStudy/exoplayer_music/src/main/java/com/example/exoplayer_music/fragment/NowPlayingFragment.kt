package com.example.exoplayer_music.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.exoplayer_music.R
import com.example.exoplayer_music.databinding.FragmentNowplayingBinding
import com.example.exoplayer_music.utils.InjectorUtils
import com.example.exoplayer_music.viewmodels.MainActivityViewModel
import com.example.exoplayer_music.viewmodels.NowPlayingFragmentViewModel
import com.example.exoplayer_music.viewmodels.NowPlayingFragmentViewModel.NowPlayingMetadata

/**
 *  A fragment representing the current media item being played.
 */
class NowPlayingFragment : Fragment() {

    private val mainActivityViewModel by activityViewModels<MainActivityViewModel> {
        InjectorUtils.provideMainActivityViewModel(requireContext())
    }

    private val nowPlayingViewModel by viewModels<NowPlayingFragmentViewModel> {
        InjectorUtils.provideNowPlayingFragmentViewModel(requireContext())
    }

    lateinit var binding: FragmentNowplayingBinding

    companion object {
        fun newInstance() = NowPlayingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNowplayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Always true, but lets lint know that as well.
        val context = activity ?: true

        // Attach observers to the LiveData coming from this ViewModel
        nowPlayingViewModel.mediaMetadata.observe(viewLifecycleOwner,
            Observer { mediaItem -> updateUI(view, mediaItem) })
        nowPlayingViewModel.mediaButtonRes.observe(viewLifecycleOwner,
        Observer { res->
            binding.mediaButton.setImageResource(res)
        })
        nowPlayingViewModel.mediaPosition.observe(viewLifecycleOwner,
        Observer { pos->
            binding.position.text = NowPlayingMetadata.timestampToMSS(context as Context,pos)
        })

        // Set UI handlers for buttons.
        binding.mediaButton.setOnClickListener {
            nowPlayingViewModel.mediaMetadata.value?.let { mainActivityViewModel.playMediaId(it.id) }
        }

        // Initialize playback duration and position to zero
        binding.duration.text = NowPlayingMetadata.timestampToMSS(context as Context,0L)
        binding.position.text = NowPlayingMetadata.timestampToMSS(context, 0L)
    }

    /**
     *  Internal function used to update all UI elements except for the current item playback.
     */
    private fun updateUI(view: View, metadata: NowPlayingMetadata) =
        with(binding) {
            if (metadata.albumArtUri == Uri.EMPTY) {
                albumArt.setImageResource(R.drawable.ic_album_black_24dp)
            } else {
                Glide.with(view)
                    .load(metadata.albumArtUri)
                    .into(albumArt)
            }
            title.text = metadata.title
            subtitle.text = metadata.subtitle
            duration.text = metadata.duration
        }

}