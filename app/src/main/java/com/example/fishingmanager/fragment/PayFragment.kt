package com.example.fishingmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        viewModel.liveDataBackStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                requireActivity().finish()

            }

        })

        viewModel.liveDataProduct.observe(viewLifecycleOwner, Observer {

            // 티켓 종류 구분하여 결제 진행 추가

        })

    } // observeLiveData()

}