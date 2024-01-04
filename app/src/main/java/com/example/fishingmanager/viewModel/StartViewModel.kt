package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.model.StartModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartViewModel : ViewModel() {

    val layoutLiveData = MutableLiveData<String>()

    val isUsableEmail = MutableLiveData<Boolean>()
    val isCorrectAuthNumber = MutableLiveData<Boolean>()
    val isUsablePassword = MutableLiveData<Boolean>()
    val isUsableRePassword = MutableLiveData<Boolean>()
    val isUsableNickname = MutableLiveData<Boolean>()
    val isClickedBackButton = MutableLiveData<Boolean>()
    val isCancelSignup = MutableLiveData<Boolean>()
    val isSavedUserInfo = MutableLiveData<Boolean>()
    val isTimeOutAuth = MutableLiveData<Boolean>()

    val passwordValid = MutableLiveData<Boolean>()

    val pageNumberLiveData = MutableLiveData<Int>()

    val isPossibleLogin = MutableLiveData<Boolean>()

    private var model = StartModel()
    var pageNumber = 1

    var userID : String = ""
    var userPassword : String = ""
    var userNickname : String = ""
    var authNumber : String = ""

    fun changeLayout(layout : String) {

        layoutLiveData.value = layout

    } // changeLayout

    fun clickPrevButton() {

        when (pageNumber) {

            2 -> {
                isUsableEmail.value = false
                authNumber = ""
            }
            3 -> {
                isCorrectAuthNumber.value = false
                authNumber = model.createAuthNumber()
                Log.d("authNumber", "clickPrevButton : $authNumber")
                userPassword = ""
            }
            4 -> isUsablePassword.value = false
            5 -> {
                isUsableRePassword.value = false
                userNickname = ""
            }
            6 -> isUsableNickname.value = false

        }

        pageNumber--
        pageNumberLiveData.value = pageNumber

    } // clickPrevButton

    fun clickNextButton(getText : String) {

        when (pageNumber) {

            1 -> {
                checkUserEmail(getText)
                authNumber = model.createAuthNumber()
                Log.d("authNumber", "clickNextButton : $authNumber")
            }
            2 -> {
                checkAuthNumber(getText)
            }
            3 -> {
                checkPassword(getText)
            }
            4 -> {
                checkRePassword(getText)
            }
            5 -> {
                checkUserNickname(getText)
            }
            6 -> {
                saveUserInfo()
            }

        }

    } // clickNextButton

    fun passwordValidCheck(password : String) {

        if (pageNumber == 3) {

            passwordValid.value = model.checkPassword(password)

        }

    } // passwordValid

    private fun checkUserEmail(id : String) {

        model.checkUserEmail(id).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var msg : String? = response.body()

                    if (msg == "usableID") {
                        userID = id
                        isUsableEmail.value = true
                        pageNumber++
                    }
                    else {
                        isUsableEmail.value = false
                    }

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                isUsableEmail.value = false
            }

        })

    } // checkUserEmail

    private fun checkAuthNumber(authNumber : String) {

        isCorrectAuthNumber.value = model.checkAuthNumber(authNumber)

        if (isCorrectAuthNumber.value == true) {

            pageNumber++

        }

    } // checkAuthNumber

    private fun checkPassword(password : String) {

        isUsablePassword.value = model.checkPassword(password)

        if (isUsablePassword.value == true) {

            userPassword = password
            pageNumber++
            Log.d("TAG", "checkPassword : $pageNumber")

        }

    } // checkPassword

    private fun checkRePassword(password : String) {

        isUsableRePassword.value = model.checkRePassword(userPassword, password)

        if (isUsableRePassword.value == true) {

            pageNumber++

        }

    } // checkRePassword

    private fun checkUserNickname(nickname : String) {

        model.checkUserNickname(nickname).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var msg : String? = response.body()

                    if (msg == "usableNickname") {
                        userNickname = nickname
                        isUsableNickname.value = true
                        pageNumber++
                    }
                    else {
                        isUsableNickname.value = false
                    }

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                isUsableNickname.value = false
            }

        })

    } // checkUserNickname

    private fun saveUserInfo() {

        Log.d("authNumber", "saveUserInfo : $userID, $userPassword, $userNickname")

        model.saveUserInfo(userID, userPassword, userNickname).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var msg : String? = response.body()
                    Log.d("TAG", "onResponse : $msg")

                    if (msg == "usable") {
                        isSavedUserInfo.value = true
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })

    } // saveUserInfo

    fun loginCheck(id : String, password : String) {

        model.checkUserInfo(id, password).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var msg : String? = response.body()

                    isPossibleLogin.value = msg == "successLogin"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })

    } // checkUserInfo

}