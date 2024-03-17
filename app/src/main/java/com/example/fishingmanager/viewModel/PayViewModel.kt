package com.example.fishingmanager.viewModel

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingmanager.data.KakaoPayLoad
import com.example.fishingmanager.data.KakaoPayReadyResponse
import com.example.fishingmanager.data.PayTicket
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.model.PayModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayViewModel(val userInfo: UserInfo) : ViewModel() {

    val model = PayModel()

    val liveDataTicketList = MutableLiveData<ArrayList<PayTicket>>()
    val liveDataBackStatus = MutableLiveData<Boolean>()
    val liveDataProduct = MutableLiveData<PayTicket>()
    val liveDataKakaoPayReadyResponse = MutableLiveData<KakaoPayReadyResponse>()
    val liveDataPayApproveStatus = MutableLiveData<Boolean>()
    var partner_order : String = ""


    // 이용권 리스트 불러오기
    fun getTicketList() {

        liveDataTicketList.value = model.getTicketList()

    } // getTicketList()


    // 이전 프래그먼트로 돌아가기
    fun backToPrevious() {

        liveDataBackStatus.value = true

    } // backToPrevious()


    // 카카오페이 준비 단계 요청 및 응답
    fun readyKakaoPay(payTicket: PayTicket) {

        liveDataProduct.value = payTicket
        partner_order = "${userInfo.nickname}${System.currentTimeMillis()}"

        val map = HashMap<String, String>()
        map["cid"] = "TC0ONETIME"
        map["partner_order_id"] = partner_order
        map["partner_user_id"] = userInfo.nickname
        map["item_name"] = payTicket.ticketName
        map["quantity"] = "1"
        map["total_amount"] = "${payTicket.ticketPrice}"
        map["tax_free_amount"] = "0"
        map["approval_url"] = "http://13.125.189.5/FM/File/success"
        map["cancel_url"] = "http://13.125.189.5/FM/File/cancel"
        map["fail_url"] = "http://13.125.189.5/FM/File/fail"

        model.readyKakaoPay(map).enqueue(object : Callback<KakaoPayReadyResponse> {
            override fun onResponse(
                call: Call<KakaoPayReadyResponse>,
                response: Response<KakaoPayReadyResponse>
            ) {
                if (response.isSuccessful) {

                    liveDataKakaoPayReadyResponse.value = response.body()

                } else {
                    Log.d("PayViewModel", "onResponse isFailure : ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<KakaoPayReadyResponse>, t: Throwable) {
                Log.d("PayViewModel", "requestKakaoPay - onFailure : $t")
            }

        })

    } // requestKakaoPay()


    // 카카오페이 결제 요청을 위한 pgToken 업데이트 및 서버에 데이터 수정 요청 및 응답
    fun updatePgToken(pgToken : String) {

        val map = HashMap<String, String>()
        map["cid"] = "TC0ONETIME"
        map["tid"] = liveDataKakaoPayReadyResponse.value!!.tid
        map["partner_order_id"] = partner_order
        map["partner_user_id"] = userInfo.nickname
        map["pg_token"] = pgToken

        model.approveKakaoPay(map).enqueue(object : Callback<KakaoPayLoad> {
            override fun onResponse(call: Call<KakaoPayLoad>, response: Response<KakaoPayLoad>) {
                if (response.isSuccessful) {

                    Log.d("PayViewModel", "updatePgToken - onResponse isSuccessful")
                    val product = response.body()!!.item_name

                    model.updateTicket(userInfo.nickname, product).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful) {

                                liveDataPayApproveStatus.value = true

                            } else {
                                liveDataPayApproveStatus.value = false
                                Log.d("PayViewModel", "updateTicket - onResponse isFailure : ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            liveDataPayApproveStatus.value = false
                            Log.d("PayViewModel", "updateTicket - onFailure : $t")
                        }

                    })

                } else {
                    liveDataPayApproveStatus.value = false
                    Log.d("PayViewModel", "updatePgToken - onResponse isFailure: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<KakaoPayLoad>, t: Throwable) {
                liveDataPayApproveStatus.value = false
                Log.d("PayViewModel", "updatePgToken - onFailure: $t")
            }

        })

    } // updatePgToken()

}