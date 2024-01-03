package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val TAG = "MainViewModel"
    val liveDataCurrentFragment = MutableLiveData<String>()


    // Fragment 전환
    fun changeFragment(fragmentName : String) {

        liveDataCurrentFragment.value = fragmentName

    } // changeFragment()

}