package com.example.fishingmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.databinding.FragmentFeedBinding
import com.example.fishingmanager.viewModel.FeedViewModel

class FeedFragment(private val feedList : ArrayList<Feed>) : Fragment() {

    private lateinit var feedViewModel : FeedViewModel
    private lateinit var binding : FragmentFeedBinding

    // Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedViewModel = FeedViewModel(feedList)
        binding.viewModel = feedViewModel

        // LiveData Observe
        clickAddFeedButton()
    }

    // Custom Method

    private fun clickAddFeedButton() {

        feedViewModel.toWriteLiveData.observe(viewLifecycleOwner) {

            if (feedViewModel.toWriteLiveData.value == true) {

                (activity as MainActivity).changeFragment("write")

            }

        }

    }

}