package com.example.fishingmanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.fragment.CheckingFishFragment
import com.example.fishingmanager.fragment.ConditionFragment
import com.example.fishingmanager.fragment.HomeFragment
import com.example.fishingmanager.fragment.PhotoViewFragment
import com.example.fishingmanager.fragment.SplashFragment
import com.example.fishingmanager.fragment.StartFragment
import com.example.fishingmanager.fragment.WriteFragment
import com.example.fishingmanager.R
import com.example.fishingmanager.data.ConditionTide
import com.example.fishingmanager.data.ConditionWeather
import com.example.fishingmanager.databinding.ActivityMainBinding
import com.example.fishingmanager.viewModel.SplashViewModel

class MainActivity : AppCompatActivity() {

    private val TAG : String = "MainActivity"
    lateinit var binding: ActivityMainBinding

    private lateinit var weatherList : ArrayList<ConditionWeather>
    private lateinit var tideList : ArrayList<ConditionTide>
    private lateinit var indexList : ArrayList<Index.Item>
    private lateinit var collectionList : ArrayList<Collection>
    private lateinit var historyList : ArrayList<History>
    private lateinit var feedList : ArrayList<Feed>
    private lateinit var userInfo : UserInfo

    lateinit var nickname : String
    lateinit var fragmentManager : FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setVariable()
        setListener()

    }


    // 변수 초기화
    fun setVariable() {

        binding.lifecycleOwner = this
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.mainFragment, SplashFragment()).commit()

    } // setView()


    // 리스너 세팅
    fun setListener() {

        fragmentManager.addOnBackStackChangedListener {

            Log.d(TAG, "fragmentBackStackCount : " + supportFragmentManager.backStackEntryCount)

        }

        binding.navigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.home_fragment -> changeFragment("home")
                R.id.condition_fragment -> changeFragment("condition")
                R.id.checkingFish_fragment -> changeFragment("checkingFish")
                R.id.feed_fragment -> changeFragment("feed")
                R.id.profile_fragment -> changeFragment("profile")

            }

            true
        }

    } // setListener()


    // Fragment 전환
    fun changeFragment(fragmentName: String) {

        fragmentTransaction = fragmentManager.beginTransaction()

        when (fragmentName) {

            "splash" -> {
                pickOutFragment(SplashFragment())
                navigationGone()
            }

            "start" -> {
                pickOutFragment(StartFragment())
                navigationGone()
            }

            "home" -> {
                pickOutFragment(HomeFragment())
                navigationVisible()
            }

            "condition" -> {
                pickOutFragment(ConditionFragment())
                navigationVisible()
            }

            "checkingFish" -> {
                pickOutFragment(CheckingFishFragment())
                navigationVisible()
            }

            "feed" -> {
                pickOutFragment(CheckingFishFragment())
                navigationVisible()
            }

            "write" -> {
                pickOutFragment(CheckingFishFragment())
                navigationGone()
            }

            "profile" -> {
                pickOutFragment(CheckingFishFragment())
                navigationVisible()
            }

            "pay" -> {
                pickOutFragment(CheckingFishFragment())
                navigationGone()
            }

            "photoView" -> {
                pickOutFragment(CheckingFishFragment())
                navigationGone()
            }

        }

    } // changeFragment()


    fun navigationGone() {

        binding.navigation.visibility = View.GONE

    } // navigationGone()


    fun navigationVisible() {

        binding.navigation.visibility = View.VISIBLE

    } // navigationVisible()


    // 백스택 쌓을 Fragment 구분
    fun pickOutFragment(fragmentName : Fragment) {

        if (fragmentName == WriteFragment() || fragmentName == PhotoViewFragment()) {

            fragmentTransaction.replace(R.id.mainFragment, fragmentName).addToBackStack(null).commit()

        } else {

            fragmentTransaction.replace(R.id.mainFragment, fragmentName).commit()

        }

    } // pickOutFragment()


    // Fragment 백스택 삭제
    fun removeFragmentStack() {

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    } // removeFragmentStack()


    fun getWeatherList() : ArrayList<ConditionWeather> {

        return weatherList

    } // getWeatherList()


    fun getTideList() : ArrayList<ConditionTide> {

        return tideList

    } // getTideList()


    fun getIndexList() : ArrayList<Index.Item> {

        return indexList

    } // getIndexList()


    fun getCollectionList() : ArrayList<Collection> {

        return collectionList

    } // getCollectionList()


    fun getHistoryList() : ArrayList<History> {

        return historyList

    }


    fun getFeedList() : ArrayList<Feed> {

        return feedList

    } // getFeedList()


    fun getUserInfo() : UserInfo {

        return userInfo

    } // getUserInfo()


    fun setWeatherList(weatherList : ArrayList<ConditionWeather>) {

        this.weatherList = weatherList

    } // setWeatherList()


    fun setTideList(tideList : ArrayList<ConditionTide>) {

        this.tideList = tideList

    } // setTideList()


    fun setIndexList(indexList : ArrayList<Index.Item>) {

        this.indexList = indexList

    } // setIndexList()


}