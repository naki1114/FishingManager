package com.example.fishingmanager.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
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
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.viewModel.WriteViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class WriteFragment : Fragment() {

    private lateinit var writeViewModel : WriteViewModel
    private lateinit var binding : FragmentWriteBinding
    private lateinit var nickname : String
    private lateinit var body : MultipartBody.Part

    private var galleryCheck = false

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result : ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {

            val imagePath = result.data!!.data
            val fileName = GetDate().getTime().toString() + ".jpg"
            val file = File(absolutelyPath(imagePath, requireContext()))
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            body = MultipartBody.Part.createFormData("uploadFile", fileName, requestFile)

            galleryCheck = true

            loadImage(result.data!!.data!!)
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

        nickname = (activity as MainActivity).nickname
        writeViewModel = WriteViewModel(nickname)
        binding.viewModel = writeViewModel

        // LiveData Observe
        checkBack()
        checkSave()
        feedStatus()
        toGallery()
    }

    // Custom Method

    private fun checkBack() {

        writeViewModel.doBackLayout.observe(viewLifecycleOwner) {

            if (writeViewModel.doBackLayout.value == true) {

                binding.writeBackLayout.visibility = View.VISIBLE

            }
            else {

                binding.writeBackLayout.visibility = View.GONE
                writeViewModel.isBack.observe(viewLifecycleOwner) {

                    if (writeViewModel.isBack.value == true) {

                        binding.writeTitleEditText.text = null
                        binding.writeContentEditText.text = null
                        changeFragment("feed")

                    }

                }

            }

        }

    } // checkBack

    private fun checkSave() {

        writeViewModel.doSaveLayout.observe(viewLifecycleOwner) {

            if (writeViewModel.doSaveLayout.value == true) {

                binding.writeSaveLayout.visibility = View.VISIBLE

            }
            else {

                writeViewModel.isSave.observe(viewLifecycleOwner) {

                    if (writeViewModel.isSave.value == true) {

                        if (galleryCheck) {
                            writeViewModel.insertImageFeed(body)
                            galleryCheck = false
                        }
                        else {
                            writeViewModel.insertFeed()
                        }

                        binding.writeImageView.setImageResource(R.drawable.btnimg)
                        binding.writeTitleEditText.text = null
                        binding.writeContentEditText.text = null

                        binding.writeSaveLayout.visibility = View.GONE

                        changeFragment("feed")

                    }

                }

            }

        }

    }

    private fun feedStatus() {

        writeViewModel.feedStatus.observe(viewLifecycleOwner) {

            when (writeViewModel.feedStatus.value) {
                "titleEmpty" -> {
                    Toast.makeText(context, "제목을 입력해 주세요.", Toast.LENGTH_LONG).show()
                }
                "titleOver" -> {
                    Toast.makeText(context, "제목은 20자까지 입력 가능합니다.", Toast.LENGTH_LONG).show()
                }
                "contentEmpty" -> {
                    Toast.makeText(context, "내용을 입력해 주세요.", Toast.LENGTH_LONG).show()
                }
                "contentOver" -> {
                    Toast.makeText(context, "내용은 1000자까지 입력 가능합니다.", Toast.LENGTH_LONG).show()
                }
            }

        }

    } // feedStatus

    private fun changeFragment(fragment : String) {

        (activity as MainActivity).changeFragment(fragment)

    } // changeFragment

    private fun toGallery() {

        writeViewModel.toGalleryLiveData.observe(viewLifecycleOwner) {

            if (writeViewModel.toGalleryLiveData.value == true) {

                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                intent.type = "image/*"
                chooserIntent.putExtra(Intent.EXTRA_INTENT, intent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "사용할 앱을 선택해주세요.")
                launcher.launch(chooserIntent)

            }

        }

    } // toGallery

    private fun absolutelyPath(path : Uri?, context : Context) : String {

        var proj : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var cursor : Cursor? = requireContext().contentResolver.query(path!!, proj, null, null, null)
        var index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        cursor?.moveToFirst()

        var result = cursor?.getString(index!!)

        return result!!

    } // absolutelyPath

    private fun loadImage(imageUri : Uri) {

        if (imageUri != null) {
            Glide.with(requireContext())
                .load(imageUri)
                .into(binding.writeImageView)
        }

    } // loadImage

}