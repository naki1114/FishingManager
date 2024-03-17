package com.example.fishingmanager.fragment

import android.content.Intent
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
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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

import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
    var modelStatus : Boolean = false

    val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback<ActivityResult>() {

        if (it.resultCode == 819) {

            val intent = it.data

            changeLayout("checkImage")
            resultBitmap = BitmapFactory.decodeFile(intent!!.getStringExtra("image"))
            binding.checkingFishCheckImage.setImageBitmap(resultBitmap)

        }

    })


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
        viewModel.init()

    } // onViewCreated()


    override fun onDestroy() {
        super.onDestroy()

        if (modelStatus) {
            tensorflowModel.closeModel()
        }

    } // onDestroy()


    // 변수 초기화
    fun setVariable() {

        tensorflowModel = TensorflowModel(requireActivity())
        viewModel = CheckingFishViewModel((activity as MainActivity).historyList, (activity as MainActivity).userInfo)

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


    // ViewModel의 LiveData 관찰
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

            if (viewModel.liveDataCheckingFishDayCount.value!! > 0) {
                startCamera()
            } else {
                if (it) {
                    startCamera()
                } else {
                    Toast.makeText(requireActivity(), "금일 이용 횟수를 모두 소진하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

        })

        viewModel.liveDataClassifyStatus.observe(viewLifecycleOwner, Observer {

            if (it) {
                classify()
            }

        })

        viewModel.liveDataClassifyCompleteStatus.observe(viewLifecycleOwner, Observer {

            if (it) {
                changeLayout("complete")
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
                viewModel.saveHistoryServer(
                    getFile(),
                    nickname,
                    result.first,
                    GetDate().getTime().toString()
                )
            }

        })

        viewModel.liveDataChangeFragment.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).changeFragmentWrite(file.toUri().toString())

        })

        viewModel.liveDataCheckingFishDayCount.observe(viewLifecycleOwner, Observer {

            if (it > 0) {
                binding.checkingFishCountText.visibility = View.GONE
            } else {
                binding.checkingFishCountText.visibility = View.VISIBLE
            }

        })


    } // observeLiveData()


    // 파일 만들기
    fun getFile(): MultipartBody.Part {

        val storage = requireActivity().cacheDir
        val fileName = "${GetDate().getTime()}.jpg"
        file = File(storage, fileName)

        file.createNewFile()
        val output = FileOutputStream(file)
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)

        output.close()

        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

        return MultipartBody.Part.createFormData("uploadFile", fileName, requestFile)

    } // getFile()


    // 레이아웃 변환
    fun changeLayout(layout: String) {

        when (layout) {

            "main" -> {
                binding.checkingFishMainLayout.visibility = View.VISIBLE
                binding.checkingFishResultLayout.visibility = View.GONE
                binding.checkingFishDialogLayout.visibility = View.GONE
                binding.checkingFishCheckImageLayout.visibility = View.GONE
                (activity as MainActivity).navigationVisible()
            }

            "checkImage" -> {
                binding.checkingFishMainLayout.visibility = View.GONE
                binding.checkingFishResultLayout.visibility = View.GONE
                binding.checkingFishDialogLayout.visibility = View.GONE
                binding.checkingFishCheckImageLayout.visibility = View.VISIBLE
                (activity as MainActivity).navigationGone()
            }

            "result" -> {
                binding.checkingFishMainLayout.visibility = View.GONE
                binding.checkingFishResultLayout.visibility = View.VISIBLE
                binding.checkingFishDialogLayout.visibility = View.GONE
                binding.checkingFishCheckImageLayout.visibility = View.GONE
                (activity as MainActivity).navigationGone()
            }

            "complete" -> {
                binding.checkingFishMainLayout.visibility = View.GONE
                binding.checkingFishResultLayout.visibility = View.GONE
                binding.checkingFishDialogLayout.visibility = View.VISIBLE
                binding.checkingFishCheckImageLayout.visibility = View.GONE
            }

        }

    } // changeLayout()


    // SharedPreference에서 유저 정보 불러오기
    fun checkUserShared() {

        userInfoShared =
            requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = userInfoShared.getString("nickname", "").toString()

    } // checkUserShared()


    // 카메라 전환
    fun startCamera() {

        TedPermission.create().setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {

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


    // 어종 검출
    fun classify() {

        tensorflowModel.init()
        result = tensorflowModel.classify(resultBitmap)

        changeLayout("result")
        binding.checkingFishResultImage.setImageBitmap(resultBitmap)
        viewModel.getDescription(result.first, result.second)
        modelStatus = true

    } // classify()

}