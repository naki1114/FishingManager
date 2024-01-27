package com.example.fishingmanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
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
import com.example.fishingmanager.fragment.FeedFragment
import com.example.fishingmanager.fragment.PayFragment
import com.example.fishingmanager.fragment.ProfileFragment


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    lateinit var binding: ActivityMainBinding

    private lateinit var weatherList: ArrayList<ConditionWeather>
    private lateinit var tideList: ArrayList<ConditionTide>
    private lateinit var indexList: ArrayList<Index.Item>
    private lateinit var collectionList: ArrayList<Collection>
    private lateinit var historyList: ArrayList<History>
    private lateinit var feedList: ArrayList<Feed>
    private lateinit var userInfo: UserInfo

    lateinit var nickname: String
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    var bundleString = ""
    lateinit var bundle : Bundle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setVariable()
        setListener()

    } // onCreate()


    override fun onStart() {
        super.onStart()

        // 현재 프래그먼트 확인해서 바텀 네비게이션 visible 처리

    } // onStart()


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
                R.id.condition_fragment -> {

                    if (bundleString == "") {

                        changeFragment("condition")

                    } else if (bundleString == "weather") {

                        fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentManager.setFragmentResult("layout", bundle)
                        fragmentTransaction.replace(R.id.mainFragment, ConditionFragment()).commit()

                    } else if (bundleString == "index") {

                        fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentManager.setFragmentResult("layout", bundle)
                        fragmentTransaction.replace(R.id.mainFragment, ConditionFragment()).commit()

                    }

                }

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
                pickOutFragment(FeedFragment())
                navigationVisible()
            }

            "write" -> {
                pickOutFragment(WriteFragment())
                navigationGone()
            }

            "profile" -> {
                pickOutFragment(ProfileFragment())
                navigationVisible()
            }

            "pay" -> {
                pickOutFragment(PayFragment())
                navigationGone()
            }

            "photoView" -> {
                pickOutFragment(PhotoViewFragment())
                navigationGone()
            }

        }

    } // changeFragment()


    fun changeFragmentWithData(fragmentName: String) {

        bundle = Bundle()

        when (fragmentName) {

            "conditionWeather" -> {
                bundleString = "weather"
                bundle.putString("layout", bundleString)
                binding.navigation.selectedItemId = R.id.condition_fragment
                bundleString = ""
            }

            "conditionIndex" -> {
                bundleString = "index"
                bundle.putString("layout", bundleString)
                binding.navigation.selectedItemId = R.id.condition_fragment
                bundleString = ""
            }

        }


    } // changeFragmentWithData()


    fun goPhotoView(image : String) {

        bundle = Bundle()
        bundle.putString("image", image)
        fragmentManager.setFragmentResult("image", bundle)
        changeFragment("photoView")

    } // changeFragmentToPhotoView()


    fun navigationGone() {

        binding.navigation.visibility = View.GONE

    } // navigationGone()


    fun navigationVisible() {

        binding.navigation.visibility = View.VISIBLE

    } // navigationVisible()


    // 백스택 쌓을 Fragment 구분
    fun pickOutFragment(fragmentName: Fragment) {

        if (fragmentName == WriteFragment() || fragmentName == PhotoViewFragment()) {

            fragmentTransaction.replace(R.id.mainFragment, fragmentName).addToBackStack(null)
                .commit()

        } else {

            fragmentTransaction.replace(R.id.mainFragment, fragmentName).commit()

        }

    } // pickOutFragment()


    // Fragment 백스택 삭제
    fun removeFragmentStack() {

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    } // removeFragmentStack()


    fun getWeatherList(): ArrayList<ConditionWeather> {

        return weatherList

    } // getWeatherList()


    fun getTideList(): ArrayList<ConditionTide> {

        return tideList

    } // getTideList()


    fun getIndexList(): ArrayList<Index.Item> {

        return indexList

    } // getIndexList()


    fun getCollectionList(): ArrayList<Collection> {

        return collectionList

    } // getCollectionList()


    fun getHistoryList(): ArrayList<History> {

        return historyList

    }


    fun getFeedList(): ArrayList<Feed> {

        return feedList

    } // getFeedList()


    fun getUserInfo(): UserInfo {

        return userInfo

    } // getUserInfo()


    fun setWeatherList(weatherList: ArrayList<ConditionWeather>) {

        this.weatherList = weatherList

    } // setWeatherList()


    fun setTideList(tideList: ArrayList<ConditionTide>) {

        this.tideList = tideList

    } // setTideList()


    fun setIndexList(indexList: ArrayList<Index.Item>) {

        this.indexList = indexList

    } // setIndexList()


}