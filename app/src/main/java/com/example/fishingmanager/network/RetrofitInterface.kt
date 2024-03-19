package com.example.fishingmanager.network

import com.example.fishingmanager.data.Combine
import com.example.fishingmanager.data.Comment
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.data.Index
import com.example.fishingmanager.data.KakaoPayLoad
import com.example.fishingmanager.data.KakaoPayReadyResponse
import com.example.fishingmanager.data.Tide
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.data.Weather
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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
    fun requestDB(
        @Field("nickname") nickname: String
    ): Call<Combine>


    // 이메일 중복 체크
    @FormUrlEncoded
    @POST("File/UserInfo/EmailDuplicateCheck.php")
    fun checkUserEmail(
        @Field("id") id: String
    ): Call<String>


    // 닉네임 중복 체크
    @FormUrlEncoded
    @POST("File/UserInfo/NicknameDuplicateCheck.php")
    fun checkUserNickname(
        @Field("nickname") nickname: String
    ): Call<String>


    // 회원가입 완료
    @FormUrlEncoded
    @POST("File/UserInfo/FinishSignup.php")
    fun saveUserInfo(
        @Field("id") id: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String
    ): Call<String>


    // 소셜 로그인
    @FormUrlEncoded
    @POST("File/UserInfo/SocialLogin.php")
    fun socialLoginCheck(
        @Field("id") id: String,
        @Field("nickname") nickname: String,
        @Field("profileImage") profileImage: String,
        @Field("type") type: String
    ): Call<String>


    // 소셜 로그인 가입 여부 체크
    @FormUrlEncoded
    @POST("File/UserInfo/SocialCheckUserInfo.php")
    fun isSignedUpUserCheck(
        @Field("id") id: String,
        @Field("type") type: String
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
    fun deleteUserInfo(
        @Field("nickname") nickname: String,
        @Field("type") type: String
    ): Call<String>


    // 프로필 사진 수정
    @Multipart
    @POST("File/UserInfo/UpdateProfileImage.php")
    fun updateProfileImage(
        @Part uploadFile: MultipartBody.Part,
        @Part("nickname") nickname: String
    ): Call<Combine>


    // 게시글 요청
    @POST("File/Feed/GetFeed.php")
    fun getFeed(): Call<ArrayList<Feed>>


    // 게시글 추가 (이미지 미포함)
    @FormUrlEncoded
    @POST("File/Feed/InsertFeed.php")
    fun insertFeed(
        @Field("nickname") nickname: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String
    ): Call<ArrayList<Feed>>


    // 게시글 추가 (이미지 포함)
    @Multipart
    @POST("File/Feed/InsertFeed.php")
    fun insertImageFeed(
        @Part uploadFile: MultipartBody.Part,
        @Part("nickname") nickname: String,
        @Part("title") title: String,
        @Part("content") content: String,
        @Part("date") date: String
    ): Call<ArrayList<Feed>>


    // 게시글 조회수 업데이트
    @FormUrlEncoded
    @POST("File/Feed/UpdateViewCount.php")
    fun updateViewCount(
        @Field("nickname") nickname: String,
        @Field("date") date: String
    ): Call<ArrayList<Feed>>


    // 댓글 요청
    @FormUrlEncoded
    @POST("File/Feed/GetComment.php")
    fun getComment(
        @Field("feedNum") feedNum: Int
    ): Call<ArrayList<Comment>>


    // 댓글 추가
    @FormUrlEncoded
    @POST("File/Feed/InsertComment.php")
    fun insertComment(
        @Field("nickname") nickname: String,
        @Field("feedNum") feedNum: String,
        @Field("content") content: String,
        @Field("date") date: String
    ): Call<ArrayList<Comment>>


    // 기록 추가
    @Multipart
    @POST("File/CheckingFish/InsertHistory.php")
    fun saveHistory(
        @Part uploadFile: MultipartBody.Part,
        @Part("nickname") nickname: String,
        @Part("fish") fish: String,
        @Part("date") date: String
    ): Call<Combine>


    // 이용권 구매
    @FormUrlEncoded
    @POST("File/UserInfo/UpdateTicket.php")
    fun updateTicket(
        @Field("nickname") nickname: String,
        @Field("product") product: String
    ): Call<UserInfo>


    // 카카오페이 요청
    @POST("v1/payment/ready")
    @FormUrlEncoded
    fun readyKakaoPay(
        @Header("Authorization") authorization : String,
        @FieldMap map : HashMap<String, String>
    ): Call<KakaoPayReadyResponse>


    // 카카오페이 승인
    @POST("v1/payment/approve")
    @FormUrlEncoded
    fun approveKakaoPay(
        @Header("Authorization") authorization : String,
        @FieldMap map : HashMap<String, String>
    ): Call<KakaoPayLoad>


    @POST("File/UserInfo/ResetCheckingFishCount.php")
    @FormUrlEncoded
    fun resetCheckingFishCount(
        @Field("nickname") nickname: String
    ): Call<String>

}