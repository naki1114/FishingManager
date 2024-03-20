package com.example.fishingmanager.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.model.StartModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import kotlin.concurrent.timer

class StartViewModel: ViewModel() {

    // 인증 번호 대기 시간 : 3분 (180초)
    companion object {
        private const val AUTH_TIME = 180
    }

    private val TAG = "StartViewModel"

    val layoutLiveData = MutableLiveData<String>()

    val isUsableEmail = MutableLiveData<Boolean>()
    val isCorrectAuthNumber = MutableLiveData<Boolean>()
    val isUsablePassword = MutableLiveData<Boolean>()
    val isUsableRePassword = MutableLiveData<Boolean>()
    val isUsableNickname = MutableLiveData<Int>()
    val isUsableSocialNickname = MutableLiveData<Int>()
    val isSavedUserInfo = MutableLiveData<Boolean>()
    val isTimeOutAuth = MutableLiveData<Boolean>()
    val authTime = MutableLiveData<Int>()
    val socialType = MutableLiveData<String>()
    val socialLoginCheck = MutableLiveData<Boolean>()
    val isSignedUpUser = MutableLiveData<Boolean>()
    val isSuccessfulFindPassword = MutableLiveData<Boolean>()

    val passwordValid = MutableLiveData<Boolean>()

    val pageNumberLiveData = MutableLiveData<Int>()

    val isPossibleLogin = MutableLiveData<Boolean>()

    private var model = StartModel()
    var signUpPageNumber = 1
    var signUpSocialPageNumber = 1

    var userID: String = ""
    var userPassword: String = ""
    var userNickname: String = ""
    var profileImage: String = ""
    var authNumber: String = ""
    var type: String = ""

    private var time  = AUTH_TIME
    private var timerTask: Timer? = null


    // 레이아웃 변경
    fun changeLayout(layout: String) {

        layoutLiveData.value = layout

    } // changeLayout()


