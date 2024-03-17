package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.data.CheckingFish
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.model.CheckingFishModel
import com.example.fishingmanager.model.SplashModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckingFishViewModel(historyList : ArrayList<History>, val userInfo: UserInfo) : ViewModel() {

    val TAG = "CheckingFishViewModel"
    val model = CheckingFishModel()

    val nickname = userInfo.nickname
    var basicHistoryList = historyList
    val liveDataBasicCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataBasicHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataBasicFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataBasicUserInfo = MutableLiveData<UserInfo>()

    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataClickedFishImage = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()
    val liveDataLoadingStatus = MutableLiveData<Boolean>()
    val liveDataCameraStatus = MutableLiveData<Boolean>()
    val liveDataClassifyStatus = MutableLiveData<Boolean>()
    val liveDataClassifyCompleteStatus = MutableLiveData<Boolean>()
    val liveDataCheckingFish = MutableLiveData<CheckingFish>()
    val liveDataSaveStatus = MutableLiveData<Boolean>()
    val liveDataSaveAndWriteStatus = MutableLiveData<Boolean>()
    val liveDataChangeFragment = MutableLiveData<String>()
    val liveDataCheckingFishCountText = MutableLiveData<String>()
    val liveDataCheckingFishDayCount = MutableLiveData<Int>()

    lateinit var complete : String


    /*
    ViewModel 초기화
     */
    fun init() {

        liveDataBasicHistoryList.value = basicHistoryList
        liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)
        liveDataCheckingFishCountText.value = "금일 남은 횟수 : ${userInfo.checkingFishCount} / 3"
        liveDataCheckingFishDayCount.value = userInfo.checkingFishTicket

    } // init()


    /*
    카메라 시작
     */
    fun startCamera() {

        liveDataCameraStatus.value = liveDataCheckingFishCountText.value != "금일 남은 횟수 : 0 / 3"

    } // startCamera()


    /*
    물고기 분류
     */
    fun classify() {

        liveDataClassifyStatus.value = true

    } // classify()


    /*
    물고기 분류 완료
     */
    fun classifyComplete() {

        liveDataClassifyCompleteStatus.value = true

    } // classifyComplete()


    /*
    PhotoView Fragment로 전환
     */
    fun goPhotoView(image : String) {

        liveDataClickedFishImage.value = image

    } // goPhotoView()


    /*
    Layout 전환
     */
    fun changeLayout(layout : String) {

        liveDataChangeLayout.value = layout

    } // changeLayout()


    /*
    새로고침 버튼 클릭
     */
    fun refresh() {

        liveDataLoadingStatus.value = true

        model.requestCombine(nickname).enqueue(object : Callback<Combine> {
            override fun onResponse(call: Call<Combine>, response: Response<Combine>) {

                if (response.isSuccessful) {

                    liveDataBasicCollectionList.value = response.body()?.collection
                    liveDataBasicHistoryList.value = SplashModel().getHistoryList(response.body()?.history)
                    liveDataBasicFeedList.value = response.body()?.feed
                    liveDataBasicUserInfo.value = response.body()?.userInfo

                    basicHistoryList = liveDataBasicHistoryList.value!!
                    liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)

                    liveDataLoadingStatus.value = false

                } else {
                    Log.d(TAG, "requestCombine - onResponse : isFailure : ${response.message()}")

                    liveDataBasicCollectionList.value = ArrayList()
                    liveDataBasicHistoryList.value = ArrayList()
                    liveDataBasicFeedList.value = ArrayList()
                    liveDataBasicUserInfo.value = UserInfo("","", -1, -1, -1, "")
                    liveDataLoadingStatus.value = false
                }

            }

            override fun onFailure(call: Call<Combine>, t: Throwable) {
                Log.d(TAG, "requestCombine - onFailure : $t")

                liveDataBasicCollectionList.value = ArrayList()
                liveDataBasicHistoryList.value = ArrayList()
                liveDataBasicFeedList.value = ArrayList()
                liveDataBasicUserInfo.value = UserInfo("","", -1, -1, -1, "")
                liveDataLoadingStatus.value = false
            }

        })

    } // refresh()


    /*
    물고기 설명 불러오기
     */
    fun getDescription(fishName : String, arg : Float) {

        liveDataCheckingFish.value = model.getDescription(fishName, arg)

    } // getDescription()


    /*
    기록에 저장
     */
    fun saveHistory() {

        complete = "save"
        liveDataSaveStatus.value = true

    } // saveHistory()


    /*
    기록에 저장 후 글 작성
     */
    fun saveAndWrite() {

        complete = "write"
        liveDataSaveAndWriteStatus.value = true

    } // saveAndWrite()


    /*
    서버의 기록 테이블에 저장 요청 후 메인 화면으로 전환
     */
    fun saveHistoryServer(file : MultipartBody.Part, nickname : String, fishName : String, date : String) {

        model.requestSaveHistory(file, nickname, fishName, date).enqueue(object : Callback<Combine> {
            override fun onResponse(call: Call<Combine>, response: Response<Combine>) {

                if (response.isSuccessful) {

                    liveDataBasicCollectionList.value = response.body()?.collection
                    liveDataBasicHistoryList.value = SplashModel().getHistoryList(response.body()?.history)
                    liveDataBasicFeedList.value = response.body()?.feed
                    liveDataBasicUserInfo.value = response.body()?.userInfo

                    basicHistoryList = liveDataBasicHistoryList.value!!
                    liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)

                    liveDataCheckingFishDayCount.value = response.body()!!.userInfo.checkingFishTicket
                    if (response.body()!!.userInfo.checkingFishTicket > 0) {
                        liveDataCheckingFishCountText.value = ""
                    } else {
                        liveDataCheckingFishCountText.value = "금일 남은 횟수 : ${response.body()?.userInfo?.checkingFishCount} / 3"
                    }

                    if (complete == "save") {

                        changeLayout("main")

                    } else if (complete == "write") {

                        liveDataChangeFragment.value = "write"

                    }

                } else {
                    Log.d(TAG, "saveHistoryServer - onResponse : isFailure : ${response.message()}")
                }

            }
            override fun onFailure(call: Call<Combine>, t: Throwable) {
                Log.d(TAG, "saveHistoryServer - onFailure : $t")
            }

        })

    } // saveHistoryServer()

}