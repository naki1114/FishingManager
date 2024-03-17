package com.example.fishingmanager.fragment

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.data.Feed
import com.example.fishingmanager.databinding.FragmentWriteBinding
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.viewModel.WriteViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.Serializable

class WriteFragment : Fragment() {

    private lateinit var writeViewModel: WriteViewModel
    private lateinit var binding: FragmentWriteBinding
    private lateinit var nickname: String
    private lateinit var body: MultipartBody.Part

    private var galleryCheck = false

    private var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {

            val imagePath = result.data!!.data
            val fileName = GetDate().getTime().toString() + ".jpg"
            val file = File(absolutelyPath(imagePath))
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

            body = MultipartBody.Part.createFormData("uploadFile", fileName, requestFile)
            galleryCheck = true

            loadImage(result.data!!.data!!)
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        nickname = (activity as MainActivity).nickname
        writeViewModel = WriteViewModel(nickname)
        binding.viewModel = writeViewModel

        getFragmentChangeBundle()
        // LiveData Observe
        checkBack()
        checkSave()
        feedStatus()
        toGallery()

    } // onViewCreated()


    fun getFragmentChangeBundle() {

        parentFragmentManager.setFragmentResultListener("write", this) { key, bundle ->

            val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                bundle.getSerializable("write", File::class.java)

            } else {

                bundle.getSerializable("write") as File

            }

            if (file != null) {

                Glide.with(requireActivity()).load(file).into(binding.writeImageView)
                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                body = MultipartBody.Part.createFormData("uploadFile", GetDate().getTime().toString() + ".jpg", requestFile)
                galleryCheck = true

            }

        }

    } // getFragmentChangeBundle()


    // 뒤로 가기 관련 observe
    private fun checkBack() {

        writeViewModel.doBackLayout.observe(viewLifecycleOwner) {

            if (it) {

                binding.writeBackLayout.visibility = View.VISIBLE

            } else {

                binding.writeBackLayout.visibility = View.GONE

            }

        }

        writeViewModel.isBack.observe(viewLifecycleOwner) {

            if (it) {

                binding.writeTitleEditText.text = null
                binding.writeContentEditText.text = null
                toFeedFragment()

            }

        }

    } // checkBack()


    // 게시글 저장 관련 observe
    private fun checkSave() {

        writeViewModel.doSaveLayout.observe(viewLifecycleOwner) {

            if (it) {

                binding.writeSaveLayout.visibility = View.VISIBLE

            }

        }

        writeViewModel.isRequestSave.observe(viewLifecycleOwner) {

            if (it) {

                if (galleryCheck) {

                    writeViewModel.insertImageFeed(body)
                    galleryCheck = false

                } else {
                    writeViewModel.insertFeed()
                }

            }

        }

        writeViewModel.isSaved.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.writeImageView.setImageResource(R.drawable.btnimg)
                binding.writeTitleEditText.text = null
                binding.writeContentEditText.text = null

                binding.writeSaveLayout.visibility = View.GONE

                toFeedFragment()

            }

        })

    } // checkSave()


    // 게시글 제목, 내용의 길이에 따른 상태
    private fun feedStatus() {

        writeViewModel.feedStatus.observe(viewLifecycleOwner) {

            when (it) {

                "titleEmpty" -> Toast.makeText(context, "제목을 입력해 주세요.", Toast.LENGTH_LONG).show()
                "titleOver" -> Toast.makeText(context, "제목은 20자까지 입력 가능합니다.", Toast.LENGTH_LONG).show()
                "contentEmpty" -> Toast.makeText(context, "내용을 입력해 주세요.", Toast.LENGTH_LONG).show()
                "contentOver" -> Toast.makeText(context, "내용은 1000자까지 입력 가능합니다.", Toast.LENGTH_LONG).show()

            }

        }

        writeViewModel.contentLiveData.observe(viewLifecycleOwner) {

            binding.writeLimitCountTextView.text = it.length.toString() + " / 1000"

        }

    } // feedStatus()


    // 프래그먼트 전환
    private fun toFeedFragment() {

        (activity as MainActivity).changeFragmentFeedNavigation()

    } // toFeedFragment()


    // 갤러리로 이동
    private fun toGallery() {

        writeViewModel.toGalleryLiveData.observe(viewLifecycleOwner) {

            if (it) {

                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                intent.type = "image/*"
                chooserIntent.putExtra(Intent.EXTRA_INTENT, intent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "사용할 앱을 선택해주세요.")
                launcher.launch(chooserIntent)

            }

        }

    } // toGallery()


    // 이미지 절대 경로
    private fun absolutelyPath(path: Uri?): String {

        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireContext().contentResolver.query(path!!, proj, null, null, null)
        val index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        cursor?.moveToFirst()

        val result = cursor?.getString(index!!)

        return result!!

    } // absolutelyPath()


    // Glide) ImageView에 이미지 띄우기
    private fun loadImage(imageUri: Uri) {

        Glide.with(requireContext())
            .load(imageUri)
            .into(binding.writeImageView)

    } // loadImage()


}