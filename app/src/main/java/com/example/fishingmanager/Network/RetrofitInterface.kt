package com.example.fishingmanagerclone.Network

import com.example.fishingmanagerclone.Data.Collection
import com.example.fishingmanagerclone.Data.Comment
import com.example.fishingmanagerclone.Data.Feed
import com.example.fishingmanagerclone.Data.History
import com.example.fishingmanagerclone.Data.Index
import com.example.fishingmanagerclone.Data.Tide
import com.example.fishingmanagerclone.Data.Weather
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RetrofitInterface {

    // 날씨 API
    @GET("getVilageFcst?serviceKey=" + RetrofitClient.WEATHER_KEY)
    fun requestWeather(
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dataType") dataType: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: String,
        @Query("ny") ny: String
    ): Call<Weather>


    // 조석 API
    @GET("tideObsPreTab/search.do?ServiceKey=" + RetrofitClient.SEE_KEY)
    fun requestTide(
        @Query("Date") baseDate: String,
        @Query("ObsCode") location: String,
        @Query("ResultType") resultType: String
    ): Call<Tide>


    // 해양생활예보지수 API
    @GET("fcIndexOfType/search.do?ServiceKey=" + RetrofitClient.SEE_KEY)
    fun requestIndex(
        @Query("Type") type: String,
        @Query("ResultType") resultType: String
    ): Call<Index>


    // 도감 데이터 조회
    @POST("")
    fun requestCollection(@Field("nickname") nickname: String): Call<ArrayList<Collection>>


    // 기록 데이터 조회
    @POST("")
    fun requestHistory(@Field("nickname") nickname: String): Call<ArrayList<History>>


    // 이메일 중복 체크
    @POST("")
    fun checkUserEmail(@Field("id") id: String): Call<String>


    // 닉네임 중복 체크
    @POST("")
    fun checkUserNickname(@Field("nickname") nickname: String): Call<String>


    // 회원가입 완료
    @POST("")
    fun saveUserInfo(
        @Field("id") id: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String
    ): Call<String>


    // 로그인 정보 조회
    @POST("")
    fun checkUserInfo(
        @Field("id") id: String,
        @Field("password") password: String
    ): Call<String>


    // 회원탈퇴
    @POST("")
    fun deleteUserInfo(@Field("nickname") nickname: String): Call<String>


    // 프로필 사진 수정
    @Multipart
    @POST("")
    fun updateProfileImage(
        @Part uploadFile: MultipartBody.Part,
        @Field("profileImage") profileImage: String
    ): Call<String>


    // 게시글 추가
    @Multipart
    @POST("")
    fun insertFeed(
        @Part uploadFile: MultipartBody.Part,
        @Field("nickname") nickname: String,
        @Field("writeImage") writeImage: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String
    ): Call<ArrayList<Feed>>


    // 댓글 추가
    @POST("")
    fun insertComment(
        @Field("nickname") nickname: String,
        @Field("content") content: String,
        @Field("date") date: String
    ): Call<ArrayList<Comment>>


    // 도감 조회
    @POST("")
    fun checkCollection(
        @Field("nickname") nickname: String,
        @Field("fish") fish: String,
        @Field("fishLength") fishLength: String,
        @Field("date") date: String
    ): Call<ArrayList<Collection>>


    // 기록 추가
    @Multipart
    @POST("")
    fun saveHistory(
        @Part uploadFile: MultipartBody.Part,
        @Field("nickname") nickname: String,
        @Field("fish") fish: String,
        @Field("fishLength") fishLength: String,
        @Field("date") date: String
    ): Call<ArrayList<History>>


    // 이용권 구매
    @POST("")
    fun updateTicket(
        @Field("nickname") nickname: String,
        @Field("product") product: String,
        @Field("dueDate") dueDate: String
    ): Call<String>


}