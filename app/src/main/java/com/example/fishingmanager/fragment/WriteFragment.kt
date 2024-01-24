package com.example.fishingmanager.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.databinding.FragmentWriteBinding
import com.example.fishingmanager.viewModel.WriteViewModel

class WriteFragment : Fragment() {

    private lateinit var writeViewModel : WriteViewModel
    private lateinit var binding : FragmentWriteBinding

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data : Intent? = result.data
                val selectedImageUri = data?.data
                loadImage(selectedImageUri!!)
            }
        }

    // Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        writeViewModel = WriteViewModel()
        binding.viewModel = writeViewModel

        // LiveData Observe
        checkWriteStatus()
        textStatus()
        toGallery()
    }

    // Custom Method

    private fun checkWriteStatus() {

        // isBackLayout Observe
        writeViewModel.isBackLayoutLiveData.observe(viewLifecycleOwner) {

            changeBackLayout(writeViewModel.isBackLayoutLiveData.value.toString())

        } // isBackLayoutLiveData Observe

        writeViewModel.isCheck.observe(viewLifecycleOwner) {

            if (writeViewModel.isCheck.value == true) {

                changeFragment("feed")

            }
            else {

                changeBackLayout("cancel")

            }

        } // isCheck Observe

    } // checkWriteStatus()

    private fun textStatus() {

        // titleStatus Observe
        writeViewModel.titleStatus.observe(viewLifecycleOwner) {

            when (writeViewModel.titleStatus.value) {

                "empty" -> {
                    if (writeViewModel.isBackLayoutLiveData.value == "empty") {
                        Toast.makeText(context, "제목을 입력해 주세요.", Toast.LENGTH_LONG).show()
                    }
                    binding.writeTitleEditText.setTypeface(null, Typeface.BOLD)
                }
                "over" -> {
                    Toast.makeText(context, "제목은 20자까지 가능합니다.", Toast.LENGTH_LONG).show()
                    binding.writeTitleEditText.setTypeface(null, Typeface.NORMAL)
                }
                else -> binding.writeTitleEditText.setTypeface(null, Typeface.NORMAL)

            }

        } // titleStatus Observe

        // contentStatus Observe
        writeViewModel.contentStatus.observe(viewLifecycleOwner) {

            binding.writeLimitCountTextView.text = writeViewModel.content.length.toString() + " / 1000"

            when (writeViewModel.contentStatus.value) {

                "empty" -> {
                    if (writeViewModel.isBackLayoutLiveData.value == "empty") {
                        Toast.makeText(context, "내용을 입력해 주세요.", Toast.LENGTH_LONG).show()
                    }
                    binding.writeContentEditText.setTypeface(null, Typeface.BOLD)
                }
                "over" -> {
                    Toast.makeText(context, "내용은 1000자까지 입력 가능합니다.", Toast.LENGTH_LONG).show()
                    binding.writeContentEditText.setTypeface(null, Typeface.NORMAL)
                }
                else -> binding.writeContentEditText.setTypeface(null, Typeface.NORMAL)

            }

        } // contentStatus Observe

    }

    private fun changeFragment(fragment : String) {

        (activity as MainActivity).changeFragment(fragment)

    } // changeFragment

    private fun changeBackLayout(type : String) {

        when(type) {
            "back" -> {
                binding.writeBackTextView.text = context?.resources?.getString(R.string.write_cancel)
                binding.writeBackLayout.visibility = View.VISIBLE
            }
            "complete" -> {
                writeViewModel.nickname = (activity as MainActivity).nickname
                binding.writeBackTextView.text = context?.resources?.getString(R.string.write_save)
                binding.writeBackLayout.visibility = View.VISIBLE
            }
            else -> {
                binding.writeBackLayout.visibility = View.GONE
            }
        }

    } // changeBackLayout

    private fun toGallery() {

        writeViewModel.toGalleryLiveData.observe(viewLifecycleOwner) {

            if (writeViewModel.toGalleryLiveData.value == true) {

                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                getContent.launch(intent)

            }

        }

    } // toGallery

    private fun loadImage(imageUri : Uri) {

        if (imageUri != null) {
            Glide.with(requireContext())
                .load(imageUri)
                .into(binding.writeImageView)
        }

    } // loadImage

}