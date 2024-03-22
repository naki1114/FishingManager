package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.SelectFish
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.model.ProfileModel
import com.example.fishingmanager.model.SplashModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(collectionList: ArrayList<Collection>, historyList: ArrayList<History>,
                       var userInfo: UserInfo, val nickname: String
) : ViewModel() {

    val TAG = "ProfileViewModel"

    val model = ProfileModel()
    val liveDataUserInfo = MutableLiveData<UserInfo>()
    var basicCollectionList = collectionList
    var basicHistoryList = historyList
    var userBasicHistoryList = ArrayList<History>()
    var liveDataCalendarList = MutableLiveData<ArrayList<CalendarDay>>()

    val liveDataBasicCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataBasicHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataBasicFeedList = MutableLiveData<ArrayList<Feed>>()
    val liveDataBasicUserInfo = MutableLiveData<UserInfo>()
    val liveDataCollectionList = MutableLiveData<ArrayList<Collection>>()
    val liveDataHistoryList = MutableLiveData<ArrayList<History>>()
    val liveDataFishList = MutableLiveData<ArrayList<SelectFish>>()
    val liveDataReadMoreFish = MutableLiveData<Collection>()

    val liveDataChangeTab = MutableLiveData<String>()
    val liveDataChangeLayout = MutableLiveData<String>()
    val liveDataClickedMenu = MutableLiveData<Boolean>()
    val liveDataChangeFragment = MutableLiveData<String>()

    val liveDataCurrentFish = MutableLiveData<String>()
    val liveDataCurrentDate = MutableLiveData<String>()
    val liveDataShowDialog = MutableLiveData<String>()
    val liveDataLogoutStatus = MutableLiveData<Boolean>()
    val liveDataDeleteAccountStatus = MutableLiveData<Boolean>()

    val liveDataClickedFishImage = MutableLiveData<UserInfo>()
    val liveDataGoToGallery = MutableLiveData<Boolean>()

    var previousLayout: String = ""

    val liveDataLoadingStatus = MutableLiveData<Boolean>()
    val liveDataUpdateProfileImage = MutableLiveData<Combine>()
    val liveDataTicketDateCount = MutableLiveData<String>()

    var collectionCount = 0


    // ViewModel 초기화
    fun init() {

        liveDataUserInfo.value = userInfo
        liveDataBasicHistoryList.value = basicHistoryList
        liveDataBasicCollectionList.value = basicCollectionList
        userBasicHistoryList = model.getHistoryList(basicHistoryList, nickname)
        liveDataCalendarList.value = model.getCalendarList(userBasicHistoryList)
        liveDataCollectionList.value = model.getCollectionList(basicCollectionList, nickname)
        liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)
        liveDataFishList.value = model.getFishList(basicHistoryList, nickname)
        liveDataCurrentFish.value = "전 체"
        liveDataCurrentDate.value = "전 체"
        liveDataTicketDateCount.value = "이용권 남은 기간 : ${userInfo.checkingFishTicket}일"

        for (i in 0 until 30) {

            if (liveDataCollectionList.value!![i].fishName != "") {
                collectionCount++
            }

        }

    } // init()


    // 물고기 자세히 보기
    fun readMoreFish(collection: Collection) {

        if (collection.fishName != "") {
            liveDataReadMoreFish.value = collection
            changeLayout("readMore")
        }

    } // selectedFish()


    // 화면 전환
    fun changeTab(tab: String) {

        liveDataChangeTab.value = tab

    } // changeTab()


    // 레이아웃 전환
    fun changeLayout(layout: String) {

        if (previousLayout != "") {
            previousLayout = liveDataChangeLayout.value!!
        } else {
            previousLayout = "main"
        }
        liveDataChangeLayout.value = layout

    } // changeLayout()


    // 이전 화면으로 돌아가기
    fun backToMainLayout() {

        liveDataChangeLayout.value = previousLayout

    } // backToMainLayout()


    // 햄버거 버튼 클릭 시
    fun clickedMenu() {

        liveDataClickedMenu.value = true

    } // clickedMenu()


    // 프래그먼트 전환
    fun changeFragment(fragment : String) {

        liveDataChangeFragment.value = fragment

    } // changeFragment()


    // 물고기 필터링
    fun changeFish(fishName: String) {

        liveDataCurrentFish.value = fishName
        liveDataHistoryList.value = model.refreshHistoryList(basicHistoryList, nickname, fishName, liveDataCurrentDate.value!!)
        liveDataChangeLayout.value = previousLayout

    } // changeFish()


    // 날짜 필터링
    fun changeDate(date: String) {

        if (date != "전 체") {

            val formatDate = date.substring(2,4) + "." + date.substring(5,7) + "." + date.substring(8,10)
            liveDataCurrentDate.value = formatDate
            liveDataHistoryList.value = model.refreshHistoryList(basicHistoryList, nickname, liveDataCurrentFish.value!!, formatDate)

        } else {

            liveDataCurrentDate.value = date
            liveDataHistoryList.value = model.refreshHistoryList(basicHistoryList, nickname, liveDataCurrentFish.value!!, date)

        }

        liveDataChangeLayout.value = previousLayout

    } // changeDate()


    // 다이얼로그 띄우기
    fun showDialog(dialogName : String) {

        liveDataShowDialog.value = dialogName

    } // showDialog()


    // 로그아웃 처리
    fun changeLogoutStatus(status : Boolean) {

        liveDataLogoutStatus.value = status

    } // changeLogoutStatus()


    // 회원탈퇴 처리
    fun changeDeleteAccountStatus(status : Boolean) {

        liveDataDeleteAccountStatus.value = status

    } // changeDeleteAccountStatus()


    // 회원 탈퇴 요청 및 응답
    fun deleteAccount(nickname: String, type: String) {

        model.requestDeleteAccount(nickname, type).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful && response.body() == "successDelete") {

                    changeFragment("start")

                } else {
                    Log.d(TAG, "deleteAccount - onResponse : isFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "deleteAccount - onFailure : $t")
            }

        })

    } // deleteAccount()


    // 포토뷰 프래그먼트로 이동
    fun goPhotoView(userInfo: UserInfo) {

        liveDataClickedFishImage.value = userInfo

    } // goPhotoView()


    // 갤러리로 이동
    fun goToGallery() {

        liveDataGoToGallery.value = true

    } // goToGallery()


    // 새로고침 후 웹 서버에 데이터 다시 요청 및 응답
    fun refresh() {

        liveDataLoadingStatus.value = true

        model.requestCombine(nickname).enqueue(object : Callback<Combine> {
            override fun onResponse(call: Call<Combine>, response: Response<Combine>) {

                if (response.isSuccessful) {

                    liveDataBasicCollectionList.value = response.body()?.collection
                    liveDataBasicHistoryList.value = SplashModel().getHistoryList(response.body()?.history)
                    liveDataBasicFeedList.value = response.body()?.feed
                    liveDataBasicUserInfo.value = response.body()?.userInfo

                    basicCollectionList = liveDataBasicCollectionList.value!!
                    basicHistoryList = liveDataBasicHistoryList.value!!
                    userBasicHistoryList = model.getHistoryList(basicHistoryList, nickname)

                    liveDataCalendarList.value = model.getCalendarList(userBasicHistoryList)
                    liveDataCollectionList.value = model.getCollectionList(basicCollectionList, nickname)
                    liveDataHistoryList.value = model.getHistoryList(basicHistoryList, nickname)
                    liveDataFishList.value = model.getFishList(basicHistoryList, nickname)

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


    // 프로필 사진 수정 요청 및 응답
    fun updateProfileImage(file : MultipartBody.Part, nickname : String) {

        model.requestUpdateProfileImage(file, nickname).enqueue(object : Callback<Combine> {
            override fun onResponse(call: Call<Combine>, response: Response<Combine>) {

                if (response.isSuccessful) {

                    liveDataUpdateProfileImage.value = response.body()

                } else {
                    Log.d(TAG, "updateProfileImage - onResponse isFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Combine>, t: Throwable) {
                Log.d(TAG, "updateProfileImage - onFailure : $t")
            }

        })

    } // updateProfileImage()

}