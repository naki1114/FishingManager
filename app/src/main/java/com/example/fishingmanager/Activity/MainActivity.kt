package com.example.fishingmanager.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.fishingmanager.Data.Collection
import com.example.fishingmanager.Data.Feed
import com.example.fishingmanager.Data.History
import com.example.fishingmanager.Data.Index
import com.example.fishingmanager.Data.Tide
import com.example.fishingmanager.Data.UserInfo
import com.example.fishingmanager.Data.Weather
import com.example.fishingmanager.Fragment.CheckingFishFragment
import com.example.fishingmanager.Fragment.ConditionFragment
import com.example.fishingmanager.Fragment.FeedFragment
import com.example.fishingmanager.Fragment.HomeFragment
import com.example.fishingmanager.Fragment.PayFragment
import com.example.fishingmanager.Fragment.PhotoViewFragment
import com.example.fishingmanager.Fragment.ProfileFragment
import com.example.fishingmanager.Fragment.SplashFragment
import com.example.fishingmanager.Fragment.StartFragment
import com.example.fishingmanager.Fragment.WriteFragment
import com.example.fishingmanager.R
import com.example.fishingmanager.ViewModel.MainViewModel
import com.example.fishingmanager.ViewModel.SplashViewModel

class MainActivity : AppCompatActivity() {

    private val TAG : String = "MainActivity"

    private lateinit var mainViewModel : MainViewModel
    private lateinit var splashViewModel: SplashViewModel

    private lateinit var weatherList : ArrayList<Weather.Item>
    private lateinit var tideList : ArrayList<Tide.Item>
    private lateinit var indexList : ArrayList<Index.Item>
    private lateinit var collectionList : ArrayList<Collection>
    private lateinit var historyList : ArrayList<History>
    private lateinit var feedList : ArrayList<Feed>
    private lateinit var userInfo : UserInfo

    lateinit var fragmentManager : FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setVariable()
        setListener()
        observeLiveDate()

    }


    // 변수 초기화
    fun setVariable() {

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        mainViewModel = MainViewModel()
        splashViewModel = SplashViewModel()
        fragmentTransaction.add(R.id.mainFragment, SplashFragment()).commit()

    } // setView()


    // 리스너 세팅
    fun setListener() {

        fragmentManager.addOnBackStackChangedListener {

            Log.d(TAG, "fragmentBackStackCount : " + supportFragmentManager.backStackEntryCount)

        }

    } // setListener()


    // ViewModel - LiveData 관찰
    fun observeLiveDate() {

        // 전환할 Fragment 이름 - String 감지
        mainViewModel.liveDataCurrentFragment.observe(this, Observer { s ->

            changeFragment(s)

        })

    } // observeLiveData()


    // Fragment 전환
    fun changeFragment(fragmentName : String) {

        fragmentTransaction = fragmentManager.beginTransaction()

        when (fragmentName) {

            "splash" -> pickOutFragment(SplashFragment())
            "start" -> pickOutFragment(StartFragment())
            "home" -> pickOutFragment(HomeFragment())
            "condition" -> pickOutFragment(ConditionFragment())
            "checkingFish" -> pickOutFragment(CheckingFishFragment())
            "feed" -> pickOutFragment(FeedFragment())
            "write" -> pickOutFragment(WriteFragment())
            "profile" -> pickOutFragment(ProfileFragment())
            "pay" -> pickOutFragment(PayFragment())
            "photoView" -> pickOutFragment(PhotoViewFragment())

        }

    } // changeFragment()


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


    fun getWeatherList() : ArrayList<Weather.Item> {

        return weatherList

    } // getWeatherList()


    fun getTideList() : ArrayList<Tide.Item> {

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


    fun setWeatherList(weatherList : ArrayList<Weather.Item>) {

        this.weatherList = weatherList

    } // setWeatherList()


    fun setTideList(tideList : ArrayList<Tide.Item>) {

        this.tideList = tideList

    } // setTideList()


    fun setIndexList(indexList : ArrayList<Index.Item>) {

        this.indexList = indexList

    } // setIndexList()


}