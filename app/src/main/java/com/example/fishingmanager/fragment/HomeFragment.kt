package com.example.fishingmanager.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.HomeHotFeedAdapter
import com.example.fishingmanager.adapter.HomeRecentCollectionAdapter
import com.example.fishingmanager.adapter.HomeRecommendAdapter
import com.example.fishingmanager.adapter.HomeSeeMoreRecentCollectionAdapter
import com.example.fishingmanager.data.SearchLocation
import com.example.fishingmanager.databinding.FragmentHomeBinding
import com.example.fishingmanager.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    lateinit var locationShared: SharedPreferences
    lateinit var userShared: SharedPreferences
    lateinit var location: SearchLocation
    lateinit var nickname: String
    lateinit var recommendAdapter: HomeRecommendAdapter
    lateinit var recentCollectionAdapter: HomeRecentCollectionAdapter
    lateinit var seeMoreAdapter: HomeSeeMoreRecentCollectionAdapter
    lateinit var hotFeedAdapter: HomeHotFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkSharedPreference()
        setVariable()
        observeLiveData()
        getData()

    } // onViewCreated()


    fun setVariable() {

        viewModel = HomeViewModel(
            (activity as MainActivity).weatherList,
            location,
            nickname,
            (activity as MainActivity).indexList,
            (activity as MainActivity).historyList,
            (activity as MainActivity).feedList
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        recommendAdapter = HomeRecommendAdapter()
        recentCollectionAdapter =
            HomeRecentCollectionAdapter(HomeRecentCollectionAdapter.ItemClickListener {
                viewModel.goPhotoView(it.fishImage)
            })
        seeMoreAdapter = HomeSeeMoreRecentCollectionAdapter()
        hotFeedAdapter = HomeHotFeedAdapter(HomeHotFeedAdapter.ItemClickListener {
            viewModel.goHotFeed(it.feedNum)
        })

        binding.homeRecommendRecyclerView.adapter = recommendAdapter
        binding.homeRecentCollectionRecyclerView.adapter = recentCollectionAdapter
        binding.homeSeeMoreRecyclerView.adapter = seeMoreAdapter
        binding.homeHotFeedRecyclerView.adapter = hotFeedAdapter

    } // setVariable()


    fun observeLiveData() {

        viewModel.liveDataWeather.observe(viewLifecycleOwner, Observer {

            binding.homeWeatherSkyImage.setImageResource(it.skyImage)

        })

        viewModel.liveDataRecommendList.observe(viewLifecycleOwner, Observer {

            recommendAdapter.setItem(it)

        })

        viewModel.liveDataChangeFragment.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).changeFragmentWithData(it)

        })

        viewModel.liveDataClickedFishImage.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).goPhotoView(it)

        })

        viewModel.liveDataChangeLayout.observe(viewLifecycleOwner, Observer {

            changeLayout(it)

        })

        viewModel.liveDataRecentCollectionList.observe(viewLifecycleOwner, Observer {

            recentCollectionAdapter.setItem(it)
            seeMoreAdapter.setItem(it)

        })

        viewModel.liveDataHotFeedList.observe(viewLifecycleOwner, Observer {

            hotFeedAdapter.setItem(it)

        })

        viewModel.liveDataHotFeedNum.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).changeFragment("feed")

        })

        viewModel.liveDataBasicWeatherList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).weatherList = it

        })

        viewModel.liveDataBasicIndexList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).indexList = it

        })

        viewModel.liveDataBasicCollectionList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).collectionList = it

        })

        viewModel.liveDataBasicHistoryList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).historyList = it

        })

        viewModel.liveDataBasicFeedList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).feedList = it

        })

        viewModel.liveDataBasicUserInfo.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).userInfo = it

        })

    } // observeLiveData()


    fun getData() {

        viewModel.getWeather()
        viewModel.getRecommendList()
        viewModel.getRecentCollectionList()
        viewModel.getHotFeedList()

    } // getData()


    fun checkSharedPreference() {

        locationShared =
            requireActivity().getSharedPreferences("location", AppCompatActivity.MODE_PRIVATE)

        val locationName = locationShared.getString("location", "").toString()
        val obsCode = locationShared.getString("obscode", "").toString()
        val lat = locationShared.getString("lat", "").toString()
        val lon = locationShared.getString("lon", "").toString()

        location = SearchLocation(locationName, obsCode, lat, lon)

        userShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)

        nickname = userShared.getString("nickname", "").toString()

    }


    fun changeLayout(layout: String) {

        when (layout) {

            "main" -> {
                binding.homeSeeMoreLayout.visibility = View.GONE
                binding.homeMainLayout.visibility = View.VISIBLE
            }

            "seeMore" -> {
                binding.homeMainLayout.visibility = View.GONE
                binding.homeSeeMoreLayout.visibility = View.VISIBLE
            }

        }

    } // changeLayout()


}