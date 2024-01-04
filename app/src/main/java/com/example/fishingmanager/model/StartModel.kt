package com.example.fishingmanager.model

import com.example.fishingmanager.function.GetRandomResult
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface
import java.util.regex.Pattern

class StartModel {

    private val responseServer = RetrofitClient.getWebServer().create(RetrofitInterface::class.java)

    private lateinit var authenticationNumber : String

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

    fun checkUserNickname(nickname : String) = responseServer.checkUserNickname(nickname)

    fun saveUserInfo(id : String, password : String, nickname : String) = responseServer.saveUserInfo(id, password, nickname)

    fun checkUserInfo(id : String, password : String) = responseServer.checkUserInfo(id, password)

}