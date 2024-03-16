package com.example.fishingmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.R
import com.example.fishingmanager.databinding.FragmentSplashBinding
import com.example.fishingmanager.viewModel.SplashViewModel
import kotlin.concurrent.thread


class SplashFragment: Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel

    private var progressValue: Float = 0.0f
    private var realLoadingValue: Int = 0
    private lateinit var nickname: String
    private lateinit var obsCode: String
    private lateinit var lat: String
    private lateinit var lon: String
    private var checkDataStatusNum: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setVariable()
        observeLiveData()
        requestData()

    } // onViewCreated()


    // 변수 초기화
    fun setVariable() {

        viewModel = SplashViewModel()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        getUserInfoSharedPreference()
        getLocationSharedPreference()

        progressValue = 0.0f
        realLoadingValue = 0

    } // setVariable()


    // API 데이터 요청
    private fun requestData() {

        // 각 메서드 인수에 들어갈 데이터 수정 예정 - SharedPreference에서 얻어와야 함.
        viewModel.getWeather("1", "1000", "JSON", GetDate().getFormatDate4(GetDate().getTime()), "2300", lat, lon)
        viewModel.getTide(GetDate().getFormatDate2(GetDate().getTime()), obsCode, "json")
        viewModel.getIndex("SF", "json")
        viewModel.getCombine(nickname)

    } // requestData()


    // ViewModel - LiveData 관찰
    private fun observeLiveData() {

        // 날씨 - ArrayList 감지
        viewModel.liveDataWeatherList.observe(viewLifecycleOwner) {

            (activity as MainActivity).weatherList = it

            if (it.size != 0) {
                progressValue += 25.0f
                updateProgressView()
            }

        }

        // 조석 - ArrayList 감지
        viewModel.liveDataTideList.observe(viewLifecycleOwner) {

            (activity as MainActivity).tideList = it

            if (it.size != 0) {
                progressValue += 25.0f
                updateProgressView()
            }

        }

        // 지수 - ArrayList 감지
        viewModel.liveDataIndexList.observe(viewLifecycleOwner) {

            (activity as MainActivity).indexList = it

            if (it.size != 0) {
                progressValue += 25.0f
                updateProgressView()
            }

        }

        viewModel.liveDataCombineData.observe(viewLifecycleOwner) {

            progressValue += 25.0f
            updateProgressView()

        }

        viewModel.liveDataCollectionList.observe(viewLifecycleOwner) {

            (activity as MainActivity).collectionList = it

        }

        viewModel.liveDataHistoryList.observe(viewLifecycleOwner) {

            (activity as MainActivity).historyList = it

        }

        viewModel.liveDataFeedList.observe(viewLifecycleOwner) {

            (activity as MainActivity).feedList = it

        }

        viewModel.liveDataUserInfo.observe(viewLifecycleOwner) {

            (activity as MainActivity).userInfo = it

        }

        viewModel.liveDataFailureWeather.observe(viewLifecycleOwner) {

            checkDataStatus()
            updateProgressView()

        }

        viewModel.liveDataFailureTide.observe(viewLifecycleOwner) {

            checkDataStatus()
            updateProgressView()

        }

        viewModel.liveDataFailureIndex.observe(viewLifecycleOwner) {

            checkDataStatus()
            updateProgressView()

        }

        viewModel.liveDataFailureCombine.observe(viewLifecycleOwner) {

            checkDataStatus()
            updateProgressView()

        }

        viewModel.liveDataFinishStatus.observe(viewLifecycleOwner) {

            (activity as MainActivity).finish()

        }

    } // observeLiveData()


    // ProgressView 업데이트
    private fun updateProgressView() {

        realLoadingValue += 25

        binding.progressView.labelText = "${progressValue.toInt()} %"
        binding.progressView.progress = progressValue

        when (progressValue.toInt()) {

            0 -> binding.progressText.text = resources.getString(R.string.splash_loading_0)
            25 -> binding.progressText.text = resources.getString(R.string.splash_loading_25)
            50 -> binding.progressText.text = resources.getString(R.string.splash_loading_50)
            75 -> binding.progressText.text = resources.getString(R.string.splash_loading_75)
            100 -> binding.progressText.text = resources.getString(R.string.splash_loading_100)

        }

        if (realLoadingValue == 100 && checkDataStatusNum != 4) {

            thread {

                Thread.sleep(1000)
                (activity as MainActivity).selectHomeMenu()

            }

        }

    } // updateProgressView()


    // 데이터 상태 확인
    private fun checkDataStatus() {

        checkDataStatusNum++

        if (checkDataStatusNum == 4) {

            binding.loadingFailLayout.visibility = View.VISIBLE

        }

    } // checkDataStatus()


    // SharedPreferences에서 사용자 정보 가져오기
    private fun getUserInfoSharedPreference() {

        val sharedPreferences = activity?.getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = sharedPreferences?.getString("nickname", "").toString()

        (activity as MainActivity).nickname = nickname

    } // getUserInfoSharedPreference()


    // SharedPreferences에서 사용자가 마지막으로 검색한 지역 가져오기
    private fun getLocationSharedPreference() {

        val sharedPreferences = activity?.getSharedPreferences("location", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        obsCode = sharedPreferences.getString("obsCode", "").toString()
        lat = sharedPreferences.getString("lat", "").toString()
        lon = sharedPreferences.getString("lon", "").toString()

        if (obsCode == "") {

            editor.putString("location", "영흥도")
            editor.putString("obsCode", "DT_0043")
            editor.putString("lat", "37")
            editor.putString("lon", "126")
            editor.apply()

            obsCode = sharedPreferences.getString("obsCode", "").toString()
            lat = sharedPreferences.getString("lat", "").toString()
            lon = sharedPreferences.getString("lon", "").toString()

        }

    } // getLocationSharedPreference()


}