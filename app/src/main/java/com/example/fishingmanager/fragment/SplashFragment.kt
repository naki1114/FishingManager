package com.example.fishingmanager.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.R
import com.example.fishingmanager.viewModel.SplashViewModel
import com.skydoves.progressview.ProgressView
import kotlin.concurrent.thread


class SplashFragment : Fragment() {

    val TAG: String = "SplashFragment"

    private lateinit var viewModel : SplashViewModel
    private lateinit var progressText: TextView
    private lateinit var progressView: ProgressView
    private var checkSharedPreferense : Boolean = false
    private var progressValue: Float = 0.0f
    private lateinit var userNickname: String
    private lateinit var obsCode: String
    private lateinit var lat : String
    private lateinit var lon : String

    val delayToStart = thread(false) {
        Thread.sleep(1000)
        (activity as MainActivity).changeFragment("start")
    }
    val delayToHome = thread(false) {
        Thread.sleep(1000)
        (activity as MainActivity).changeFragment("home")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        observeLiveData()
        requestData()

    } // onViewCreated


    // 변수 초기화
    fun setVariable() {

        viewModel = SplashViewModel()
        progressText = activity?.findViewById(R.id.progressText)!!
        progressView = activity?.findViewById(R.id.progressView)!!
        checkSharedPreferense = checkLoginSharedPreference()
        getLocationSharedPreference()

    } // setVariable()


    // API 데이터 요청
    fun requestData() {

        // 각 메서드 인수에 들어갈 데이터 수정 예정 - SharedPreference에서 얻어와야 함.
        viewModel.getWeather("1", "1000", "JSON", GetDate().getFormatDate4(GetDate().getTime()), "2300", lat, lon)
        viewModel.getTide(GetDate().getFormatDate2(GetDate().getTime()), obsCode, "json")
        viewModel.getIndex("SF", "json")

        if (userNickname != "") {
            viewModel.getCombine(userNickname)
        }

    } // requestData()


    // ViewModel - LiveData 관찰
    private fun observeLiveData() {

        // 날씨 - ArrayList 감지
        viewModel.liveDataWeatherList.observe(viewLifecycleOwner, Observer {

            updateProgressView()
            (activity as MainActivity).setWeatherList(it)

        })

        // 조석 - ArrayList 감지
        viewModel.liveDataTideList.observe(viewLifecycleOwner, Observer {

            updateProgressView()
            (activity as MainActivity).setTideList(it)

        })

        // 지수 - ArrayList 감지
        viewModel.liveDataIndexList.observe(viewLifecycleOwner, Observer {

            updateProgressView()
            (activity as MainActivity).setIndexList(it)

        })

        // DB 데이터 - Boolean 감지
        viewModel.liveDataCombineData.observe(viewLifecycleOwner, Observer {

            if (it) {

                updateProgressView()

            }

        })

    } // observeLiveData()


    // ProgressView 업데이트
    fun updateProgressView() {

        if (checkSharedPreferense) {

            progressValue += 33.3f

            if (progressValue.toInt() == 99) {

                progressValue = 100.0f

            }

        } else {

            progressValue += 25.0f

        }

        progressView.labelText = "${progressValue.toInt()} %"
        progressView.progress = progressValue

        when (progressValue.toInt()) {

            0 -> progressText.text = resources.getString(R.string.splash_loading_0)
            25 -> progressText.text = resources.getString(R.string.splash_loading_25)
            50 -> progressText.text = resources.getString(R.string.splash_loading_50)
            75 -> progressText.text = resources.getString(R.string.splash_loading_75)
            33 -> progressText.text = resources.getString(R.string.splash_loading_33)
            66 -> progressText.text = resources.getString(R.string.splash_loading_66)
            99 , 100 -> {
                progressText.text = resources.getString(R.string.splash_loading_100)

                if (checkSharedPreferense) {

                    delayToStart.start()

                } else {

                    delayToHome.start()

                }

            }

        }

    } // updateProgressView()


    // SharedPreference 체크
    fun checkLoginSharedPreference() : Boolean {

        val sharedPreferences = activity?.getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        userNickname = sharedPreferences?.getString("nickname", "").toString()

        return userNickname == ""

    } // checkLoginSharedPreference()


    fun getLocationSharedPreference() {

        val sharedPreferences = activity?.getSharedPreferences("location", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        obsCode = sharedPreferences?.getString("obsCode", "").toString()
        lat = sharedPreferences?.getString("lat", "").toString()
        lon = sharedPreferences?.getString("lon", "").toString()

        if (obsCode == "") {

            editor.putString("location", "영흥도")
            editor.putString("obsCode", "DT_0043")
            editor.putString("lat", "37")
            editor.putString("lon", "126")
            editor.commit()

            obsCode = sharedPreferences?.getString("obsCode", "").toString()
            lat = sharedPreferences.getString("lat", "").toString()
            lon = sharedPreferences.getString("lon", "").toString()

        }

    } // getLocationSharedPreference()


}