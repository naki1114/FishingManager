package com.example.fishingmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.fishingmanager.R
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


    fun setVariable() {

        viewModel = PayViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = PayTicketAdapter(PayTicketAdapter.ItemClickListener {
            viewModel.startPayStep(it)
        })
        binding.payRecyclerView.adapter = adapter

    } // setVariable()


    fun observeLiveData() {

        viewModel.liveDataTicketList.observe(viewLifecycleOwner, Observer {

            adapter.setItem(it)

        })

        viewModel.liveDataBackStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                requireActivity().finish()

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

    } // observeLiveData()


    fun showToast(message : String) {

        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()

    } // showToast()

}