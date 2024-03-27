package com.example.fishingmanager.activity

import android.net.Uri
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
import com.example.fishingmanager.fragment.EnterFragment
import com.example.fishingmanager.fragment.FeedFragment
import com.example.fishingmanager.fragment.PayFragment
import com.example.fishingmanager.fragment.ProfileFragment
import java.io.File


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    lateinit var binding: ActivityMainBinding

    lateinit var weatherList: ArrayList<ConditionWeather>
    lateinit var tideList: ArrayList<ConditionTide>
    lateinit var indexList: ArrayList<Index.Item>
    lateinit var collectionList: ArrayList<Collection>
    lateinit var historyList: ArrayList<History>
    lateinit var feedList: ArrayList<Feed>
    lateinit var userInfo: UserInfo

    lateinit var nickname: String
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    var bundleString = ""
    lateinit var bundle : Bundle

    val enterFragment = EnterFragment()
    val splashFragment = SplashFragment()
    val conditionFragment = ConditionFragment()
    val payFragment = PayFragment()
    val photoViewFragment = PhotoViewFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setVariable()
        setListener()
        checkSharedPreference()


    } // onCreate()


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
                R.id.feed_fragment -> {

                    if (bundleString == "") {

                        changeFragment("feed")

                    } else {

                        fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentManager.setFragmentResult("feed", bundle)
                        fragmentTransaction.replace(R.id.mainFragment, FeedFragment()).commit()

                    }

                }
                R.id.profile_fragment -> changeFragment("profile")

            }

            true
        }

    } // setListener()


    // Fragment 전환
    fun changeFragment(fragmentName: String) {

        fragmentTransaction = fragmentManager.beginTransaction()

        when (fragmentName) {

            "enter" -> {
                pickOutFragment(enterFragment)
                navigationGone()
            }

            "splash" -> {
                pickOutFragment(splashFragment)
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
                pickOutFragment(conditionFragment)
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
                pickOutFragment(payFragment)
                navigationGone()
            }

            "photoView" -> {
                pickOutFragment(photoViewFragment)
                navigationGone()
            }

        }

    } // changeFragment()


    // Condition 프래그먼트로 전환할 때 데이터 함께 전달 처리
    fun changeFragmentCondition(fragmentName: String) {

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


    // Feed 프래그먼트로 전환할 때 데이터 함께 전달 처리
    fun changeFragmentFeed(feed : Feed) {

        bundle = Bundle()

        bundle.putSerializable("feed", feed)
        bundleString = "feedView"
        binding.navigation.selectedItemId = R.id.feed_fragment
        bundleString = ""

    } // changeFragmentFeed()


    // Write 프래그먼트로 전환할 때 데이터 함께 전달 처리
    fun changeFragmentWrite(file : File) {

        bundle = Bundle()

        bundle.putSerializable("write", file)

        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentManager.setFragmentResult("write", bundle)
        fragmentTransaction.replace(R.id.mainFragment, WriteFragment()).commit()

    } // changeFragmentWrite()


    fun changeFragmentFeedNavigation() {

        binding.navigation.selectedItemId = R.id.feed_fragment

    }


    // Home 프래그먼트로 이동
    fun selectHomeMenu() {

        binding.navigation.selectedItemId = R.id.home_fragment

    } // selectHomeMenu()


    // PhotoView 프래그먼트로 데이터 전달과 함께 이동
    fun goPhotoView(image : String) {

        bundle = Bundle()
        bundle.putString("image", image)
        fragmentManager.setFragmentResult("image", bundle)
        changeFragment("photoView")

    } // changeFragmentToPhotoView()


    // 바텀 내비게이션 숨김
    fun navigationGone() {

        runOnUiThread { binding.navigation.visibility = View.GONE }

    } // navigationGone()


    // 바텀 내비게이션 보임
    fun navigationVisible() {

        runOnUiThread { binding.navigation.visibility = View.VISIBLE }

    } // navigationVisible()


    // 백스택 쌓을 Fragment 구분
    fun pickOutFragment(fragmentName: Fragment) {

        if (fragmentName == photoViewFragment || fragmentName == payFragment) {

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


    // SharedPreference에 저장된 사용자 정보 확인 후 앱 진입 처리
    fun checkSharedPreference() {

        val sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE)
        val sharedNickname = sharedPreferences?.getString("nickname", "").toString()

        if (sharedNickname == "") {
            changeFragment("enter")
        } else {
            changeFragment("splash")
        }

    } // checkSharedPreference()

}