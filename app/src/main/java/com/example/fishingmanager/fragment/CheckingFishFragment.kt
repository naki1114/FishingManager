package com.example.fishingmanager.fragment

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.CheckingFishHistoryAdapter
import com.example.fishingmanager.databinding.FragmentCheckingFishBinding
import com.example.fishingmanager.function.GetDate
import com.example.fishingmanager.tensorflowModel.TensorflowModel
import com.example.fishingmanager.viewModel.CheckingFishViewModel
import com.websitebeaver.documentscanner.DocumentScanner
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

class CheckingFishFragment : Fragment() {

    val TAG = "CheckingFishFragment"
    lateinit var binding : FragmentCheckingFishBinding

    lateinit var tensorflowModel : TensorflowModel
    lateinit var viewModel : CheckingFishViewModel
    lateinit var adapter : CheckingFishHistoryAdapter

    lateinit var userInfoShared : SharedPreferences
    lateinit var nickname : String

    lateinit var loadingAnimationRight: Animation
    lateinit var loadingAnimationLeft: Animation
    var loadingAnimationStatus = false
    lateinit var animationThread: Thread
    lateinit var resultBitmap : Bitmap
    lateinit var result: Pair<String, Float>
    lateinit var file : File
    lateinit var camera : DocumentScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checking_fish, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUserShared()
        setVariable()


    } // onViewCreated()


    override fun onStart() {
        super.onStart()

        observeLiveData()

    } // onStart()


    fun setVariable() {

        camera = DocumentScanner(
            requireActivity(),
            {
                    croppedImageResults ->
                resultBitmap = BitmapFactory.decodeFile(croppedImageResults.first())
                changeLayout("checkImage")
                binding.checkingFishCheckImage.setImageBitmap(resultBitmap)
            },
            {
                    errorMessage ->
                Log.d(TAG, "cameraError : $errorMessage")
            },
            {
                // 사용자가 카메라 취소했을 때
            }
        )

        tensorflowModel = TensorflowModel(requireActivity())
        viewModel = CheckingFishViewModel((activity as MainActivity).historyList, nickname)
        viewModel.init()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = CheckingFishHistoryAdapter(CheckingFishHistoryAdapter.ItemClickListener {
            viewModel.goPhotoView(it.fishImage)
        })

        binding.checkingFishHistoryRecyclerView.adapter = adapter

        loadingAnimationRight = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_right)
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
                viewModel.saveHistoryServer(getFile(), nickname, result.first, GetDate().getTime().toString())
            }

        })

        viewModel.liveDataSaveAndWriteStatus.observe(viewLifecycleOwner, Observer {

            if (it) {
                viewModel.saveHistoryServerAndChangeFragment(getFile(), nickname, result.first, GetDate().getTime().toString())
            }

        })

        viewModel.liveDataChangeFragment.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).changeFragment(it)

        })


    } // observeLiveData()


    fun getFile() : MultipartBody.Part {

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


    fun changeLayout(layout : String) {

        when(layout) {

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

        userInfoShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = userInfoShared.getString("nickname", "").toString()

    } // checkUserShared()


    fun startCamera() {

        camera.startScan()

    } // startCamera()


    fun classify() {

        tensorflowModel.init()
        result = tensorflowModel.classify(resultBitmap)

        binding.checkingFishCheckLayout.visibility = View.GONE
        binding.checkingFishResultLayout.visibility = View.VISIBLE
        binding.checkingFishResultImage.setImageBitmap(resultBitmap)
        viewModel.getDescription(result.first, result.second)


    } // classify()

}