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
import com.example.fishingmanager.adapter.CheckingFishHistoryAdapter
import com.example.fishingmanager.databinding.FragmentCheckingFishBinding
import com.example.fishingmanager.viewModel.CheckingFishViewModel
import kotlin.concurrent.thread

class CheckingFishFragment : Fragment() {

    lateinit var binding : FragmentCheckingFishBinding

    lateinit var viewModel : CheckingFishViewModel
    lateinit var adapter : CheckingFishHistoryAdapter

    lateinit var userInfoShared : SharedPreferences
    lateinit var nickname : String

    lateinit var loadingAnimationRight: Animation
    lateinit var loadingAnimationLeft: Animation
    var loadingAnimationStatus = false
    lateinit var animationThread: Thread

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checking_fish, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUserShared()
        setVariable()
        observeLiveData()
        getData()

    } // onViewCreated()


    fun setVariable() {

        viewModel = CheckingFishViewModel((activity as MainActivity).historyList, nickname)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = CheckingFishHistoryAdapter(CheckingFishHistoryAdapter.ItemClickListener {
            viewModel.goPhotoView(it.fishImage)
        })

        binding.checkingFishHistoryRecyclerView.adapter = adapter

        loadingAnimationRight = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_right)
        loadingAnimationLeft = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_left)

    } // setVariable()


    fun observeLiveData() {

        viewModel.liveDataHistoryList.observe(viewLifecycleOwner, Observer {

            if (viewModel.liveDataBasicHistoryList.value?.size == 0) {
                binding.checkingFishMainLayout.visibility = View.GONE
                binding.checkingFishResponseFailureLayout.visibility = View.VISIBLE
            } else {
                adapter.setItem(it)
            }

        })

        viewModel.liveDataClickedFishImage.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).goPhotoView(it)

        })

        viewModel.liveDataChangeLayout.observe(viewLifecycleOwner, Observer {

            changeLayout(it)

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

        viewModel.liveDataLoadingStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.checkingFishResponseFailureLayout.visibility = View.GONE
                binding.checkingFishLoadingLayout.visibility = View.VISIBLE

                binding.checkingFishLoadingRightImage.visibility = View.VISIBLE
                binding.checkingFishLoadingRightImage.startAnimation(loadingAnimationRight)
                loadingAnimationStatus = true

                animationThread = thread {

                    try {

                        while (loadingAnimationStatus) {

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.checkingFishLoadingRightImage.visibility = View.GONE
                                binding.checkingFishLoadingLeftImage.visibility = View.VISIBLE
                                binding.checkingFishLoadingLeftImage.startAnimation(
                                    loadingAnimationLeft
                                )

                            }

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.checkingFishLoadingLeftImage.visibility = View.GONE
                                binding.checkingFishLoadingRightImage.visibility = View.VISIBLE
                                binding.checkingFishLoadingRightImage.startAnimation(
                                    loadingAnimationRight
                                )

                            }

                        }

                    } catch (e: InterruptedException) {

                        loadingAnimationStatus = false

                        Handler(Looper.getMainLooper()).post {

                            binding.checkingFishLoadingRightImage.clearAnimation()
                            binding.checkingFishLoadingLeftImage.clearAnimation()
                            binding.checkingFishLoadingRightImage.visibility = View.GONE
                            binding.checkingFishLoadingLeftImage.visibility = View.GONE
                            binding.checkingFishLoadingLayout.visibility = View.GONE

                            if (viewModel.liveDataBasicHistoryList.value?.size == 0) {

                                binding.checkingFishResponseFailureLayout.visibility = View.VISIBLE

                            } else {

                                binding.checkingFishMainLayout.visibility = View.VISIBLE

                            }

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

        viewModel.init()

    } // getData()


    fun changeLayout(layout : String) {

        (activity as MainActivity).changeFragment(layout)

    } // changeLayout()


    fun checkUserShared() {

        userInfoShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = userInfoShared.getString("nickname", "").toString()

    } // checkUserShared()

}