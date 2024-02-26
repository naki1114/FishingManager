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

class WriteViewModel(val nickname : String) : ViewModel() {

    val TAG = "WriteViewModel"

    val doBackLayout = MutableLiveData<Boolean>()
    val doSaveLayout = MutableLiveData<Boolean>()

    val isBack = MutableLiveData<Boolean>()
    val isSave = MutableLiveData<Boolean>()

    val feedStatus = MutableLiveData<String>()
    val titleLiveData = MutableLiveData<String>()
    val contentLiveData = MutableLiveData<String>()

    val toGalleryLiveData = MutableLiveData<Boolean>()

    private var model = WriteModel()

    fun writeBackLayout() {

        doBackLayout.value = true

    } // writeBackLayout

    fun clickBackCancel() {

        doBackLayout.value = false

    } // clickBackCancel

    fun clickBackCheck() {

        doBackLayout.value = false
        isBack.value = true

    } // clickBackCheck

    fun writeSaveLayout(title : String, content : String) {

        if (title.isEmpty()) {
            feedStatus.value = "titleEmpty"
        }
        else if (title.length > 20) {
            feedStatus.value = "titleOver"
        }
        else if (content.isEmpty()) {
            feedStatus.value = "contentEmpty"
        }
        else if (content.length > 1000) {
            feedStatus.value = "contentEmpty"
        }
        else {
            feedStatus.value = "notEmpty"
            titleLiveData.value = title
            contentLiveData.value = content
            doSaveLayout.value = true
        }

    } // writeSaveLayout

    fun clickSaveCancel() {

        doSaveLayout.value = false

    } // clickSaveCancel

    fun clickSaveCheck() {

        doSaveLayout.value = false
        isSave.value = true

    } // clickSaveCheck

    fun titleChanged(title : String) {

        titleLiveData.value = title

    }  // titleChanged

    fun contentChanged(content : String) {

        contentLiveData.value = content

    }  // contentChanged

    fun insertFeed() {

        val date = System.currentTimeMillis().toString()
        val title = titleLiveData.value
        val content = contentLiveData.value

        model.insertFeed(nickname, title!!, content!!, date).enqueue(object : Callback<ArrayList<Feed>> {

            override fun onResponse(call: Call<ArrayList<Feed>>, response: Response<ArrayList<Feed>>) {
                if (response.isSuccessful) {
                    var msg = response.body()
                    isSave.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {
                Log.d(TAG, "onFailure : ${t.message}")
            }

        })

    } // insertFeed

    fun insertImageFeed(body : MultipartBody.Part) {

        val date = System.currentTimeMillis().toString()
        val title = titleLiveData.value
        val content = contentLiveData.value

        model.insertImageFeed(body, nickname, title!!, content!!, date).enqueue(object : Callback<ArrayList<Feed>> {

            override fun onResponse(call: Call<ArrayList<Feed>>, response: Response<ArrayList<Feed>>) {
                if (response.isSuccessful) {
                    var msg = response.body()
                    isSave.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {
                Log.d(TAG, "onFailure : ${t.message}")
            }

        })

    } // insertImageFeed

    fun toGallery() {

        toGalleryLiveData.value = true

    } // toGallery

}