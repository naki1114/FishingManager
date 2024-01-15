package com.example.fishingmanager.model

import com.example.fishingmanager.function.GetRandomResult
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface
import java.util.regex.Pattern

class StartModel {

    private val responseServer = RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

    lateinit var authenticationNumber : String

    fun checkUserEmail(id : String) = responseServer.checkUserEmail(id)

    fun createAuthNumber() : String {

        authenticationNumber = GetRandomResult().getRandomResult()

        return authenticationNumber

    } // createAuthNumber

    fun checkAuthNumber(authNumber : String) : Boolean {

        return authenticationNumber == authNumber

    } // checkAuthNumber

    fun checkPassword(password : String) : Boolean {

        val passwordPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?%.])[A-Za-z[0-9]$@$!%*#?%.]{8,16}$"
        val pattern = Pattern.compile(passwordPattern)

        val match = pattern.matcher(password)

        return match.find().toString() == "true"

    } // checkPassword

    fun checkRePassword(userPassword : String, password : String) : Boolean {

        return userPassword == password

    } // checkRePassword

    fun checkValidNickname(nickname : String) : Boolean {

        val nicknamePattern1 = "^(?=.*[가-힣])[가-힣]{2,10}$"
        val pattern1 = Pattern.compile(nicknamePattern1)
        val match1 = pattern1.matcher(nickname)

        val nicknamePattern2 = "^(?=.*[가-힣])(?=.*[0-9])[가-힣0-9]{2,10}$"
        val pattern2 = Pattern.compile(nicknamePattern2)
        val match2 = pattern2.matcher(nickname)

        val nicknamePattern3 = "^(?=.*[a-zA-Z])[a-zA-Z]{2,10}$"
        val pattern3 = Pattern.compile(nicknamePattern3)
        val match3 = pattern3.matcher(nickname)

        val nicknamePattern4 = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{2,10}$"
        val pattern4 = Pattern.compile(nicknamePattern4)
        val match4 = pattern4.matcher(nickname)

        val nicknamePattern5 = "^(?=.*[가-힣])(?=.*[a-zA-Z])[가-힣a-zA-Z]{2,10}$"
        val pattern5 = Pattern.compile(nicknamePattern5)
        val match5 = pattern5.matcher(nickname)

        val nicknamePattern6 = "^(?=.*[가-힣])(?=.*[a-zA-Z])(?=.*[0-9])[가-힣a-zA-Z0-9]{2,10}$"
        val pattern6 = Pattern.compile(nicknamePattern6)
        val match6 = pattern6.matcher(nickname)

        return match1.find().toString() == "true" || match2.find().toString() == "true" || match3.find().toString() == "true" || match4.find().toString() == "true"
                || match5.find().toString() == "true" || match6.find().toString() == "true"

    }

    fun checkUserNickname(nickname : String) = responseServer.checkUserNickname(nickname)

    fun saveUserInfo(id : String, password : String, nickname : String) = responseServer.saveUserInfo(id, password, nickname)

    fun checkUserInfo(id : String, password : String) = responseServer.checkUserInfo(id, password)

}