package com.example.fishingmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checking_fish, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        observeLiveData()
        getData()

    } // onViewCreated()


    fun setVariable() {

        viewModel = CheckingFishViewModel()

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

}