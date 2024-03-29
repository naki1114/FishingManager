package com.example.fishingmanager.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.databinding.FragmentPhotoViewBinding
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.viewModel.PhotoViewViewModel

class PhotoViewFragment: Fragment() {

    lateinit var binding: FragmentPhotoViewBinding
    lateinit var viewModel: PhotoViewViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                (activity as MainActivity).removeFragmentStack()
                (activity as MainActivity).navigationVisible()

            }

        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_view, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setVariable()
        observeLiveData()

    } // onViewCreated()


    // 초기화
    fun setVariable() {

        viewModel = PhotoViewViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        parentFragmentManager.setFragmentResultListener("image", this) { key, bundle ->

            viewModel.setImage(bundle.getString("image")!!)

        }

    } // setVariable()


    // LiveData observe
    private fun observeLiveData() {

        viewModel.liveDataCloseStatus.observe(viewLifecycleOwner) {

            if (it) {

                (activity as MainActivity).onBackPressed()

            }

        }

        viewModel.liveDataImage.observe(viewLifecycleOwner) {

            if (it == "FM") {

                Glide.with((activity as MainActivity)).load(R.drawable.fishing_logo).into(binding.photoViewImage)

            } else {

                Glide.with((activity as MainActivity)).load(RetrofitClient.BASE_URL + it).into(binding.photoViewImage)

            }

        }

    } // observeLiveData()


}