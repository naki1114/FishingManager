package com.example.fishingmanager.network

import com.example.fishingmanager.data.Collection
import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.Comment
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.History
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.Tide
import com.example.fishingmanager.data.Weather
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RetrofitInterface {

    // 날씨 API
    @GET("getVilageFcst?serviceKey=${RetrofitClient.WEATHER_KEY}")
    fun requestWeather(
        @Query("pageNo") pageNo: String,
        @Query("numOfRows") numOfRows: String,
        @Query("dataType") dataType: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: String,
        @Query("ny") ny: String
    ): Call<Weather>


    // 조석 API
    @GET("tideObsPreTab/search.do?ServiceKey=${RetrofitClient.SEE_KEY}")
    fun requestTide(
        @Query("Date") baseDate: String,
        @Query("ObsCode") location: String,
        @Query("ResultType") resultType: String
    ): Call<Tide>


    // 해양생활예보지수 API
    @GET("fcIndexOfType/search.do?ServiceKey=${RetrofitClient.SEE_KEY}")
    fun requestIndex(
        @Query("Type") type: String,
        @Query("ResultType") resultType: String
    ): Call<Index>


    // 도감, 기록, 게시글, 사용자 정보 조회
    @FormUrlEncoded
    @POST("File/Splash/GetUserData.php")
    fun requestDB(@Field("nickname") nickname: String) : Call<Combine>


    // 이메일 중복 체크
    @FormUrlEncoded
    @POST("File/UserInfo/EmailDuplicateCheck.php")
    fun checkUserEmail(@Field("id") id: String): Call<String>


    // 닉네임 중복 체크
    @FormUrlEncoded
    @POST("File/UserInfo/NicknameDuplicateCheck.php")
    fun checkUserNickname(@Field("nickname") nickname: String): Call<String>


    // 회원가입 완료
    @FormUrlEncoded
    @POST("File/UserInfo/FinishSignup.php")
    fun saveUserInfo(
        @Field("id") id: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String
    ): Call<String>


    // 비밀번호 찾기
    @FormUrlEncoded
    @POST("File/UserInfo/FindPassword.php")
    fun changePassword(
        @Field("id") id: String,
        @Field("password") password: String
    )


    // 로그인 정보 조회
    @FormUrlEncoded
    @POST("File/UserInfo/LoginCheck.php")
    fun checkUserInfo(
        @Field("id") id: String,
        @Field("password") password: String
    ): Call<String>


    // 회원탈퇴
    @FormUrlEncoded
    @POST("File/UserInfo/DeleteAccount.php")
    fun deleteUserInfo(@Field("nickname") nickname: String): Call<String>


    // 프로필 사진 수정
    @Multipart
    @FormUrlEncoded
    @POST("File/UserInfo/UpdateProfileImage.php")
    fun updateProfileImage(
        @Part uploadFile: MultipartBody.Part,
        @Field("profileImage") profileImage: String
    ): Call<String>


    // 게시글 요청
    @FormUrlEncoded
    @POST("File/Feed/GetFeed.php")
    fun getFeed(): Call<ArrayList<Feed>>


    // 게시글 추가 (이미지 미포함)
    @FormUrlEncoded
    @POST("File/Feed/InsertFeed.php")
    fun insertFeed(
        @Field("nickname") nickname: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: Int
    ): Call<String>


    // 게시글 추가
    @Multipart
    @FormUrlEncoded
    @POST("File/Feed/InsertFeed.php")
    fun insertFeed(
        @Part uploadFile: MultipartBody.Part,
        @Field("nickname") nickname: String,
        @Field("writeImage") writeImage: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String
    ): Call<ArrayList<Feed>>


    // 게시글 수정
    @Multipart
    @FormUrlEncoded
    @POST("File/Feed/UpdateFeed.php")
    fun updateFeed(
        @Part uploadFile: MultipartBody.Part,
        @Field("nickname") nickname: String,
        @Field("writeImage") writeImage: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String
    ): Call<String>


    // 게시글 삭제
    @FormUrlEncoded
    @POST("File/Feed/DeleteFeed.php")
    fun deleteFeed(
        @Field("nickname") nickname: String,
        @Field("feedNum") feedNum: Int
    ): Call<String>


    // 댓글 요청
    @FormUrlEncoded
    @POST("File/Feed/GetComment.php")
    fun getComment(): Call<ArrayList<Comment>>


    // 댓글 추가
    @FormUrlEncoded
    @POST("File/Feed/InsertComment.php")
    fun insertComment(
        @Field("nickname") nickname: String,
        @Field("feedNum") feedNum: Int,
        @Field("content") content: String,
        @Field("date") date: String
    ): Call<ArrayList<Comment>>


    // 댓글 삭제
    @FormUrlEncoded
    @POST("File/Feed/DeleteComment.php")
    fun deleteComment(
        @Field("nickname") nickname: String,
        @Field("feedNum") feedNum: Int,
        @Field("date") date: String
    ): Call<String>


    // 도감 조회
    @FormUrlEncoded
    @POST("File/CheckingFish/CheckCollection.php")
    fun checkCollection(
        @Field("nickname") nickname: String,
        @Field("fish") fish: String,
        @Field("fishLength") fishLength: String,
        @Field("date") date: String
    ): Call<ArrayList<Collection>>


    // 기록 추가
    @Multipart
    @FormUrlEncoded
    @POST("File/CheckingFish/InsertHistory.php")
    fun saveHistory(
        @Part uploadFile: MultipartBody.Part,
        @Field("nickname") nickname: String,
        @Field("fish") fish: String,
        @Field("fishLength") fishLength: String,
        @Field("date") date: String
    ): Call<ArrayList<History>>


    // 이용권 구매
    @FormUrlEncoded
    @POST("File/UserInfo/UpdateTicket.php")
    fun updateTicket(
        @Field("nickname") nickname: String,
        @Field("product") product: String,
        @Field("dueDate") dueDate: String
    ): Call<String>

}