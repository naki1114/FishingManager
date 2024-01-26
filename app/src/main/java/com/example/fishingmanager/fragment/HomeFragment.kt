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
import com.example.fishingmanager.databinding.FragmentHomeBinding
import com.example.fishingmanager.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    lateinit var locationShared: SharedPreferences
    lateinit var location: String

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
        viewModel.getWeather()

    } // onViewCreated()


    fun setVariable() {

        viewModel = HomeViewModel((activity as MainActivity).getWeatherList(), location)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    } // setVariable()


    fun setView() {



    } // setView()


    fun observeLiveData() {

        viewModel.liveDataWeather.observe(viewLifecycleOwner, Observer {

            binding.homeWeatherSkyImage.setImageResource(it.skyImage)

        })

    } // observeLiveData()


    fun checkSharedPreference() {

        locationShared = requireActivity().getSharedPreferences("location", AppCompatActivity.MODE_PRIVATE)

        location = locationShared.getString("location", "").toString()

        if (location == "") {
            location = "영흥도"
        }

    }


}