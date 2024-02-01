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
import com.example.fishingmanager.adapter.CheckingFishHistoryAdapter
import com.example.fishingmanager.databinding.FragmentCheckingFishBinding
import com.example.fishingmanager.viewModel.CheckingFishViewModel

class CheckingFishFragment : Fragment() {

    lateinit var binding : FragmentCheckingFishBinding

    lateinit var viewModel : CheckingFishViewModel
    lateinit var adapter : CheckingFishHistoryAdapter

    lateinit var userInfoShared : SharedPreferences
    lateinit var nickname : String


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

    } // setVariable()


    fun observeLiveData() {

        viewModel.liveDataHistoryList.observe(viewLifecycleOwner, Observer {

            adapter.setItem(it)

        })

        viewModel.liveDataClickedFishImage.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).goPhotoView(it)

        })

        viewModel.liveDataChangeLayout.observe(viewLifecycleOwner, Observer {

            changeLayout(it)

        })

    } // observeLiveData()


    fun getData() {

        viewModel.getHistoryList()

    } // getData()


    fun changeLayout(layout : String) {



    } // changeLayout()


    fun checkUserShared() {

        userInfoShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = userInfoShared.getString("nickname", "").toString()

    } // checkUserShared()

}