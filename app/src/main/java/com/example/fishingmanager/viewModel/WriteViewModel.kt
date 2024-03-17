package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.model.WriteModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteViewModel(val nickname: String): ViewModel() {

    private val TAG = "WriteViewModel"

    val doBackLayout = MutableLiveData<Boolean>()
    val doSaveLayout = MutableLiveData<Boolean>()

    val isBack = MutableLiveData<Boolean>()
    val isRequestSave = MutableLiveData<Boolean>()

    val feedStatus = MutableLiveData<String>()
    val titleLiveData = MutableLiveData<String>()
    val contentLiveData = MutableLiveData<String>()

    val toGalleryLiveData = MutableLiveData<Boolean>()
    val isSaved = MutableLiveData<Boolean>()

    private var model = WriteModel()


    // 뒤로 가기 버튼 클릭 시, 다이얼로그 띄워줄지 확인
    fun writeBackLayout() {

        doBackLayout.value = true

    } // writeBackLayout()


    // 뒤로 가기 취소
    fun clickBackCancel() {

        doBackLayout.value = false

    } // clickBackCancel()


    // 뒤로 가기 선택
    fun clickBackCheck() {

        doBackLayout.value = false
        isBack.value = true

    } // clickBackCheck()


    // 완료 버튼 클릭 시, 다이얼로그 띄워줄지 확인
    fun writeSaveLayout(title: String, content: String) {

        if (title.isEmpty()) {

            feedStatus.value = "titleEmpty"

        } else if (title.length > 20) {

            feedStatus.value = "titleOver"

        } else if (content.isEmpty()) {

            feedStatus.value = "contentEmpty"

        } else if (content.length > 1000) {

            feedStatus.value = "contentOver"

        } else {

            feedStatus.value = "notEmpty"
            titleLiveData.value = title
            contentLiveData.value = content
            doSaveLayout.value = true

        }

    } // writeSaveLayout()


    // 게시글 저장 취소
    fun clickSaveCancel() {

        doSaveLayout.value = false

    } // clickSaveCancel()


    // 게시글 저장 확인
    fun clickSaveCheck() {

        doSaveLayout.value = false
        isRequestSave.value = true

    } // clickSaveCheck()


    // 게시글 제목 변화 적용
    fun titleChanged(title: String) {

        titleLiveData.value = title

    }  // titleChanged()


    // 게시글 내용 변화 적용
    fun contentChanged(content: String) {

        contentLiveData.value = content

    }  // contentChanged()


    // Retrofit) 게시글 저장 (이미지 없을 경우)
    fun insertFeed() {

        val date = System.currentTimeMillis().toString()
        val title = titleLiveData.value
        val content = contentLiveData.value

        model.insertFeed(nickname, title!!, content!!, date).enqueue(object: Callback<ArrayList<Feed>> {

            override fun onResponse(call: Call<ArrayList<Feed>>, response: Response<ArrayList<Feed>>) {

                if (response.isSuccessful) {

                    isRequestSave.value = false
                    isSaved.value = true

                }

            }

            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {

                Log.d(TAG, "onFailure : ${t.message}")

            }

        })

    } // insertFeed()


    // Retrofit) 게시글 저장 (이미지 있을 경우)
    fun insertImageFeed(body: MultipartBody.Part) {

        val date = System.currentTimeMillis().toString()
        val title = titleLiveData.value
        val content = contentLiveData.value

        model.insertImageFeed(body, nickname, title!!, content!!, date).enqueue(object: Callback<ArrayList<Feed>> {

            override fun onResponse(call: Call<ArrayList<Feed>>, response: Response<ArrayList<Feed>>) {

                if (response.isSuccessful) {

                    isRequestSave.value = false
                    isSaved.value = true

                }

            }

            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {

                Log.d(TAG, "onFailure : ${t.message}")

            }

        })

    } // insertImageFeed()


    // 내장 갤러리로 이동할지 확인
    fun toGallery() {

        toGalleryLiveData.value = true

    } // toGallery()


}