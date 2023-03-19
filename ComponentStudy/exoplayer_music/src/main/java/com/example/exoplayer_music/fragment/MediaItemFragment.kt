package com.example.exoplayer_music.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.exoplayer_music.MediaItemAdapter
import com.example.exoplayer_music.databinding.FragmentMediaitemListBinding
import com.example.exoplayer_music.utils.InjectorUtils
import com.example.exoplayer_music.viewmodels.MainActivityViewModel
import com.example.exoplayer_music.viewmodels.MediaItemFragmentViewModel

/**
 * A fragment representing a list of MediaItems
 */
class MediaItemFragment:Fragment(){

    private lateinit var mediaId:String
    private lateinit var binding:FragmentMediaitemListBinding

    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>{
        InjectorUtils.provideMainActivityViewModel(requireContext())
    }

    private val mediaItemFragmentViewModel by viewModels<MediaItemFragmentViewModel> {
        InjectorUtils.provideMediaItemFragmentViewModel(requireContext(), mediaId)
    }

    private val listAdapter = MediaItemAdapter{ clickedItem->
        mainActivityViewModel.mediaItemClicked(clickedItem)
    }

    companion object{
        fun newInstance(mediaId:String):MediaItemFragment{
            return MediaItemFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(MEDIA_ID_ARG,mediaId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaitemListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Always true, but lets lint know that as well
        mediaId=arguments?.getString(MEDIA_ID_ARG)?:return
        mediaItemFragmentViewModel.mediaItems.observe(viewLifecycleOwner,
        Observer{ list->
            binding.loadingSpinner.visibility =
                if (list?.isNotEmpty()==true) View.GONE else View.VISIBLE
            listAdapter.submitList(list)
        })
        mediaItemFragmentViewModel.networkError.observe(viewLifecycleOwner,
        Observer{error->
           if (error){
               binding.loadingSpinner.visibility = View.GONE
               binding.networkError.visibility = View.VISIBLE
           }else{
               binding.networkError.visibility = View.GONE
           }
        })

        // Set the adapter
        binding.list.adapter = listAdapter
    }

}

private const val MEDIA_ID_ARG = "com.example.android.uamp.fragments.MediaItemFragment.MEDIA_ID"