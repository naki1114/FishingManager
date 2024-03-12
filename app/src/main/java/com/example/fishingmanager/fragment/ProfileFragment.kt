package com.example.fishingmanager.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.adapter.ProfileCollectionAdapter
import com.example.fishingmanager.adapter.ProfileHistoryAdapter
import com.example.fishingmanager.adapter.ProfileSelectFishAdapter
import com.example.fishingmanager.data.UserInfo
import com.example.fishingmanager.databinding.FragmentProfileBinding
import com.example.fishingmanager.network.RetrofitClient
import com.example.fishingmanager.viewModel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.lang.StringBuilder
import kotlin.concurrent.thread

class ProfileFragment : Fragment() {

    val TAG = "ProfileFragment"

    lateinit var binding : FragmentProfileBinding

    lateinit var viewModel : ProfileViewModel
    lateinit var collectionAdapter : ProfileCollectionAdapter
    lateinit var historyAdapter : ProfileHistoryAdapter
    lateinit var selectFishAdapter : ProfileSelectFishAdapter
    lateinit var launcher : ActivityResultLauncher<Intent>
    lateinit var userInfo : UserInfo
    lateinit var nickname : String

    lateinit var loadingAnimationRight: Animation
    lateinit var loadingAnimationLeft: Animation
    var loadingAnimationStatus = false
    lateinit var animationThread: Thread

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUserShared()
        setVariable()
        observeLiveData()
        getData()

    } // onViewCreated()


    fun setVariable() {

        userInfo = (activity as MainActivity).userInfo
        viewModel = ProfileViewModel((activity as MainActivity).collectionList, (activity as MainActivity).historyList, userInfo, nickname)
        binding.viewModel = viewModel
        binding.userInfo = userInfo
        binding.lifecycleOwner = this

        collectionAdapter = ProfileCollectionAdapter(ProfileCollectionAdapter.ItemClickListener {
            viewModel.readMoreFish(it)
        })
        historyAdapter = ProfileHistoryAdapter()
        selectFishAdapter = ProfileSelectFishAdapter(ProfileSelectFishAdapter.ItemClickListener {
            viewModel.changeFish(it.fishName)
        })

        binding.profileCollectionRecyclerView.adapter =  collectionAdapter
        binding.profileHistoryRecyclerView.adapter = historyAdapter
        binding.profileSelectFishRecyclerView.adapter = selectFishAdapter

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback<ActivityResult> {

            if (it.resultCode == -1) {

                val intent = it.data
                val uri = intent!!.data

                Glide.with(requireActivity()).load(uri).into(binding.profileProfileImage)

            }

        })

        loadingAnimationRight = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_right)
        loadingAnimationLeft = AnimationUtils.loadAnimation(activity, R.anim.loading_animation_left)

    } // setVariable()


    fun setCalendarView(list : ArrayList<CalendarDay>) {

        binding.profileCalendarView.setTitleFormatter(
            MonthArrayTitleFormatter(
                resources.getTextArray(
                    R.array.custom_months
                )
            )
        )
        binding.profileCalendarView.setWeekDayFormatter(
            ArrayWeekDayFormatter(
                resources.getTextArray(
                    R.array.custom_weekdays
                )
            )
        )

        binding.profileCalendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)
        binding.profileCalendarView.addDecorators(
            TodayDecorator(requireActivity()),
            SundayDecorator(),
            SaturdayDecorator(),
            SelectableDecorator(list)
        )

        binding.profileCalendarView.setTitleFormatter(object : TitleFormatter {
            override fun format(day: CalendarDay?): CharSequence {

                val inputText: org.threeten.bp.LocalDate = day!!.date
                val calendarHeaderElements = inputText.toString().split("-")
                val calendarHeaderBuilder = StringBuilder()
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                    .append("   ")
                    .append(calendarHeaderElements[1])

                return calendarHeaderBuilder.toString()

            }

        })

        binding.profileSelectDateChoiceButton.setOnClickListener {

            if (binding.profileCalendarView.selectedDate != null) {
                viewModel.changeDate(binding.profileCalendarView.selectedDate!!.date.toString())
            }

        }

        binding.profileSelectDateAllButton.setOnClickListener {

            viewModel.changeDate("전 체")

        }

    } // setView()


    fun observeLiveData() {

        viewModel.liveDataUserInfo.observe(viewLifecycleOwner, Observer {

            if (it.profileImage == "FM") {
                binding.profileProfileImage.setImageResource(R.drawable.fishing_logo)
            } else {
                Glide.with(requireActivity()).load(RetrofitClient.BASE_URL + it.profileImage).into(binding.profileProfileImage)
            }

        })

        viewModel.liveDataCollectionList.observe(viewLifecycleOwner, Observer {

            if (viewModel.liveDataBasicCollectionList.value?.size == 0) {
                binding.profileCollectionLayout.visibility = View.GONE
                binding.profileResponseFailureLayout.visibility = View.VISIBLE
                Log.d("API테스트", "Collection 데이터 없음")
            } else {
                collectionAdapter.setItem(it)
                Log.d("API테스트", "Collection 데이터 있음")
            }

        })

        viewModel.liveDataHistoryList.observe(viewLifecycleOwner, Observer {

            if (viewModel.liveDataBasicHistoryList.value?.size == 0) {
                binding.profileHistoryLayout.visibility = View.GONE
                binding.profileResponseFailureLayout.visibility = View.VISIBLE
                Log.d("API테스트", "History 데이터 없음")
            } else {
                historyAdapter.setItem(it)
                Log.d("API테스트", "History 데이터 있음")
            }

        })

        viewModel.liveDataFishList.observe(viewLifecycleOwner, Observer {

            selectFishAdapter.setItem(it)

        })

        viewModel.liveDataReadMoreFish.observe(viewLifecycleOwner, Observer {

            binding.collection = it
            Glide.with(requireActivity()).load(it.fishImage).into(binding.profileCollectionReadMoreImage)

        })

        viewModel.liveDataChangeTab.observe(viewLifecycleOwner, Observer {

            changeTab(it)

        })

        viewModel.liveDataChangeLayout.observe(viewLifecycleOwner, Observer {

            changeLayout(it)

        })

        viewModel.liveDataClickedMenu.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.profileDrawerLayout.openDrawer(binding.drawerView)
            }

        })

        viewModel.liveDataChangeFragment.observe(viewLifecycleOwner, Observer {

            when (it) {
                "start" -> removeUserShared()
            }

            (activity as MainActivity).changeFragment(it)

        })

        viewModel.liveDataCalendarList.observe(viewLifecycleOwner, Observer {

            setCalendarView(it)

        })

        viewModel.liveDataShowDialog.observe(viewLifecycleOwner, Observer {

            when (it) {

                "logout" -> {
                    binding.profileLogoutLayout.visibility = View.VISIBLE
                }
                "deleteAccount" -> {
                    binding.profileDeleteAccountLayout.visibility = View.VISIBLE
                }

            }

        })

        viewModel.liveDataLogoutStatus.observe(viewLifecycleOwner, Observer {

            binding.profileLogoutLayout.visibility = View.GONE

            if (it) {
                when (getUserType()) {

                    "google" -> {

                        FirebaseAuth.getInstance().signOut()
                        val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        val client = GoogleSignIn.getClient(requireActivity(), opt)
                        client.signOut()

                    }

                    "kakao" -> {

                        UserApiClient.instance.logout { error ->

                            if (error != null) {
                                Log.d(TAG, "Failed Logout")
                            }else {
                                Log.d(TAG, "Success Logout")
                            }

                        }

                    }

                    "naver" -> {

                        NaverIdLoginSDK.logout()

                    }

                }

                viewModel.changeFragment("start")
            }

        })

        viewModel.liveDataDeleteAccountStatus.observe(viewLifecycleOwner, Observer {

            binding.profileDeleteAccountLayout.visibility = View.GONE

            if (it) {
                when (getUserType()) {

                    "google" -> {

                        FirebaseAuth.getInstance().signOut()
                        val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                        val client = GoogleSignIn.getClient(requireActivity(), opt)
                        client.signOut()
                        client.revokeAccess()

                    }

                    "kakao" -> {

                        UserApiClient.instance.unlink { error ->

                            if (error != null) {
                                Log.d(TAG, "Failed DeleteAccount")
                            }else {
                                Log.d(TAG, "Success DeleteAccount")
                            }

                        }

                    }

                    "naver" -> {

                        NidOAuthLogin().callDeleteTokenApi(requireContext(), object :
                            OAuthLoginCallback {

                            override fun onSuccess() {
                                Log.d(TAG, "토큰 삭제 성공")
                            }

                            override fun onFailure(httpStatus: Int, message: String) {
                                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                                Log.d(TAG, "errorCode : $errorCode")
                                Log.d(TAG, "errorDesc : $errorDescription")
                            }

                            override fun onError(errorCode: Int, message: String) {
                                onFailure(errorCode, message)
                            }

                        })

                    }

                }

                viewModel.deleteAccount(nickname, getUserType())
            }

        })

        viewModel.liveDataClickedFishImage.observe(viewLifecycleOwner, Observer {

            (activity as MainActivity).goPhotoView(it.profileImage)

        })

        viewModel.liveDataGoToGallery.observe(viewLifecycleOwner, Observer {

            if (it) {

                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                launcher.launch(intent)

            }

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

                binding.profileProfileImage.isClickable = false
                binding.profileEditProfileImage.isClickable = false
                binding.profileCollectionButton.isClickable = false
                binding.profileHistoryButton.isClickable = false
                binding.profileMenuButton.isClickable = false

                binding.profileResponseFailureLayout.visibility = View.GONE
                binding.profileLoadingLayout.visibility = View.VISIBLE

                binding.profileLoadingRightImage.visibility = View.VISIBLE
                binding.profileLoadingRightImage.startAnimation(loadingAnimationRight)
                loadingAnimationStatus = true

                animationThread = thread {

                    try {

                        while (loadingAnimationStatus) {

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.profileLoadingRightImage.visibility = View.GONE
                                binding.profileLoadingLeftImage.visibility = View.VISIBLE
                                binding.profileLoadingLeftImage.startAnimation(
                                    loadingAnimationLeft
                                )

                            }

                            Thread.sleep(1000)

                            Handler(Looper.getMainLooper()).post {

                                binding.profileLoadingLeftImage.visibility = View.GONE
                                binding.profileLoadingRightImage.visibility = View.VISIBLE
                                binding.profileLoadingRightImage.startAnimation(
                                    loadingAnimationRight
                                )

                            }

                        }

                    } catch (e: InterruptedException) {

                        loadingAnimationStatus = false

                        Handler(Looper.getMainLooper()).post {

                            binding.profileLoadingRightImage.clearAnimation()
                            binding.profileLoadingLeftImage.clearAnimation()
                            binding.profileLoadingRightImage.visibility = View.GONE
                            binding.profileLoadingLeftImage.visibility = View.GONE
                            binding.profileLoadingLayout.visibility = View.GONE

                        }
                    }
                }

            } else {

                binding.profileProfileImage.isClickable = true
                binding.profileEditProfileImage.isClickable = true
                binding.profileCollectionButton.isClickable = true
                binding.profileHistoryButton.isClickable = true
                binding.profileMenuButton.isClickable = true

                if (animationThread.isAlive) {
                    animationThread.interrupt()
                }

                if (binding.profileCollectionBlock.visibility == View.VISIBLE) {

                    if (viewModel.liveDataBasicCollectionList.value?.size == 0) {
                        binding.profileCollectionLayout.visibility = View.GONE
                        binding.profileResponseFailureLayout.visibility = View.VISIBLE
                    } else {
                        binding.profileCollectionLayout.visibility = View.VISIBLE
                    }

                } else if (binding.profileHistoryBlock.visibility == View.VISIBLE) {

                    if (viewModel.liveDataBasicHistoryList.value?.size == 0) {
                        binding.profileHistoryLayout.visibility = View.GONE
                        binding.profileResponseFailureLayout.visibility = View.VISIBLE
                    } else {
                        binding.profileHistoryLayout.visibility = View.VISIBLE
                    }

                }

            }

        })


    } // observeLiveData()


    fun getData() {

        viewModel.init()

    } // getData()


    fun removeUserShared() {

        val userInfoShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        val editor = userInfoShared.edit()
        editor.clear()
        editor.commit()

    } // removeUserShared()

    fun checkUserShared() {

        val userInfoShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        nickname = userInfoShared.getString("nickname", "").toString()

    } // checkUserShared()


    fun changeTab(tab : String) {

        when(tab) {

            "collection" -> {

                if (viewModel.liveDataBasicCollectionList.value?.size == 0) {

                    binding.profileCollectionLayout.visibility = View.GONE
                    binding.profileResponseFailureLayout.visibility = View.VISIBLE

                } else {
                    binding.profileCollectionLayout.visibility = View.VISIBLE
                }

                binding.profileHistoryLayout.visibility = View.GONE
                binding.profileCollectionButton.visibility = View.GONE
                binding.profileCollectionBlock.visibility = View.VISIBLE
                binding.profileHistoryButton.visibility = View.VISIBLE
                binding.profileHistoryBlock.visibility = View.GONE
            }

            "history" -> {

                if (viewModel.liveDataBasicHistoryList.value?.size == 0) {

                    binding.profileHistoryLayout.visibility = View.GONE
                    binding.profileResponseFailureLayout.visibility = View.VISIBLE

                } else {
                    binding.profileHistoryLayout.visibility = View.VISIBLE
                }

                binding.profileCollectionLayout.visibility = View.GONE
                binding.profileCollectionButton.visibility = View.VISIBLE
                binding.profileCollectionBlock.visibility = View.GONE
                binding.profileHistoryButton.visibility = View.GONE
                binding.profileHistoryBlock.visibility = View.VISIBLE
            }

        }

    } // changeTab()


    fun changeLayout(layout : String) {

        when(layout) {

            "main" -> {
                binding.profileMainLayout.visibility = View.VISIBLE
                binding.profileCollectionReedMoreLayout.visibility = View.GONE
                binding.profileSelectFishLayout.visibility = View.GONE
                binding.profileSelectLengthLayout.visibility = View.GONE
                binding.profileSelectDateLayout.visibility = View.GONE
                (activity as MainActivity).navigationVisible()
            }

            "readMore" -> {
                binding.profileMainLayout.visibility = View.GONE
                binding.profileCollectionReedMoreLayout.visibility = View.VISIBLE
                binding.profileSelectFishLayout.visibility = View.GONE
                binding.profileSelectLengthLayout.visibility = View.GONE
                binding.profileSelectDateLayout.visibility = View.GONE
                (activity as MainActivity).navigationGone()
            }

            "selectFish" -> {
                binding.profileMainLayout.visibility = View.GONE
                binding.profileCollectionReedMoreLayout.visibility = View.GONE
                binding.profileSelectFishLayout.visibility = View.VISIBLE
                binding.profileSelectLengthLayout.visibility = View.GONE
                binding.profileSelectDateLayout.visibility = View.GONE
                (activity as MainActivity).navigationGone()
            }

            "selectLength" -> {
                binding.profileMainLayout.visibility = View.GONE
                binding.profileCollectionReedMoreLayout.visibility = View.GONE
                binding.profileSelectFishLayout.visibility = View.GONE
                binding.profileSelectLengthLayout.visibility = View.VISIBLE
                binding.profileSelectDateLayout.visibility = View.GONE
                (activity as MainActivity).navigationGone()
            }

            "selectDate" -> {
                binding.profileMainLayout.visibility = View.GONE
                binding.profileCollectionReedMoreLayout.visibility = View.GONE
                binding.profileSelectFishLayout.visibility = View.GONE
                binding.profileSelectLengthLayout.visibility = View.GONE
                binding.profileSelectDateLayout.visibility = View.VISIBLE
                (activity as MainActivity).navigationGone()
            }

        }

    } // changeLayout()

    private fun getUserType() : String {

        val sharedPreferences = activity?.getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        var type = sharedPreferences?.getString("type", "")

        return type!!

    } // getUserType


    class TodayDecorator(context: Context) : DayViewDecorator {

        val drawable: Drawable =
            ContextCompat.getDrawable(context, R.drawable.calendar_selector_color)!!

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return true
        }


        override fun decorate(view: DayViewFacade?) {
            view!!.setSelectionDrawable(drawable)
        }

    } // DayDecorator


    class SundayDecorator : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            val sunday = day?.date?.with(org.threeten.bp.DayOfWeek.SUNDAY)?.dayOfMonth
            return sunday == day?.day
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.RED) {})
        }

    } // SundayDecorator


    class SaturdayDecorator : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            val saturday = day?.date?.with(org.threeten.bp.DayOfWeek.SATURDAY)?.dayOfMonth
            return saturday == day?.day
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.BLUE) {})
        }

    } // SaturdayDecorator


    class SelectableDecorator(selectableDay : ArrayList<CalendarDay>) : DayViewDecorator {

        val list = selectableDay

        override fun shouldDecorate(day: CalendarDay?): Boolean {

            return !list.contains(day)

        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#D9D9D9")) {})
            view?.setDaysDisabled(true)
        }

    } // MinMaxDecorator


}