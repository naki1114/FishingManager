package com.example.fishingmanager.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.ProfileCollectionAdapter
import com.example.fishingmanager.adapter.ProfileHistoryAdapter
import com.example.fishingmanager.adapter.ProfileSelectFishAdapter
import com.example.fishingmanager.databinding.FragmentProfileBinding
import com.example.fishingmanager.viewModel.ProfileViewModel

class ProfileFragment : Fragment() {

    lateinit var binding : FragmentProfileBinding

    lateinit var viewModel : ProfileViewModel
    lateinit var collectionAdapter : ProfileCollectionAdapter
    lateinit var historyAdapter : ProfileHistoryAdapter
    lateinit var selectFishAdapter : ProfileSelectFishAdapter

    lateinit var userInfoShared : SharedPreferences
    lateinit var nickname : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

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

        viewModel = ProfileViewModel((activity as MainActivity).collectionList, (activity as MainActivity).historyList, nickname)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        collectionAdapter = ProfileCollectionAdapter(ProfileCollectionAdapter.ItemClickListener {
            viewModel.readMoreFish(it)
        })
        historyAdapter = ProfileHistoryAdapter()
        selectFishAdapter = ProfileSelectFishAdapter(ProfileSelectFishAdapter.ItemClickListener {

        })

        binding.profileCollectionRecyclerView.adapter =  collectionAdapter
        binding.profileHistoryRecyclerView.adapter = historyAdapter
        binding.profileSelectFishRecyclerView.adapter = selectFishAdapter


    } // setVariable()


    fun observeLiveData() {

        viewModel.liveDataCollectionList.observe(viewLifecycleOwner, Observer {

            collectionAdapter.setItem(it)

        })

        viewModel.liveDataHistoryList.observe(viewLifecycleOwner, Observer {

            historyAdapter.setItem(it)

        })

        viewModel.liveDataFishList.observe(viewLifecycleOwner, Observer {

            selectFishAdapter.setItem(it)

        })

        viewModel.liveDataReadMoreFish.observe(viewLifecycleOwner, Observer {

            binding.collection = it
            Glide.with(requireActivity()).load(it.fishImage).into(binding.profileCollectionReadMoreImage)

        })

        viewModel.liveDataChangeTab.observe(viewLifecycleOwner, Observer {

            changeTab(it)

        })

        viewModel.liveDataChangeLayout.observe(viewLifecycleOwner, Observer {

            changeLayout(it)

        })

        viewModel.liveDataClickedMenu.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.profileDrawerLayout.openDrawer(binding.drawerView)

            }

        })

        viewModel.liveDataChangeFragment.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).changeFragment(it)

        })

    } // observeLiveData()


    fun getData() {

        viewModel.init()

    } // getData()


    fun checkUserShared() {

        userInfoShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = userInfoShared.getString("nickname", "").toString()

    } // checkUserShared()


    fun changeTab(tab : String) {

        when(tab) {

            "collection" -> {
                binding.profileHistoryLayout.visibility = View.GONE
                binding.profileCollectionLayout.visibility = View.VISIBLE
                binding.profileCollectionButton.visibility = View.GONE
                binding.profileCollectionBlock.visibility = View.VISIBLE
                binding.profileHistoryButton.visibility = View.VISIBLE
                binding.profileHistoryBlock.visibility = View.GONE
            }

            "history" -> {
                binding.profileHistoryLayout.visibility = View.VISIBLE
                binding.profileCollectionLayout.visibility = View.GONE
                binding.profileCollectionButton.visibility = View.VISIBLE
                binding.profileCollectionBlock.visibility = View.GONE
                binding.profileHistoryButton.visibility = View.GONE
                binding.profileHistoryBlock.visibility = View.VISIBLE
            }

        }

    } // changeTab()


    fun changeLayout(layout : String) {

        when(layout) {

            "main" -> {
                binding.profileMainLayout.visibility = View.VISIBLE
                binding.profileCollectionReedMoreLayout.visibility = View.GONE
                binding.profileSelectFishLayout.visibility = View.GONE
                binding.profileSelectLengthLayout.visibility = View.GONE
                binding.profileSelectDateLayout.visibility = View.GONE
                (activity as MainActivity).navigationVisible()
            }

            "readMore" -> {
                binding.profileMainLayout.visibility = View.GONE
                binding.profileCollectionReedMoreLayout.visibility = View.VISIBLE
                binding.profileSelectFishLayout.visibility = View.GONE
                binding.profileSelectLengthLayout.visibility = View.GONE
                binding.profileSelectDateLayout.visibility = View.GONE
                (activity as MainActivity).navigationGone()
            }

            "selectFish" -> {
                binding.profileMainLayout.visibility = View.GONE
                binding.profileCollectionReedMoreLayout.visibility = View.GONE
                binding.profileSelectFishLayout.visibility = View.VISIBLE
                binding.profileSelectLengthLayout.visibility = View.GONE
                binding.profileSelectDateLayout.visibility = View.GONE
                (activity as MainActivity).navigationGone()
            }

            "selectLength" -> {
                binding.profileMainLayout.visibility = View.GONE
                binding.profileCollectionReedMoreLayout.visibility = View.GONE
                binding.profileSelectFishLayout.visibility = View.GONE
                binding.profileSelectLengthLayout.visibility = View.VISIBLE
                binding.profileSelectDateLayout.visibility = View.GONE
                (activity as MainActivity).navigationGone()
            }

            "selectDate" -> {
                binding.profileMainLayout.visibility = View.GONE
                binding.profileCollectionReedMoreLayout.visibility = View.GONE
                binding.profileSelectFishLayout.visibility = View.GONE
                binding.profileSelectLengthLayout.visibility = View.GONE
                binding.profileSelectDateLayout.visibility = View.VISIBLE
                (activity as MainActivity).navigationGone()
            }

        }

    } // changeLayout()

}