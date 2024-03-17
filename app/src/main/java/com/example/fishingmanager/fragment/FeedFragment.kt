package com.example.fishingmanager.fragment

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.FeedAdapter
import com.example.fishingmanager.adapter.FeedCommentAdapter
import com.example.fishingmanager.data.Comment
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.databinding.FragmentFeedBinding
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.viewModel.FeedViewModel
import java.text.SimpleDateFormat

class FeedFragment: Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var binding: FragmentFeedBinding

    private lateinit var feedAdapter: FeedAdapter
    private lateinit var feedCommentAdapter: FeedCommentAdapter

    private lateinit var feedList: ArrayList<Feed>
    private lateinit var feedCommentList: ArrayList<Comment>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

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


    // 초기화
    private fun setVariable() {

        val nickname = (activity as MainActivity).nickname

        feedViewModel = FeedViewModel(nickname)
        binding.viewModel = feedViewModel

        feedViewModel.getFeed()

        feedViewModel.feedList.observe(viewLifecycleOwner) {

            feedList = feedViewModel.feedList.value!!

            feedAdapter = FeedAdapter(FeedAdapter.ItemClickListener {
                feedViewModel.readFeed(it)
            }, feedList)
            binding.feedRecyclerView.adapter = feedAdapter

        }

        feedViewModel.feedCommentList.observe(viewLifecycleOwner) {

            feedCommentList = feedViewModel.feedCommentList.value!!

            feedCommentAdapter = FeedCommentAdapter(feedCommentList)
            binding.feedViewCommentRecyclerView.adapter = feedCommentAdapter
            binding.feedViewCommentEditText.text = null
            binding.feedViewCommentCountTextView.text = feedCommentList.size.toString()

        }

        parentFragmentManager.setFragmentResultListener("feed", this) { key, bundle ->

            val feed = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                bundle.getSerializable("feed", Feed::class.java)

            } else {

                bundle.getSerializable("feed") as Feed

            }

            if (feed != null) {

                feedViewModel.goFeedView(feed)

            }

        }

    } // setVariable()


    // 게시글 추가 버튼 클릭시 Write Fragment로 전환
    private fun clickAddFeedButton() {

        feedViewModel.toWriteLiveData.observe(viewLifecycleOwner) {

            if (it) {

                (activity as MainActivity).changeFragment("write")

            }

        }

    } // clickAddFeedButton()


    // 게시글 상세 보기
    private fun readFeed() {

        feedViewModel.toReadLiveData.observe(viewLifecycleOwner) {

            val feed = it
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            var date = feed.date

            if (feed.date.length != 10) {
                date = dateFormat.format(feed.date.toLong()).toString()
            }

            binding.feedViewWriterTextView.text = feed.nickname
            binding.feedViewTitleTextView.text = feed.title
            binding.feedViewContentTextView.text = feed.content
            binding.feedViewWriteDateTextView.text = date

            if (it.profileImage.length < 3) {

                if (it.profileImage == "FM") {

                    binding.feedViewWriterProfileImageView.setImageResource(R.drawable.fishing_logo)

                }

            } else {

                if (it.profileImage.substring(0,5) == "https") {

                    Glide.with(requireActivity()).load(it.profileImage).into(binding.feedViewWriterProfileImageView)

                } else {

                    Glide.with(requireActivity()).load(RetrofitClient.BASE_URL + it.profileImage).into(binding.feedViewWriterProfileImageView)

                }

            }

            if (feed?.feedImage == null || feed?.feedImage == "") {

                binding.feedViewImageView.visibility = View.GONE

            } else {
                
                Glide.with(requireContext()).load(RetrofitClient.BASE_URL + feed?.feedImage).into(binding.feedViewImageView)
                binding.feedViewImageView.visibility = View.VISIBLE

            }

            binding.feedLayout.visibility = View.GONE
            binding.feedViewLayout.visibility = View.VISIBLE

            bottomNavigationVisibility(false)

        }

    } // readFeed()


    // 게시글 목록 보기
    private fun toFeedList() {

        feedViewModel.toFeedListLiveData.observe(viewLifecycleOwner) {

            binding.feedLayout.visibility = View.VISIBLE
            binding.feedViewLayout.visibility = View.GONE
            bottomNavigationVisibility(true)

        }

    } // toFeedList()


    // 검색 키워드에 맞게 게시글 목록 조회
    private fun searchKeyword() {

        feedViewModel.searchLiveData.observe(viewLifecycleOwner) {

            feedList = arrayListOf()
            val type = it[0]
            val keyword = it[1]
            val originalList = (activity as MainActivity).feedList
            val size = originalList.size

            for (i in 0 until size) {

                if (type == "제목") {

                    if (originalList[i].title.contains(keyword)) {

                        feedList.add(originalList[i])

                    }

                } else {

                    if (originalList[i].nickname == keyword) {

                        feedList.add(originalList[i])

                    }

                }

            }

            feedAdapter = FeedAdapter(FeedAdapter.ItemClickListener {

                feedViewModel.readFeed(it)

            }, feedList)

            binding.feedRecyclerView.adapter = feedAdapter

        }

    } // searchKeyword()


    // 바텀 내비게이션 visibility 설정
    private fun bottomNavigationVisibility(visibility: Boolean) {

        if (visibility) {

            (activity as MainActivity).binding.navigation.visibility = View.VISIBLE

        } else {

            (activity as MainActivity).binding.navigation.visibility = View.GONE

        }

    } // bottomNavigationVisibility()


}