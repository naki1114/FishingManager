package com.example.fishingmanager.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import kotlin.concurrent.thread

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

    lateinit var loadingAnimationRight: Animation
    lateinit var loadingAnimationLeft: Animation
    var loadingAnimationStatus = false
    var animationThread: Thread = Thread()


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

        loadingAnimationRight = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_right)
        loadingAnimationLeft = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_left)

    } // setVariable()


    fun observeLiveData() {

        viewModel.liveDataWeather.observe(viewLifecycleOwner, Observer {

            if (it.skyImage == 0) {

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

                binding.homeWeatherLayout.visibility = View.GONE
                binding.homeWeatherErrorLayout.visibility = View.VISIBLE
                binding.homeWeatherErrorChildLayout.visibility = View.VISIBLE

            } else {

                binding.homeWeatherLayout.visibility = View.VISIBLE
                binding.homeWeatherErrorLayout.visibility = View.GONE
                binding.homeWeatherErrorChildLayout.visibility = View.GONE
                binding.homeWeatherSkyImage.setImageResource(it.skyImage)

            }

        })

        viewModel.liveDataRecommendList.observe(viewLifecycleOwner, Observer {

            if (it.size == 0) {

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

                binding.homeRecommendRecyclerView.visibility = View.GONE
                binding.homeRecommendErrorLayout.visibility = View.VISIBLE

            } else {

                binding.homeRecommendRecyclerView.visibility = View.VISIBLE
                binding.homeRecommendErrorLayout.visibility = View.GONE
                recommendAdapter.setItem(it)

            }

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

            if (it.size == 0) {

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

                binding.homeRecentCollectionRecyclerView.visibility = View.GONE
                binding.homeRecentCollectionErrorLayout.visibility = View.VISIBLE

            } else {

                binding.homeRecentCollectionRecyclerView.visibility = View.VISIBLE
                binding.homeRecentCollectionErrorLayout.visibility = View.GONE
                recentCollectionAdapter.setItem(it)
                seeMoreAdapter.setItem(it)

            }

        })

        viewModel.liveDataHotFeedList.observe(viewLifecycleOwner, Observer {

            if (it.size == 0) {

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

                binding.homeHotFeedRecyclerView.visibility = View.GONE
                binding.homeHotFeedErrorLayout.visibility = View.VISIBLE

            } else {

                binding.homeHotFeedRecyclerView.visibility = View.VISIBLE
                binding.homeHotFeedErrorLayout.visibility = View.GONE
                hotFeedAdapter.setItem(it)

            }

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

        viewModel.liveDataWeatherLoadingStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.homeWeatherErrorChildLayout.visibility = View.GONE
                binding.homeWeatherLoadingLayout.visibility = View.VISIBLE
                binding.homeWeatherLoadingRightImage.visibility = View.VISIBLE
                binding.homeWeatherLoadingRightImage.startAnimation(loadingAnimationRight)
                loadingAnimationStatus = true

                animationThread = thread {

                    try {

                        while (loadingAnimationStatus) {

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.homeWeatherLoadingRightImage.visibility = View.GONE
                                binding.homeWeatherLoadingLeftImage.visibility = View.VISIBLE
                                binding.homeWeatherLoadingLeftImage.startAnimation(loadingAnimationLeft)

                            }

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.homeWeatherLoadingLeftImage.visibility = View.GONE
                                binding.homeWeatherLoadingRightImage.visibility = View.VISIBLE
                                binding.homeWeatherLoadingRightImage.startAnimation(loadingAnimationRight)

                            }

                        }

                    } catch (e: InterruptedException) {

                        loadingAnimationStatus = false

                        Handler(Looper.getMainLooper()).post {

                            binding.homeWeatherLoadingRightImage.clearAnimation()
                            binding.homeWeatherLoadingLeftImage.clearAnimation()
                            binding.homeWeatherLoadingRightImage.visibility = View.GONE
                            binding.homeWeatherLoadingLeftImage.visibility = View.GONE
                            binding.homeWeatherLoadingLayout.visibility = View.GONE

                        }

                    }

                }


            } else {

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

            }

        })

        viewModel.liveDataIndexLoadingStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.homeRecommendErrorLayout.visibility = View.GONE
                binding.homeRecommendLoadingLayout.visibility = View.VISIBLE
                binding.homeRecommendLoadingRightImage.visibility = View.VISIBLE
                binding.homeRecommendLoadingRightImage.startAnimation(loadingAnimationRight)
                loadingAnimationStatus = true

                animationThread = thread {

                    try {

                        while (loadingAnimationStatus) {

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.homeRecommendLoadingRightImage.visibility = View.GONE
                                binding.homeRecommendLoadingLeftImage.visibility = View.VISIBLE
                                binding.homeRecommendLoadingLeftImage.startAnimation(loadingAnimationLeft)

                            }

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.homeRecommendLoadingLeftImage.visibility = View.GONE
                                binding.homeRecommendLoadingRightImage.visibility = View.VISIBLE
                                binding.homeRecommendLoadingRightImage.startAnimation(loadingAnimationRight)

                            }

                        }

                    } catch (e: InterruptedException) {

                        loadingAnimationStatus = false

                        Handler(Looper.getMainLooper()).post {

                            binding.homeRecommendLoadingRightImage.clearAnimation()
                            binding.homeRecommendLoadingLeftImage.clearAnimation()
                            binding.homeRecommendLoadingRightImage.visibility = View.GONE
                            binding.homeRecommendLoadingLeftImage.visibility = View.GONE
                            binding.homeRecommendLoadingLayout.visibility = View.GONE

                        }

                    }

                }


            } else {

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

            }

        })

        viewModel.liveDataCombineLoadingStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.homeRecentCollectionErrorLayout.visibility = View.GONE
                binding.homeRecentCollectionLoadingLayout.visibility = View.VISIBLE
                binding.homeRecentCollectionLoadingRightImage.visibility = View.VISIBLE
                binding.homeRecentCollectionLoadingRightImage.startAnimation(loadingAnimationRight)
                binding.homeHotFeedErrorLayout.visibility = View.GONE
                binding.homeHotFeedLoadingLayout.visibility = View.VISIBLE
                binding.homeHotFeedLoadingRightImage.visibility = View.VISIBLE
                binding.homeHotFeedLoadingRightImage.startAnimation(loadingAnimationRight)
                loadingAnimationStatus = true

                animationThread = thread {

                    try {

                        while (loadingAnimationStatus) {

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.homeRecentCollectionLoadingRightImage.visibility = View.GONE
                                binding.homeRecentCollectionLoadingLeftImage.visibility = View.VISIBLE
                                binding.homeRecentCollectionLoadingLeftImage.startAnimation(loadingAnimationLeft)
                                binding.homeHotFeedLoadingRightImage.visibility = View.GONE
                                binding.homeHotFeedLoadingLeftImage.visibility = View.VISIBLE
                                binding.homeHotFeedLoadingLeftImage.startAnimation(loadingAnimationLeft)

                            }

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.homeRecentCollectionLoadingLeftImage.visibility = View.GONE
                                binding.homeRecentCollectionLoadingRightImage.visibility = View.VISIBLE
                                binding.homeRecentCollectionLoadingRightImage.startAnimation(loadingAnimationRight)
                                binding.homeHotFeedLoadingLeftImage.visibility = View.GONE
                                binding.homeHotFeedLoadingRightImage.visibility = View.VISIBLE
                                binding.homeHotFeedLoadingRightImage.startAnimation(loadingAnimationRight)

                            }

                        }

                    } catch (e: InterruptedException) {

                        loadingAnimationStatus = false

                        Handler(Looper.getMainLooper()).post {

                            binding.homeRecentCollectionLoadingRightImage.clearAnimation()
                            binding.homeRecentCollectionLoadingLeftImage.clearAnimation()
                            binding.homeRecentCollectionLoadingRightImage.visibility = View.GONE
                            binding.homeRecentCollectionLoadingLeftImage.visibility = View.GONE
                            binding.homeRecentCollectionLoadingLayout.visibility = View.GONE
                            binding.homeHotFeedLoadingRightImage.clearAnimation()
                            binding.homeHotFeedLoadingLeftImage.clearAnimation()
                            binding.homeHotFeedLoadingRightImage.visibility = View.GONE
                            binding.homeHotFeedLoadingLeftImage.visibility = View.GONE
                            binding.homeHotFeedLoadingLayout.visibility = View.GONE

                        }

                    }

                }


            } else {

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

            }

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