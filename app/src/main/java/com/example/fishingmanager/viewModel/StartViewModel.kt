package com.example.fishingmanager.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.model.StartModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import kotlin.concurrent.timer

class StartViewModel : ViewModel() {

    companion object {
        private const val AUTH_TIME = 180
    }

    val layoutLiveData = MutableLiveData<String>()

    val isUsableEmail = MutableLiveData<Boolean>()
    val isCorrectAuthNumber = MutableLiveData<Boolean>()
    val isUsablePassword = MutableLiveData<Boolean>()
    val isUsableRePassword = MutableLiveData<Boolean>()
    val isUsableNickname = MutableLiveData<Int>()
    val isUsableSocialNickname = MutableLiveData<Int>()
    val isClickedBackButton = MutableLiveData<Boolean>()
    val isCancelSignup = MutableLiveData<Boolean>()
    val isSavedUserInfo = MutableLiveData<Boolean>()
    val isTimeOutAuth = MutableLiveData<Boolean>()
    val authTime = MutableLiveData<Int>()
    val socialType = MutableLiveData<String>()
    val socialLoginCheck = MutableLiveData<Boolean>()
    val isSignedUpUser = MutableLiveData<Boolean>()

    val passwordValid = MutableLiveData<Boolean>()

    val pageNumberLiveData = MutableLiveData<Int>()

    val isPossibleLogin = MutableLiveData<Boolean>()

    private var model = StartModel()
    var signUpPageNumber = 1
    var signUpSocialPageNumber = 1

    var userID : String = ""
    var userPassword : String = ""
    var userNickname : String = ""
    var profileImage : String = ""
    var authNumber : String = ""
    var type : String = ""

    private var time  = AUTH_TIME
    private var timerTask : Timer? = null

    fun changeLayout(layout : String) {

        layoutLiveData.value = layout

    } // changeLayout

    fun clickPrevButton() {

        when (signUpPageNumber) {

            2 -> {
                isUsableEmail.value = null
                authNumber = ""
                stopTimer(1)
            }
            3 -> {
                isCorrectAuthNumber.value = null
                authNumber = model.createAuthNumber()
                userPassword = ""
                isUsableEmail.value = true
                startTimer()
            }
            4 -> isUsablePassword.value = null
            5 -> {
                isUsableRePassword.value = null
                userNickname = ""
            }
            6 -> isUsableNickname.value = null

        }

        signUpPageNumber--
        pageNumberLiveData.value = signUpPageNumber

    } // clickPrevButton

    fun clickNextButton(getText : String) {

        when (signUpPageNumber) {

            1 -> {
                checkUserEmail(getText)
                authNumber = model.createAuthNumber()
                startTimer()
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

    fun clickSocialPrevButton() {

        if (signUpSocialPageNumber == 2)  {
            signUpSocialPageNumber--
        }

    } // clickSocialPrevButton

    fun clickSocialNextButton(nickname : String) {

        if (signUpSocialPageNumber == 1)  {

            if (model.checkValidNickname(nickname)) {

                checkUserNickname(nickname)

            }

        }
        else {

            model.socialLoginCheck(userID, nickname, profileImage, type).enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        var msg = response.body()?.split(" ")

                        if (msg?.size == 2) {
                            userNickname = msg?.get(1).toString()
                        }

                        if (msg?.get(0) == "successLogin") {
                            socialLoginCheck.value = true
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }

            })

        }

    } // clickSocialNextButton

    fun passwordValidCheck(password : String) {

        if (signUpPageNumber == 3) {

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
                        signUpPageNumber++
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

            signUpPageNumber++
            stopTimer(1)

        }
        else {

            stopTimer(1)
            isCorrectAuthNumber.value = false

        }

    } // checkAuthNumber

    private fun checkPassword(password : String) {

        isUsablePassword.value = model.checkPassword(password)

        if (isUsablePassword.value == true) {

            userPassword = password
            signUpPageNumber++

        }

    } // checkPassword

    private fun checkRePassword(password : String) {

        isUsableRePassword.value = model.checkRePassword(userPassword, password)

        if (isUsableRePassword.value == true) {

            signUpPageNumber++

        }

    } // checkRePassword

    private fun checkUserNickname(nickname : String) {

        if (model.checkValidNickname(nickname)) {

            model.checkUserNickname(nickname).enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        var msg : String? = response.body()

                        if (msg == "usableNickname") {
                            userNickname = nickname
                            isUsableNickname.value = 0
                            isUsableSocialNickname.value = 0
                            signUpPageNumber++
                            signUpSocialPageNumber++
                        }
                        else {
                            isUsableNickname.value = 1
                            isUsableSocialNickname.value = 1
                        }

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    isUsableNickname.value = 3
                    isUsableSocialNickname.value = 3
                }

            })

        }
        else {

            isUsableNickname.value = 2
            isUsableSocialNickname.value = 2

        }

    } // checkUserNickname

    private fun saveUserInfo() {

        model.saveUserInfo(userID, userPassword, userNickname).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var msg : String? = response.body()

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
                    var msg = response.body()?.split(" ")

                    if (msg?.size == 2) {
                        userNickname = msg?.get(1).toString()
                    }

                    isPossibleLogin.value = msg?.get(0) == "successLogin"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })

    } // loginCheck

    private fun startTimer() {

        time = AUTH_TIME

        timerTask = timer(period = 1000) {

            if (time >= 0) {

                authTime.postValue(time)
                time--

            }
            else {

                stopTimer(0)

            }

        }

    } // startTimer

    private fun stopTimer(type : Int) {

        timerTask?.cancel()
        model.authenticationNumber = "!"
        if (type == 0) {
            isTimeOutAuth.postValue(false)
            authTime.postValue(time)
        }
        else {
            authTime.value = time
        }

    } // stopTimer

    fun reSendMail() {

        authNumber = model.createAuthNumber()
        isUsableEmail.value = true
        startTimer()

    } // reSendMail

    fun socialLogin(type : String) {

        this.type = type
        socialType.value = type

    } // socialLogin

    fun isSignedUpUserCheck(id : String, type : String) {

        model.isSignedUpUserCheck(id, type).enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var msg = response.body()?.split(" ")

                    if (msg?.size == 2) {

                        userNickname = msg?.get(1).toString()
                        isSignedUpUser.value = true

                    }
                    else {

                        isSignedUpUser.value = false

                    }

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })

    }

}