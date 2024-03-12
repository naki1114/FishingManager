package com.example.fishingmanager.fragment

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.databinding.FragmentStartBinding
import com.example.fishingmanager.other.GmailSender
import com.example.fishingmanager.other.SocialAccount
import com.example.fishingmanager.viewModel.StartViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class StartFragment : Fragment() {

    val TAG = "StartFragment"

    private lateinit var startViewModel : StartViewModel
    private lateinit var binding : FragmentStartBinding

    private lateinit var googleClient : GoogleSignInClient
    private lateinit var auth : FirebaseAuth
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        val intent = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        }
        catch (e : ApiException) {
            Log.e(TAG, e.stackTraceToString())
        }

    }

    // Lifecycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startViewModel = StartViewModel()
        binding.viewModel = startViewModel

        // LiveData Observe
        changeLayout()
        changeSignupPage()
        passwordValidView()
        finishSignup()
        socialLoginCheck()
        socialLogin()
        socialNicknameCheck()

        loginCheck()
        timer()
    }

    // Custom Method

    private fun changeLayout() {

        startViewModel.layoutLiveData.observe(viewLifecycleOwner) {

            when(it) {

                "login" -> {
                    binding.loginLayout.visibility = View.VISIBLE
                    binding.signupLayout.visibility = View.GONE
                    binding.signupGoogleLayout.visibility = View.GONE
                    binding.startLayout.visibility = View.GONE
                }
                "signup" -> {
                    binding.loginLayout.visibility = View.GONE
                    binding.signupLayout.visibility = View.VISIBLE
                    binding.signupGoogleLayout.visibility = View.GONE
                    binding.startLayout.visibility = View.GONE
                }
                "signupSocial" -> {
                    binding.loginLayout.visibility = View.GONE
                    binding.signupLayout.visibility = View.GONE
                    binding.signupGoogleLayout.visibility = View.VISIBLE
                    binding.startLayout.visibility = View.GONE
                }
                else -> {
                    binding.loginLayout.visibility = View.GONE
                    binding.signupLayout.visibility = View.GONE
                    binding.signupGoogleLayout.visibility = View.GONE
                    binding.startLayout.visibility = View.VISIBLE
                }

            }

        }

    } // changeLayout

    private fun changeSignupPage() {

        // id observe
        startViewModel.isUsableEmail.observe(viewLifecycleOwner) {

            if (it == true) {

                viewSignUpSecondPage()
                sendEmail()

            }
            else if (it == false) {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

                if (binding.signupUserInfoEditText.length() == 0) {

                    binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_1page_valid_not_input)

                }
                else {

                    binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_1page_valid_using)

                }

                binding.signupValidTextView.visibility = View.VISIBLE

            }

        } // id observe

        // authNumber observe
        startViewModel.isCorrectAuthNumber.observe(viewLifecycleOwner) {

            if (it == true) {

                viewSignUpThirdPage()

            }
            else {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_2page_valid_mismatch)
                binding.signupValidTextView.visibility = View.VISIBLE

            }

        } // authNumber observe

        // password observe
        startViewModel.isUsablePassword.observe(viewLifecycleOwner) {

            if (it == true) {

                viewSignUpFourthPage()

            }

        } // password observe

        // rePassword observe
        startViewModel.isUsableRePassword.observe(viewLifecycleOwner) {

            if (it == true) {

                viewSignUpFifthPage()

            }
            else {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_4page_valid)
                binding.signupValidTextView.visibility = View.VISIBLE

            }

        } // rePassword observe

        // nickname observe
        startViewModel.isUsableNickname.observe(viewLifecycleOwner) {

            when (it) {

                0 -> {

                    viewSignUpSixthPage()

                }
                1 -> {

                    binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_5page_valid_using)
                    binding.signupValidTextView.visibility = View.VISIBLE

                }
                2 -> {

                    binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_5page_valid_false)
                    binding.signupValidTextView.visibility = View.VISIBLE

                }

            }

        } // nickname observe

        // pageNumber observe
        startViewModel.pageNumberLiveData.observe(viewLifecycleOwner) {

            when (it) {

                1 -> viewSignUpFirstPage()
                2 -> viewSignUpSecondPage()
                3 -> viewSignUpThirdPage()
                4 -> viewSignUpFourthPage()
                5 -> viewSignUpFifthPage()

            }

        } // pageNumber observe

    } // changeSignupPage

    private fun socialNicknameCheck() {

        startViewModel.isUsableSocialNickname.observe(viewLifecycleOwner) {

            when (it) {

                0 -> {

                    viewSignUpGoogleSecondPage()

                }
                1 -> {

                    binding.signupGoogleValidTextView.text = context?.resources?.getString(R.string.start_signup_5page_valid_using)
                    binding.signupGoogleValidTextView.visibility = View.VISIBLE

                }
                2 -> {

                    binding.signupGoogleValidTextView.text = context?.resources?.getString(R.string.start_signup_5page_valid_false)
                    binding.signupGoogleValidTextView.visibility = View.VISIBLE

                }

            }

        }

    } // googleNicknameCheck

    private fun viewSignUpFirstPage() {

        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_1page_main)
        binding.signupSubInfoTextView.text = context?.resources?.getString(R.string.start_signup_1page_sub)
        binding.signupSubInfoTextView.visibility = View.VISIBLE
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_1page_hint)
        binding.signupUserInfoEditText.text = null
        binding.signupUserInfoEditText.setText(startViewModel.userID)
        binding.signupUserInfoEditText.setSelection(binding.signupUserInfoEditText.length())
        binding.signupValidTextView.visibility = View.INVISIBLE
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.INVISIBLE
        binding.signupCheckEmailTextView.text = null
        binding.signupCheckNicknameTextView.text = null
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupPrevButton.visibility = View.INVISIBLE
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_1page)
        binding.signupNextButton.text =  context?.resources?.getString(R.string.next)

    } // viewFirstPage <- id

    private fun viewSignUpSecondPage() {

        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_2page_main)
        binding.signupSubInfoTextView.text = null
        binding.signupSubInfoTextView.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_NUMBER
        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_2page_hint)
        binding.signupUserInfoEditText.text = null
        binding.signupValidTextView.visibility = View.VISIBLE
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.VISIBLE
        binding.signupCheckEmailTextView.text = null
        binding.signupCheckNicknameTextView.text = null
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupPrevButton.visibility = View.VISIBLE
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_2page)
        binding.signupNextButton.text =  context?.resources?.getString(R.string.next)

    } // viewSecondPage <- authNumber

    private fun viewSignUpThirdPage() {

        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_3page_main)
        binding.signupSubInfoTextView.text = context?.resources?.getString(R.string.start_signup_3page_sub)
        binding.signupSubInfoTextView.visibility = View.VISIBLE
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_3page_hint)
        binding.signupUserInfoEditText.text = null
        binding.signupUserInfoEditText.setText(startViewModel.userPassword)
        binding.signupUserInfoEditText.setSelection(binding.signupUserInfoEditText.length())
        binding.signupValidTextView.visibility = View.INVISIBLE
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.INVISIBLE
        binding.signupCheckEmailTextView.text = null
        binding.signupCheckNicknameTextView.text = null
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupPrevButton.visibility = View.VISIBLE
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_3page)
        binding.signupNextButton.text =  context?.resources?.getString(R.string.next)

    } // viewThirdPage <- password

    private fun viewSignUpFourthPage() {

        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_4page_main)
        binding.signupSubInfoTextView.text = null
        binding.signupSubInfoTextView.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_4page_hint)
        binding.signupUserInfoEditText.text = null
        binding.signupValidTextView.visibility = View.INVISIBLE
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.INVISIBLE
        binding.signupCheckEmailTextView.text = null
        binding.signupCheckNicknameTextView.text = null
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupPrevButton.visibility = View.VISIBLE
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_4page)
        binding.signupNextButton.text =  context?.resources?.getString(R.string.next)

    } // viewFourthPage <- rePassword

    private fun viewSignUpFifthPage() {

        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_5page_main)
        binding.signupSubInfoTextView.text = context?.resources?.getString(R.string.start_signup_5page_sub)
        binding.signupSubInfoTextView.visibility = View.VISIBLE
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_5page_hint)
        binding.signupUserInfoEditText.text = null
        binding.signupUserInfoEditText.setText(startViewModel.userNickname)
        binding.signupUserInfoEditText.setSelection(binding.signupUserInfoEditText.length())
        binding.signupValidTextView.visibility = View.INVISIBLE
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.INVISIBLE
        binding.signupCheckEmailTextView.text = null
        binding.signupCheckNicknameTextView.text = null
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupPrevButton.visibility = View.VISIBLE
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_5page)
        binding.signupNextButton.text =  context?.resources?.getString(R.string.next)

    } // viewFifthPage <- nickname

    private fun viewSignUpSixthPage() {

        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_6page_main)
        binding.signupInputLayout.visibility = View.GONE
        binding.signupReSendLayout.visibility = View.INVISIBLE
        binding.signupCheckEmailTextView.text = startViewModel.userID
        binding.signupCheckNicknameTextView.text = startViewModel.userNickname
        binding.signupCheckInfoLayout.visibility = View.VISIBLE
        binding.signupPrevButton.visibility = View.VISIBLE
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_6page)
        binding.signupNextButton.text =  context?.resources?.getString(R.string.complete)

    } // viewSixthPage <- checkInfo

    private fun viewSignUpGoogleFirstPage() {

        binding.signupGoogleMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_5page_main)
        binding.signupGoogleUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
        binding.signupGoogleUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_5page_hint)
        binding.signupGoogleUserInfoEditText.setText(startViewModel.userID)
        binding.signupGoogleUserInfoEditText.setSelection(binding.signupUserInfoEditText.length())
        binding.signupGoogleValidTextView.visibility = View.INVISIBLE
        binding.signupGoogleInputLayout.visibility = View.VISIBLE
        binding.signupGoogleCheckEmailTextView.text = null
        binding.signupGoogleCheckNicknameTextView.text = null
        binding.signupGoogleCheckInfoLayout.visibility = View.GONE
        binding.signupGooglePrevButton.visibility = View.INVISIBLE
        binding.signupGoogleNextButton.text =  context?.resources?.getString(R.string.next)
        binding.signupGoogleNextButton.visibility = View.VISIBLE
        binding.signupGoogleProgressTextView.text = context?.resources?.getString(R.string.start_signup_google_1page)

    } // viewSignUpGoogleFirstPage

    private fun viewSignUpGoogleSecondPage() {

        binding.signupGoogleMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_6page_main)
        binding.signupGoogleInputLayout.visibility = View.GONE
        binding.signupGoogleCheckEmailTextView.text = startViewModel.userID
        binding.signupGoogleCheckNicknameTextView.text = startViewModel.userNickname
        binding.signupGoogleCheckInfoLayout.visibility = View.VISIBLE
        binding.signupGooglePrevButton.visibility = View.VISIBLE
        binding.signupGoogleNextButton.text =  context?.resources?.getString(R.string.complete)
        binding.signupGoogleProgressTextView.text = context?.resources?.getString(R.string.start_signup_google_2page)

    } // viewSignUpGoogleSecondPage

    private fun passwordValidView() {

        startViewModel.passwordValid.observe(viewLifecycleOwner) {

            if (it == true) {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_3page_valid_true)
                binding.signupValidTextView.visibility = View.VISIBLE

            }
            else {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_3page_valid_false)
                binding.signupValidTextView.visibility = View.VISIBLE

            }

        }

    } // passwordValidView

    private fun finishSignup() {

        startViewModel.isSavedUserInfo.observe(viewLifecycleOwner) {

            if (it == true) {

                viewSignUpFirstPage()
                binding.signupLayout.visibility = View.GONE
                saveUserInfoToShared()
                changeFragment("splash")

            }

        }

    } // finishSignup

    private fun loginCheck() {

        startViewModel.isPossibleLogin.observe(viewLifecycleOwner) {

            if (it == true) {

                saveUserInfoToShared()
                changeFragment("splash")

            }
            else {

                binding.failureLoginTextView.visibility = View.VISIBLE

            }

        }

    } // loginCheck

    private fun sendEmail() {

        val email = startViewModel.userID
        val authNumber = startViewModel.authNumber

        GmailSender().sendMail(authNumber, email)

    } // sendEmail

    private fun timer() {

        startViewModel.authTime.observe(viewLifecycleOwner) {

            val time = startViewModel.authTime.value

            if (time!! >= 0) {

                val sec = if (time % 60 < 10) "0" + (time % 60).toString() else (time % 60).toString()
                val min = "0" + (time / 60).toString()

                binding.signupValidTextView.text = "$min : $sec"

            }

        }

        startViewModel.isTimeOutAuth.observe(viewLifecycleOwner) {

            if (it == false) {

                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_2page_valid_timeOut)

            }

        }

    } // timer

    private fun saveUserInfoToShared() {

        val sharedPreferences = activity?.getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        val edit = sharedPreferences?.edit()
        (activity as MainActivity).nickname = startViewModel.userNickname

        edit?.putString("nickname", startViewModel.userNickname)
        edit?.putString("type", "FM")
        edit?.commit()

    } // saveUserInfoToShared

    private fun saveUserInfoToShared(nickname : String, type : String) {

        val sharedPreferences = activity?.getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        val edit = sharedPreferences?.edit()
        (activity as MainActivity).nickname = nickname

        edit?.putString("nickname", nickname)
        edit?.putString("type", type)
        edit?.commit()

    } // saveUserInfoToShared

    private fun socialLoginCheck() {

        startViewModel.socialType.observe(viewLifecycleOwner) {

            when(it) {

                "google" -> googleLogin()
                "kakao" -> kakaoLogin()
                "naver" -> naverLogin()

            }

        }

    } // socialLogin

    private fun googleLogin() {

        auth = FirebaseAuth.getInstance()

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestIdToken(SocialAccount.GOOGLE_CLIENT_ID)
            .requestEmail()
            .requestId()
            .requestProfile()
            .build()

        googleClient = GoogleSignIn.getClient(requireActivity(), googleSignInOption)

        val signInIntent = googleClient.signInIntent
        googleAuthLauncher.launch(signInIntent)

    } // googleLogin

    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val id = account.email.toString()
                val profileImage = account.photoUrl.toString()
                val type = startViewModel.socialType.value.toString()

                startViewModel.userID = id
                startViewModel.profileImage = profileImage

                startViewModel.isSignedUpUserCheck(id, type)

            }
        }

    } // firebaseAuthWithGoogle

    private fun kakaoLogin() {

        KakaoSdk.init(requireContext(), SocialAccount.KAKAO_NATIVE_APP_KEY)

        val mCallback : (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d(TAG, "로그인 실패 $error")
            }
            else if (token != null) {
                Log.d(TAG, "로그인 성공 ${token.accessToken}")
            }
        }

        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                // 로그인 실패 부분
                if (error != null) {
                    Log.e(TAG, "로그인 실패 $error")
                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                        return@loginWithKakaoTalk
                    }
                    // 다른 오류
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback) // 카카오 이메일 로그인
                    }
                }
                // 로그인 성공 부분
                else if (token != null) {
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {

                            Log.d(TAG, "kakaoLogin : 사용자 정보 요청 실패 $error")

                        }
                        else if (user != null){

                            val id = user.kakaoAccount?.email.toString()
                            val profileImage = user.kakaoAccount?.profile?.profileImageUrl.toString()
                            val type = startViewModel.socialType.value.toString()

                            startViewModel.userID = id
                            startViewModel.profileImage = profileImage

                            startViewModel.isSignedUpUserCheck(id, type)

                        }
                    }

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback) // 카카오 이메일 로그인
        }

    } // kakaoLogin

    private fun naverLogin() {

        val naverClientId = SocialAccount.NAVER_CLIENT_ID
        val naverClientSecret = SocialAccount.NAVER_CLIENT_PW
        val naverClientName = "네이버 아이디 로그인"

        NaverIdLoginSDK.initialize(requireActivity(), naverClientId, naverClientSecret, naverClientName)

        var naverToken : String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {

            override fun onSuccess(response: NidProfileResponse) {

                val id = response.profile?.email.toString()
                val profileImage = response.profile?.profileImage.toString()
                val type = startViewModel.socialType.value.toString()

                startViewModel.userID = id
                startViewModel.profileImage = profileImage

                startViewModel.isSignedUpUserCheck(id, type)

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

        }

        val oauthLoginCallback = object : OAuthLoginCallback {

            override fun onSuccess() {
                naverToken = NaverIdLoginSDK.getAccessToken()

                NidOAuthLogin().callProfileApi(profileCallback)
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

        }

        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)

        NaverIdLoginSDK.logout()

    } // naverLogin

    private fun changeFragment(fragment : String) {

        (activity as MainActivity).changeFragment(fragment)

    } // changeFragment

    private fun socialLogin() {

        startViewModel.socialLoginCheck.observe(viewLifecycleOwner) {

            if (it) {

                val id = startViewModel.userID
                val nickname = startViewModel.userNickname
                val profileImage = startViewModel.profileImage
                val type = startViewModel.type

                saveUserInfoToShared(nickname, type)
                changeFragment("splash")

            }

        }

        startViewModel.isSignedUpUser.observe(viewLifecycleOwner) {

            if (it) {

                val nickname = startViewModel.userNickname
                val type = startViewModel.type

                saveUserInfoToShared(nickname, type)
                changeFragment("splash")

            }
            else {

                startViewModel.changeLayout("signupSocial")

            }

        }

    } // socialLogin

    private fun getUserInfo() : String {

        val userInfoShared = requireActivity().getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)

        return userInfoShared.getString("nickname", "").toString()

    } // checkUserShared

}