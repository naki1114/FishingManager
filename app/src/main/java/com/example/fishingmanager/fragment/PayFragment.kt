package com.example.fishingmanager.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.PayTicketAdapter
import com.example.fishingmanager.databinding.FragmentPayBinding
import com.example.fishingmanager.viewModel.PayViewModel

class PayFragment : Fragment() {

    lateinit var binding : FragmentPayBinding
    lateinit var viewModel : PayViewModel

    lateinit var adapter : PayTicketAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, container, false)

        return binding.root

    } // onCreateView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariable()
        observeLiveData()
        viewModel.getTicketList()

    } // onViewCreated()


    // 변수 초기화
    fun setVariable() {

        viewModel = PayViewModel((activity as MainActivity).userInfo)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = PayTicketAdapter(PayTicketAdapter.ItemClickListener {
            viewModel.readyKakaoPay(it)
        })
        binding.payRecyclerView.adapter = adapter

    } // setVariable()


    // ViewModel의 LiveData 관찰
    fun observeLiveData() {

        viewModel.liveDataTicketList.observe(viewLifecycleOwner, Observer {

            adapter.setItem(it)

        })

        viewModel.liveDataBackStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                (activity as MainActivity).removeFragmentStack()
                (activity as MainActivity).navigationVisible()

            }

        })

        viewModel.liveDataProduct.observe(viewLifecycleOwner, Observer {

            when (it.ticketName) {

                "FM 세트 한 달 이용권" -> {
                    showToast("FM 세트 한 달 이용권")
                }
                "FM 세트 일 년 이용권" -> {
                    showToast("FM 세트 일 년 이용권")
                }
                "어종 확인 한 달 이용권" -> {
                    showToast("어종 확인 한 달 이용권")
                }
                "어종 확인 일 년 이용권" -> {
                    showToast("어종 확인 일 년 이용권")
                }
                "광고 제거 한 달 이용권" -> {
                    showToast("광고 제거 한 달 이용권")
                }
                "광고 제거 일 년 이용권" -> {
                    showToast("광고 제거 일 년 이용권")
                }

            }

        })

        viewModel.liveDataKakaoPayReadyResponse.observe(viewLifecycleOwner, Observer {

            Log.d("PayFragment", "kakaopayreadyresponse : $it")

            binding.payWebView.webViewClient = KakaoPayWebViewClient()
            binding.payWebView.settings.javaScriptEnabled = true
            binding.payWebView.loadUrl(it.next_redirect_mobile_url)

            binding.payMainLayout.visibility = View.GONE
            binding.payWebViewLayout.visibility = View.VISIBLE


        })

        viewModel.liveDataPayApproveStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.payWebViewLayout.visibility = View.GONE
                binding.payMainLayout.visibility = View.VISIBLE
                Toast.makeText(requireActivity(), "결제 성공", Toast.LENGTH_SHORT).show()

            } else {

                binding.payWebViewLayout.visibility = View.GONE
                binding.payMainLayout.visibility = View.VISIBLE
                Toast.makeText(requireActivity(), "결제에 실패하였습니다.", Toast.LENGTH_SHORT).show()

            }

        })


    } // observeLiveData()


    // 토스트 메시지 모듈화
    fun showToast(message : String) {

        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()

    } // showToast()


    // WebViewClient 상속하여 shouldOverrideUrlLoading() 메서드 사용하기 위한 목적의 inner Class
    inner class KakaoPayWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

            val url = request?.url.toString()
            Log.d("TAG", "shouldOverrideUrlLoading: $url")

            if (url.startsWith("intent://")) {
                Log.d("TAG", "shouldOverrideUrlLoading: intent")
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                startActivity(intent)
                return true

            } else if (url.contains("pg_token=")) {
                Log.d("TAG", "shouldOverrideUrlLoading: pgToken")
                val pgToken = url.substringAfter("pg_token=")

                viewModel.updatePgToken(pgToken)

            } else if (url.contains("cancel")) {

                binding.payWebViewLayout.visibility = View.GONE
                binding.payMainLayout.visibility = View.VISIBLE

            } else if (url.contains("fail")) {

                binding.payWebViewLayout.visibility = View.GONE
                binding.payMainLayout.visibility = View.VISIBLE
                Toast.makeText(requireActivity(), "결제에 실패하였습니다.", Toast.LENGTH_SHORT).show()

            }

            view!!.loadUrl(url)

            return false
        }

    } // KakaoPayWebViewClient

}