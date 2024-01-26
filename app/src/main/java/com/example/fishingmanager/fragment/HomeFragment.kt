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
import com.example.fishingmanager.adapter.HomeRecommendAdapter
import com.example.fishingmanager.databinding.FragmentHomeBinding
import com.example.fishingmanager.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    lateinit var locationShared: SharedPreferences
    lateinit var location: String
    lateinit var recommendAdapter: HomeRecommendAdapter

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
        setView()
        observeLiveData()
        getData()

    } // onViewCreated()


    fun setVariable() {

        viewModel = HomeViewModel((activity as MainActivity).getWeatherList(), location, (activity as MainActivity).getIndexList())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        recommendAdapter = HomeRecommendAdapter()
        binding.homeRecommendRecyclerView.adapter = recommendAdapter

    } // setVariable()


    fun setView() {



    } // setView()


    fun observeLiveData() {

        viewModel.liveDataWeather.observe(viewLifecycleOwner, Observer {

            binding.homeWeatherSkyImage.setImageResource(it.skyImage)

        })

        viewModel.liveDataRecommendList.observe(viewLifecycleOwner, Observer {

            recommendAdapter.setItem(it)

        })

    } // observeLiveData()


    fun getData() {

        viewModel.getWeather()
        viewModel.getRecommendList()

    } // getData()


    fun checkSharedPreference() {

        locationShared = requireActivity().getSharedPreferences("location", AppCompatActivity.MODE_PRIVATE)

        location = locationShared.getString("location", "").toString()

        if (location == "") {
            location = "영흥도"
        }

    }


}