package com.example.fishingmanager.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.Guidelines
import com.canhub.cropper.CropImageView.Guidelines.*
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.CameraActivity
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.CheckingFishHistoryAdapter
import com.example.fishingmanager.databinding.FragmentCheckingFishBinding
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.tensorflowModel.TensorflowModel
import com.example.fishingmanager.viewModel.CheckingFishViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.websitebeaver.documentscanner.DocumentScanner
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

class CheckingFishFragment : Fragment() {

    val TAG = "CheckingFishFragment"
    lateinit var binding: FragmentCheckingFishBinding

    lateinit var tensorflowModel: TensorflowModel
    lateinit var viewModel: CheckingFishViewModel
    lateinit var adapter: CheckingFishHistoryAdapter

    lateinit var userInfoShared: SharedPreferences
    lateinit var nickname: String

    lateinit var loadingAnimationRight: Animation
    lateinit var loadingAnimationLeft: Animation
    var loadingAnimationStatus = false
    lateinit var animationThread: Thread
    lateinit var resultBitmap: Bitmap
    lateinit var result: Pair<String, Float>
    lateinit var file: File

    val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback<ActivityResult>() {

        if (it.resultCode == 819) {

            val intent = it.data
            Log.d(TAG, "${it.resultCode}")
            Log.d(TAG, "${intent!!.getStringExtra("image")}")
//            binding.checkingFishCameraButton.setImageBitmap(BitmapFactory.decodeFile(intent!!.getStringExtra("image")))
            binding.checkingFishCheckImageLayout.visibility = View.VISIBLE
            binding.checkingFishMainLayout.visibility = View.GONE
            resultBitmap = BitmapFactory.decodeFile(intent!!.getStringExtra("image"))
            binding.checkingFishCheckImage.setImageBitmap(resultBitmap)

        }

    })

    lateinit var captureUri : Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_checking_fish, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUserShared()
        setVariable()
        observeLiveData()


    } // onViewCreated()


    fun setVariable() {

        tensorflowModel = TensorflowModel(requireActivity())
        viewModel = CheckingFishViewModel((activity as MainActivity).historyList, nickname)
        viewModel.init()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = CheckingFishHistoryAdapter(CheckingFishHistoryAdapter.ItemClickListener {
            viewModel.goPhotoView(it.fishImage)
        })

        binding.checkingFishHistoryRecyclerView.adapter = adapter

        loadingAnimationRight =
            AnimationUtils.loadAnimation(activity, R.anim.loading_animation_right)
        loadingAnimationLeft = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_left)

    } // setVariable()


    fun observeLiveData() {

        viewModel.liveDataHistoryList.observe(viewLifecycleOwner, Observer {

            if (viewModel.liveDataBasicHistoryList.value?.size == 0) {
                binding.checkingFishMainLayout.visibility = View.GONE
                binding.checkingFishResponseFailureLayout.visibility = View.VISIBLE
            } else {
                adapter.setItem(it)
            }

        })

        viewModel.liveDataClickedFishImage.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).goPhotoView(it)

        })

        viewModel.liveDataChangeLayout.observe(viewLifecycleOwner, Observer {

            changeLayout(it)

        })

        viewModel.liveDataBasicCollectionList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).collectionList = it

        })

        viewModel.liveDataBasicHistoryList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).historyList = it

        })

        viewModel.liveDataBasicFeedList.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).feedList = it

        })

        viewModel.liveDataBasicUserInfo.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).userInfo = it

        })

        viewModel.liveDataLoadingStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.checkingFishResponseFailureLayout.visibility = View.GONE
                binding.checkingFishLoadingLayout.visibility = View.VISIBLE

                binding.checkingFishLoadingRightImage.visibility = View.VISIBLE
                binding.checkingFishLoadingRightImage.startAnimation(loadingAnimationRight)
                loadingAnimationStatus = true

                animationThread = thread {

                    try {

                        while (loadingAnimationStatus) {

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.checkingFishLoadingRightImage.visibility = View.GONE
                                binding.checkingFishLoadingLeftImage.visibility = View.VISIBLE
                                binding.checkingFishLoadingLeftImage.startAnimation(
                                    loadingAnimationLeft
                                )

                            }

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.checkingFishLoadingLeftImage.visibility = View.GONE
                                binding.checkingFishLoadingRightImage.visibility = View.VISIBLE
                                binding.checkingFishLoadingRightImage.startAnimation(
                                    loadingAnimationRight
                                )

                            }

                        }

                    } catch (e: InterruptedException) {

                        loadingAnimationStatus = false

                        Handler(Looper.getMainLooper()).post {

                            binding.checkingFishLoadingRightImage.clearAnimation()
                            binding.checkingFishLoadingLeftImage.clearAnimation()
                            binding.checkingFishLoadingRightImage.visibility = View.GONE
                            binding.checkingFishLoadingLeftImage.visibility = View.GONE
                            binding.checkingFishLoadingLayout.visibility = View.GONE

                            if (viewModel.liveDataBasicHistoryList.value?.size == 0) {

                                binding.checkingFishResponseFailureLayout.visibility = View.VISIBLE

                            } else {

                                binding.checkingFishMainLayout.visibility = View.VISIBLE

                            }

                        }

                    }

                }

            } else {

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

            }

        })

        viewModel.liveDataCameraStatus.observe(viewLifecycleOwner, Observer {

            if (it) {

                startCamera()

            }

        })

        viewModel.liveDataClassifyStatus.observe(viewLifecycleOwner, Observer {

            if (it) {
                classify()
            }

        })

        viewModel.liveDataClassifyCompleteStatus.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.checkingFishDialogLayout.visibility = View.VISIBLE
            }

        })

        viewModel.liveDataCheckingFish.observe(viewLifecycleOwner, Observer {

            binding.checkingFish = it

        })

        viewModel.liveDataSaveStatus.observe(viewLifecycleOwner, Observer {

            if (it) {
                viewModel.saveHistoryServer(
                    getFile(),
                    nickname,
                    result.first,
                    GetDate().getTime().toString()
                )
            }

        })

        viewModel.liveDataSaveAndWriteStatus.observe(viewLifecycleOwner, Observer {

            if (it) {
                viewModel.saveHistoryServerAndChangeFragment(
                    getFile(),
                    nickname,
                    result.first,
                    GetDate().getTime().toString()
                )
            }

        })

        viewModel.liveDataChangeFragment.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).changeFragment(it)

        })


    } // observeLiveData()


    fun getFile(): MultipartBody.Part {

        val storage = requireActivity().cacheDir
        val fileName = nickname + GetDate().getTime() + "jpg"
        file = File(storage, fileName)

        file.createNewFile()
        val output = FileOutputStream(file)
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)

        output.close()

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("uploadFile", fileName, requestFile)

        return body

    } // getFile()


    fun changeLayout(layout: String) {

        when (layout) {

            "main" -> {
                binding.checkingFishMainLayout.visibility = View.VISIBLE
                binding.checkingFishResultLayout.visibility = View.GONE
                binding.checkingFishDialogLayout.visibility = View.GONE
                binding.checkingFishCheckImageLayout.visibility = View.GONE
            }

            "checkImage" -> {
                binding.checkingFishMainLayout.visibility = View.GONE
                binding.checkingFishResultLayout.visibility = View.GONE
                binding.checkingFishDialogLayout.visibility = View.GONE
                binding.checkingFishCheckImageLayout.visibility = View.VISIBLE
            }

            "result" -> {
                binding.checkingFishMainLayout.visibility = View.GONE
                binding.checkingFishResultLayout.visibility = View.VISIBLE
                binding.checkingFishDialogLayout.visibility = View.GONE
                binding.checkingFishCheckImageLayout.visibility = View.GONE
            }

            "complete" -> {
                binding.checkingFishMainLayout.visibility = View.GONE
                binding.checkingFishResultLayout.visibility = View.GONE
                binding.checkingFishDialogLayout.visibility = View.VISIBLE
                binding.checkingFishCheckImageLayout.visibility = View.GONE
            }

        }

    } // changeLayout()


    fun checkUserShared() {

        userInfoShared =
            requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = userInfoShared.getString("nickname", "").toString()

    } // checkUserShared()


    fun startCamera() {

        TedPermission.create().setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {

//                val fileName = nickname + System.currentTimeMillis()
//                val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                val file = File.createTempFile(fileName, ".jpg", storageDir)
//
//                captureUri = FileProvider.getUriForFile(requireActivity(), requireActivity().packageName+".fileprovider", file)
//
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                launcher.launch(intent)

                val intent = Intent(requireActivity(), CameraActivity::class.java)
                launcher.launch(intent)

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireActivity(), "권한을 허용해 주세요.", Toast.LENGTH_SHORT).show()
                for (i in 0 until deniedPermissions!!.size) {
                    Log.d(TAG, "onPermissionDenied: ${deniedPermissions[i]}")
                }
            }
        }).setDeniedMessage("권한을 허용해 주세요.").setPermissions(android.Manifest.permission.CAMERA)
            .check()


    } // startCamera()


    fun classify() {

        tensorflowModel.init()
        result = tensorflowModel.classify(resultBitmap)

        binding.checkingFishCheckImageLayout.visibility = View.GONE
        binding.checkingFishResultLayout.visibility = View.VISIBLE
        binding.checkingFishResultImage.setImageBitmap(resultBitmap)
        viewModel.getDescription(result.first, result.second)


    } // classify()


    override fun onDestroy() {
        super.onDestroy()

        tensorflowModel.closeModel()

    }

}