    // FM 회원 가입시 이전 버튼 클릭
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

    } // clickPrevButton()


    // FM 회원 가입시 다음 버튼 클릭
    fun clickNextButton(userInfo: String) {

        when (signUpPageNumber) {

            1 -> {

                checkUserEmail(userInfo)
                authNumber = model.createAuthNumber()
                startTimer()

            }
            2 -> checkAuthNumber(userInfo)
            3 -> checkPassword(userInfo)
            4 -> checkRePassword(userInfo)
            5 -> checkUserNickname(userInfo)
            6 -> saveUserInfo()

        }

    } // clickNextButton()


    // 소셜 로그인 닉네임 입력시 이전 버튼 클릭
    fun clickSocialPrevButton() {

        if (signUpSocialPageNumber == 2)  {
            signUpSocialPageNumber--
        }

    } // clickSocialPrevButton()


    // 소셜 로그인 닉네임 입력시 다음 버튼 클릭
    fun clickSocialNextButton(nickname: String) {

        if (signUpSocialPageNumber == 1)  {

            if (model.checkValidNickname(nickname)) {

                checkUserNickname(nickname)

            }

        } else {

            model.socialLoginCheck(userID, nickname, profileImage, type).enqueue(object: Callback<String> {

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

                    Log.d(TAG, "onFailure : ${t.message}")

                }

            })

        }

    } // clickSocialNextButton()


    // 비밀번호 정규식 확인
    fun passwordValidCheck(password: String) {

        if (signUpPageNumber == 3) {

            passwordValid.value = model.checkPassword(password)

        }

    } // passwordValidCheck()


    // Retrofit) 이메일 중복 체크
    private fun checkUserEmail(id: String) {

        val layout = layoutLiveData.value

        model.checkUserEmail(id, layout!!).enqueue(object: Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful) {

                    var msg : String? = response.body()

                    if (msg == "usableID") {

                        userID = id
                        isUsableEmail.value = true
                        signUpPageNumber++

                    } else {

                        isUsableEmail.value = false

                    }

                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Log.d(TAG, "onFailure : ${t.message}")
                isUsableEmail.value = false

            }

        })

    } // checkUserEmail()


    // 인증 번호 일치 확인
    private fun checkAuthNumber(authNumber: String) {

        isCorrectAuthNumber.value = model.checkAuthNumber(authNumber)

        if (isCorrectAuthNumber.value == true) {

            signUpPageNumber++
            stopTimer(1)

        } else {

            stopTimer(1)
            isCorrectAuthNumber.value = false

        }

    } // checkAuthNumber()


    // 비밀번호 정규식 확인
    private fun checkPassword(password: String) {

        isUsablePassword.value = model.checkPassword(password)

        if (isUsablePassword.value == true) {

            userPassword = password
            signUpPageNumber++

        }

    } // checkPassword()


    // 비밀번호 재입력 일치 확인
    private fun checkRePassword(password: String) {

        isUsableRePassword.value = model.checkRePassword(userPassword, password)

        if (isUsableRePassword.value == true) {

            if (layoutLiveData.value == "signup") {

                signUpPageNumber++

            } else if (layoutLiveData.value == "findPassword") {

                changePassword(userID, userPassword)

            }

        }

    } // checkRePassword()


    // Retrofit) 닉네임 정규식 확인 후 중복 확인
    private fun checkUserNickname(nickname: String) {

        if (model.checkValidNickname(nickname)) {

            model.checkUserNickname(nickname).enqueue(object: Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {

                    if (response.isSuccessful) {

                        var msg: String? = response.body()

                        if (msg == "usableNickname") {

                            userNickname = nickname
                            isUsableNickname.value = 0
                            isUsableSocialNickname.value = 0
                            signUpPageNumber++
                            signUpSocialPageNumber++

                        } else {

                            isUsableNickname.value = 1
                            isUsableSocialNickname.value = 1

                        }

                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                    Log.d(TAG, "onFailure : ${t.message}")
                    isUsableNickname.value = 3
                    isUsableSocialNickname.value = 3

                }

            })

        } else {

            isUsableNickname.value = 2
            isUsableSocialNickname.value = 2

        }

    } // checkUserNickname()


    // Retrofit) 서버에 회원 정보 저장
    private fun saveUserInfo() {

        model.saveUserInfo(userID, userPassword, userNickname).enqueue(object: Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful) {

                    var msg : String? = response.body()

                    if (msg == "usable") {

                        isSavedUserInfo.value = true

                    }

                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Log.d(TAG, "onFailure : ${t.message}")

            }

        })

    } // saveUserInfo()


    // Retrofit) 로그인 정보 조회
    fun loginCheck(id: String, password: String) {

        model.checkUserInfo(id, password).enqueue(object: Callback<String> {

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

                Log.d(TAG, "onFailure : ${t.message}")

            }

        })

    } // loginCheck()


    // 인증 제한 시간 카운트 시작
    private fun startTimer() {

        time = AUTH_TIME

        timerTask = timer(period = 1000) {

            if (time >= 0) {

                authTime.postValue(time)
                time--

            } else {

                stopTimer(0)

            }

        }

    } // startTimer()


    // 인증 제한 시간 카운트 종료
    private fun stopTimer(type: Int) {

        timerTask?.cancel()
        model.authenticationNumber = "!"

        if (type == 0) {

            isTimeOutAuth.postValue(false)
            authTime.postValue(time)

        } else {

            authTime.value = time

        }

    } // stopTimer()


    // 인증 번호 재생성 후 재전송
    fun reSendMail() {

        authNumber = model.createAuthNumber()
        isUsableEmail.value = true
        startTimer()

    } // reSendMail()


    // 소셜 로그인 타입 확인 (google / naver / kakao)
    fun socialLogin(type: String) {

        this.type = type
        socialType.value = type

    } // socialLogin()


    // Retrofit) 소셜 로그인 가입된 회원인지 확인
    fun isSignedUpUserCheck(id: String, type: String) {

        model.isSignedUpUserCheck(id, type).enqueue(object: Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful) {

                    var msg = response.body()?.split(" ")

                    if (msg?.size == 2) {

                        userNickname = msg?.get(1).toString()
                        isSignedUpUser.value = true

                    } else {

                        isSignedUpUser.value = false

                    }

                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Log.d(TAG, "onFailure : ${t.message}")

            }

        })

    } // isSignedUpUserCheck()


    // Retrofit) 비밀번호 변경
    private fun changePassword(id: String, password: String) {

        model.changePassword(id, password).enqueue(object: Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful) {

                    val msg = response.body()
                    if (msg == "successful") {

                        isSuccessfulFindPassword.value = true

                    }

                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Log.d(TAG, "onFailure : ${t.message}")

            }

        })

    }


}