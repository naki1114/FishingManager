package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.model.WriteModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteViewModel : ViewModel() {

    val TAG = "WriteViewModel"

    val isBackLayoutLiveData = MutableLiveData<String>()
    val isCheck = MutableLiveData<Boolean>()

    val titleStatus = MutableLiveData<String>()
    val contentStatus = MutableLiveData<String>()

    val toGalleryLiveData = MutableLiveData<Boolean>()

    private var model = WriteModel()

    lateinit var title : String
    lateinit var content : String
    lateinit var nickname : String

    fun writeBackLayout(type : String) {

        isBackLayoutLiveData.value = type

    } // writeBackLayout

    fun writeBackLayout(type : String, title : String, content : String) {

        if (title.isEmpty()) {
            titleStatus.value = "empty"
            isBackLayoutLiveData.value = "empty"
        }
        else if (title.length > 20) {
            titleStatus.value = "over"
        }
        else if (content.isEmpty()) {
            Log.d(TAG, "writeBackLayout : 눌렀잖아")
            contentStatus.value = "empty"
            isBackLayoutLiveData.value = "empty"
        }
        else if (content.length > 1000) {
            contentStatus.value = "over"
        }
        else {
            titleStatus.value = title
            contentStatus.value = content
            isBackLayoutLiveData.value = type
        }

    }

    fun clickCheck() {

        when(isBackLayoutLiveData.value) {

            "back" -> {
                isCheck.value = true
            }
            "complete" -> {
                insertFeed()
                isCheck.value = true
            }

        }

    } // clickCheck

    fun clickCancel() {

        isCheck.value = false

    } // clickCancel

    fun titleChanged(title : String) {

        this.title = title

        if (title.isEmpty()) {
            titleStatus.value = "empty"
            isBackLayoutLiveData.value = "empty"
        }
        else if (title.length > 20) {
            titleStatus.value = "over"
            isBackLayoutLiveData.value = null
        }
        else {
            titleStatus.value = title
            isBackLayoutLiveData.value = null
        }

    } // titleChanged

    fun contentChanged(content : String) {

        this.content = content

        if (content.isEmpty()) {
            contentStatus.value = "empty"
            isBackLayoutLiveData.value = "empty"
        }
        else if (content.length > 1000) {
            contentStatus.value = "over"
            isBackLayoutLiveData.value = null
        }
        else {
            contentStatus.value = content
            isBackLayoutLiveData.value = null
        }

    } // contentChanged

    private fun insertFeed() {

        val date = (System.currentTimeMillis() / 1000).toInt()

//        model.insertFeed(nickname, title, content, date).enqueue(object : Callback<ArrayList<Feed>> {
//
//            override fun onResponse(call: Call<ArrayList<Feed>>, response: Response<ArrayList<Feed>>) {
//                if (response.isSuccessful) {
//                    var msg : ArrayList<Feed>? = response.body()
//                    Log.d(TAG, "onResponse : $msg")
//                }
//            }
//
//            override fun onFailure(call: Call<ArrayList<Feed>>, t: Throwable) {
//                Log.d(TAG, "onFailure : ${t.message}")
//            }
//
//        })

        model.insertFeed(nickname, title, content, date).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var msg : String? = response.body()
                    Log.d(TAG, "onResponse : $msg")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "onFailure : ${t.message}")
            }

        })

    }

    fun toGallery() {

        toGalleryLiveData.value = true

    }

}