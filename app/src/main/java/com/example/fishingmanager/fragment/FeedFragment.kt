package com.example.fishingmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.FeedAdapter
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.databinding.FragmentFeedBinding
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.viewModel.FeedViewModel
import java.text.SimpleDateFormat

class FeedFragment : Fragment() {

    private lateinit var feedViewModel : FeedViewModel
    private lateinit var binding : FragmentFeedBinding
    private lateinit var adapter : FeedAdapter

    private lateinit var feedList : ArrayList<Feed>

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

        setVariable()

        // LiveData Observe
        clickAddFeedButton()
        readFeed()
        toFeedList()
        searchKeyword()
    }

    // Custom Method

    private fun setVariable() {

        feedViewModel = FeedViewModel()
        binding.viewModel = feedViewModel

        feedViewModel.getFeed()

        feedViewModel.feedList.observe(viewLifecycleOwner) {

            feedList = feedViewModel.feedList.value!!

            adapter = FeedAdapter(FeedAdapter.ItemClickListener {
                feedViewModel.readFeed(it)
            }, feedList)
            binding.feedRecyclerView.adapter = adapter

        }

    } // setVariable

    private fun clickAddFeedButton() {

        feedViewModel.toWriteLiveData.observe(viewLifecycleOwner) {

            if (feedViewModel.toWriteLiveData.value == true) {

                (activity as MainActivity).changeFragment("write")

            }

        }

    } // clickAddFeedButton

    private fun readFeed() {

        feedViewModel.toReadLiveData.observe(viewLifecycleOwner) {

            val feed = feedViewModel.toReadLiveData.value
            val dateFormat = SimpleDateFormat("yyyy / MM / dd")
            val date = dateFormat.format(feed?.date!!.toLong()).toString()

            binding.feedViewWriterTextView.text = feed?.nickname
            binding.feedViewTitleTextView.text = feed?.title
            binding.feedViewContentTextView.text = feed?.content
            binding.feedViewWriteDateTextView.text = date

            if (feed?.feedImage == null) {
                binding.feedViewImageView.visibility = View.GONE
            }
            else {
                Glide.with(requireContext()).load(RetrofitClient.BASE_URL + feed?.feedImage).into(binding.feedViewImageView)
                binding.feedViewImageView.visibility = View.VISIBLE
            }

            binding.feedLayout.visibility = View.GONE
            binding.feedViewLayout.visibility = View.VISIBLE

            bottomNavigationVisibility(false)

        }

    } // readFeed

    private fun toFeedList() {

        feedViewModel.toFeedListLiveData.observe(viewLifecycleOwner) {

            binding.feedLayout.visibility = View.VISIBLE
            binding.feedViewLayout.visibility = View.GONE
            bottomNavigationVisibility(true)

        }

    } // toFeedList

    private fun searchKeyword() {

        feedViewModel.searchLiveData.observe(viewLifecycleOwner) {

            feedList = ArrayList<Feed>()
            val type = feedViewModel.searchLiveData.value?.get(0)
            val keyword = feedViewModel.searchLiveData.value?.get(1)
            val originalList = (activity as MainActivity).feedList
            val size = originalList.size

            for (i in 0 ..< size) {

                if (type == "제목") {

                    if (originalList[i].title.contains(keyword.toString())) {

                        feedList.add(originalList[i])

                    }

                }
                else {

                    if (originalList[i].nickname == keyword) {

                        feedList.add(originalList[i])

                    }

                }

            }

            adapter = FeedAdapter(FeedAdapter.ItemClickListener {
                feedViewModel.readFeed(it)
            }, feedList)
            binding.feedRecyclerView.adapter = adapter

        }

    } // searchKeyword

    private fun bottomNavigationVisibility(visibility : Boolean) {

        if (visibility) {
            (activity as MainActivity).binding.navigation.visibility = View.VISIBLE
        }
        else {
            (activity as MainActivity).binding.navigation.visibility = View.GONE
        }

    } // bottomNavigationVisibility

}