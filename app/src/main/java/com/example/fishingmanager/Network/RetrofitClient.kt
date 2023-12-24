package com.example.fishingmanager.Network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    private val BASE_URL: String = ""
    private val WEATHER_URL : String = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
    private val TIDE_URL : String = "https://www.khoa.go.kr/api/oceangrid/"
    private val INDEX_URL : String = "https://www.khoa.go.kr/api/oceangrid/"

    companion object {

        const val BASE_PROFILEIMAGE_URL : String = "/profileImage/"
        const val BASE_COLLECTIONIMAGE_URL : String = "/collectionImage/"
        const val BASE_HISTORYIMAGE_URL : String = "/historyImage/"
        const val BASE_FEEDIMAGE_URL : String = "/feedImage/"
        const val WEATHER_KEY : String = "Oq7LVV8pYwr%2FIbPdcoazaGdaAZvQw238%2FIC%2F5T7ximjXZYKku1Ft3K4RpUYIQOu2xPiTKRqM5fZIICFUBVbrbQ%3D%3D"
        const val SEE_KEY : String = "9OtHEMl1FMRpzk6Y32waMg=="

    }

    fun getWebServer(): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    } // getRetrofitClient()


    fun getWeatherAPI() : Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    } // getWeatherAPI()


    fun getTideAPI() : Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(TIDE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    } // getTideAPI()


    fun getIndexAPI() : Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(INDEX_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    } // getIndexAPI()


}