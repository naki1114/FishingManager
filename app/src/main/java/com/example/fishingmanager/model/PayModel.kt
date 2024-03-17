package com.example.fishingmanager.model

import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.fishingmanager.R
import com.example.fishingmanager.data.PayTicket
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.network.RetrofitInterface
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.Header

class PayModel : WebViewClient() {

    private val webServerRetrofitInterface: RetrofitInterface = RetrofitClient.getWebServer().create(RetrofitInterface::class.java)
    private val kakaoPayRetrofitInterface: RetrofitInterface = RetrofitClient.getKaKaoPayAPI().create(RetrofitInterface::class.java)
    val list = ArrayList<PayTicket>()


    // 이용권 리스트 불러오기
    fun getTicketList(): ArrayList<PayTicket> {

        list.add(
            PayTicket(
                "FM 세트 한 달 이용권",
                R.drawable.checkingfish_ticket,
                R.drawable.remove_ad_ticket,
                "30일",
                "￦ 5,800 → ￦ 4,900/ 월",
                false,
                4900,
                "1"
            )
        )
        list.add(
            PayTicket(
                "FM 세트 일 년 이용권",
                R.drawable.checkingfish_ticket,
                R.drawable.remove_ad_ticket,
                "365일",
                "￦ 69,600 → ￦ 49,000 / 년",
                true,
                49000,
                "2"
            )
        )
        list.add(
            PayTicket(
                "어종 확인 한 달 이용권",
                R.drawable.checkingfish_ticket,
                0,
                "30일",
                "￦ 3,900 / 월",
                false,
                3900,
                "3"

            )
        )
        list.add(
            PayTicket(
                "어종 확인 일 년 이용권",
                R.drawable.checkingfish_ticket,
                0,
                "365일",
                "￦ 46,800 → ￦ 39,000 / 년",
                true,
                39000,
                "4"
            )
        )
        list.add(
            PayTicket(
                "광고 제거 한 달 이용권",
                R.drawable.remove_ad_ticket,
                0,
                "30일",
                "￦ 1,900 / 월",
                false,
                1900,
                "5"
            )
        )
        list.add(
            PayTicket(
                "광고 제거 일 년 이용권",
                R.drawable.remove_ad_ticket,
                0,
                "365일",
                "￦ 22,800 → ￦ 19,000 / 년",
                true,
                19000,
                "6"
            )
        )

        return list

    } // getTicketList()


    // 카카오페이 준비 단계 - 요청
    fun readyKakaoPay(
        map : HashMap<String, String>
    ) = kakaoPayRetrofitInterface.readyKakaoPay(
        RetrofitClient.KAKAOPAY_SECRET_KEY,
        map
    ) // readyKakaoPay()


    // 카카오페이 결제 완료 - 요청
    fun approveKakaoPay(
        map : HashMap<String, String>
    ) = kakaoPayRetrofitInterface.approveKakaoPay(
        RetrofitClient.KAKAOPAY_SECRET_KEY,
        map
    ) // readyKakaoPay()


    // 결제 완료 후 웹 서버에 이용권 업데이트 요청
    fun updateTicket(nickname : String, product : String) = webServerRetrofitInterface.updateTicket(nickname, product)


}