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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
    lateinit var drawerView : View

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

        viewModel.liveDataChangeTab.observe(viewLifecycleOwner, Observer {



        })

        viewModel.liveDataChangeLayout.observe(viewLifecycleOwner, Observer {



        })

        viewModel.liveDataClickedMenu.observe(viewLifecycleOwner, Observer {

            if (it) {
                Log.d("TAG", "observeLiveData: 들어옴")
                binding.profileDrawerLayout.openDrawer(R.id.drawerView)

            }

        })

    } // observeLiveData()


    fun getData() {

        viewModel.init()

    } // getData()


    fun checkUserShared() {

        userInfoShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = userInfoShared.getString("nickname", "").toString()

    } // checkUserShared()

}