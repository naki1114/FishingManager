package com.example.fishingmanager.fragment

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class StartFragment: Fragment() {

    private val TAG = "StartFragment"

    private lateinit var startViewModel: StartViewModel
    private lateinit var binding: FragmentStartBinding

    private lateinit var googleClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        val intent = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)

        try {

            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)

        } catch (e: ApiException) {

            Log.e(TAG, e.stackTraceToString())

        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        startViewModel = StartViewModel()
        binding.viewModel = startViewModel

        // LiveData Observe
        changeLayout()
        changeSignUpPage()
        passwordValidView()
        finishSignup()
        socialLoginCheck()
        socialLogin()
        socialNicknameCheck()

        loginCheck()
        timer()

    } // onViewCreated()


    // 레이아웃 전환
    private fun changeLayout() {

        startViewModel.layoutLiveData.observe(viewLifecycleOwner) {

            when(it) {

                "login" -> {

                    binding.loginLayout.visibility = View.VISIBLE
                    binding.signupLayout.visibility = View.GONE
                    binding.signupSocialLayout.visibility = View.GONE
                    binding.startLayout.visibility = View.GONE

                }
                "signup", "findPassword" -> {

                    if (it == "signup") {
                        binding.signupTitleTextView.text = context?.resources?.getString(R.string.start_signup)
                    } else {
                        binding.signupTitleTextView.text =context?.resources?.getString(R.string.start_findPassword)
                    }

                    binding.loginLayout.visibility = View.GONE
                    binding.signupLayout.visibility = View.VISIBLE
                    binding.signupSocialLayout.visibility = View.GONE
                    binding.startLayout.visibility = View.GONE

                }
                "signupSocial" -> {

                    binding.loginLayout.visibility = View.GONE
                    binding.signupLayout.visibility = View.GONE
                    binding.signupSocialLayout.visibility = View.VISIBLE
                    binding.startLayout.visibility = View.GONE

                }
                else -> {

                    binding.loginLayout.visibility = View.GONE
                    binding.signupLayout.visibility = View.GONE
                    binding.signupSocialLayout.visibility = View.GONE
                    binding.startLayout.visibility = View.VISIBLE

                }

            }

        }

    } // changeLayout()


    // FM 회원가입 과정에서 사용자의 입력값(id, quthNumber, password, rePassword, nickname 등) observe
    private fun changeSignUpPage() {

        // id observe
        startViewModel.isUsableEmail.observe(viewLifecycleOwner) {

            if (it) {

                viewSignUpSecondPage()
                sendEmail()

            } else {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

                if (binding.signupUserInfoEditText.length() == 0) {

                    binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_1page_valid_not_input)

                } else {

                    binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_1page_valid_using)

                }

                binding.signupValidTextView.visibility = View.VISIBLE

            }

        } // id observe

        // authNumber observe
        startViewModel.isCorrectAuthNumber.observe(viewLifecycleOwner) {

            if (it) {

                viewSignUpThirdPage()

            } else {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_2page_valid_mismatch)
                binding.signupValidTextView.visibility = View.VISIBLE

            }

        } // authNumber observe

        // password observe
        startViewModel.isUsablePassword.observe(viewLifecycleOwner) {

            if (it) {

                viewSignUpFourthPage()

            }

        } // password observe

        // rePassword observe
        startViewModel.isUsableRePassword.observe(viewLifecycleOwner) {

            if (it) {

                viewSignUpFifthPage()

            } else {

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

        startViewModel.isSuccessfulFindPassword.observe(viewLifecycleOwner) {

            if (it) {

                binding.loginLayout.visibility = View.VISIBLE
                binding.signupLayout.visibility = View.GONE
                binding.signupSocialLayout.visibility = View.GONE
                binding.startLayout.visibility = View.GONE

                viewSignUpFirstPage()

                Toast.makeText(requireContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()

            }

        }

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

    } // changeSignUpPage()


    // 소셜 로그인시 닉네임 체크
    private fun socialNicknameCheck() {

        startViewModel.isUsableSocialNickname.observe(viewLifecycleOwner) {

            when (it) {

                0 -> {

                    viewSignUpSocialSecondPage()

                }
                1 -> {

                    binding.signupSocialValidTextView.text = context?.resources?.getString(R.string.start_signup_5page_valid_using)
                    binding.signupSocialValidTextView.visibility = View.VISIBLE

                }
                2 -> {

                    binding.signupSocialValidTextView.text = context?.resources?.getString(R.string.start_signup_5page_valid_false)
                    binding.signupSocialValidTextView.visibility = View.VISIBLE

                }

            }

        }

    } // socialNicknameCheck()


    // FM 회원가입 / 비밀번호 찾기 1페이지 호출
    private fun viewSignUpFirstPage() {

        val select = startViewModel.layoutLiveData.value

        if (select == "signup") {

            binding.signupTitleTextView.text =
                context?.resources?.getString(R.string.start_signup)
            binding.signupMainInfoTextView.text =
                context?.resources?.getString(R.string.start_signup_1page_main)
            binding.signupSubInfoTextView.text =
                context?.resources?.getString(R.string.start_signup_1page_sub)
            binding.signupUserInfoEditText.hint =
                context?.resources?.getString(R.string.start_signup_1page_hint)
            binding.signupProgressTextView.text =
                context?.resources?.getString(R.string.start_signup_1page)

        } else if (select == "findPassword") {

            binding.signupTitleTextView.text =
                context?.resources?.getString(R.string.start_findPassword)
            binding.signupMainInfoTextView.text =
                context?.resources?.getString(R.string.start_findPassword_1page_main)
            binding.signupSubInfoTextView.text =
                context?.resources?.getString(R.string.start_findPassword_1page_sub)
            binding.signupUserInfoEditText.hint =
                context?.resources?.getString(R.string.start_findPassword_1page_hint)
            binding.signupProgressTextView.text =
                context?.resources?.getString(R.string.start_findPassword_1page)

        }

        binding.signupSubInfoTextView.visibility = View.VISIBLE
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
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
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewSignUpFirstPage() <- id


    // FM 회원가입 / 비밀번호 찾기 2페이지 호출
    private fun viewSignUpSecondPage() {

        val select = startViewModel.layoutLiveData.value

        if (select == "signup") {

            binding.signupMainInfoTextView.text =
                context?.resources?.getString(R.string.start_signup_2page_main)
            binding.signupUserInfoEditText.hint =
                context?.resources?.getString(R.string.start_signup_2page_hint)
            binding.signupProgressTextView.text =
                context?.resources?.getString(R.string.start_signup_2page)

        } else if (select == "findPassword") {

            binding.signupMainInfoTextView.text =
                context?.resources?.getString(R.string.start_findPassword_2page_main)
            binding.signupUserInfoEditText.hint =
                context?.resources?.getString(R.string.start_findPassword_2page_hint)
            binding.signupProgressTextView.text =
                context?.resources?.getString(R.string.start_findPassword_2page)

        }

        binding.signupSubInfoTextView.text = null
        binding.signupSubInfoTextView.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_NUMBER
        binding.signupUserInfoEditText.text = null
        binding.signupValidTextView.visibility = View.VISIBLE
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.VISIBLE
        binding.signupCheckEmailTextView.text = null
        binding.signupCheckNicknameTextView.text = null
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupPrevButton.visibility = View.VISIBLE
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewSignUpSecondPage() <- authNumber


    // FM 회원가입 / 비밀번호 찾기 3페이지 호출
    private fun viewSignUpThirdPage() {

        val select = startViewModel.layoutLiveData.value

        if (select == "signup") {

            binding.signupMainInfoTextView.text =
                context?.resources?.getString(R.string.start_signup_3page_main)
            binding.signupSubInfoTextView.text =
                context?.resources?.getString(R.string.start_signup_3page_sub)
            binding.signupUserInfoEditText.hint =
                context?.resources?.getString(R.string.start_signup_3page_hint)
            binding.signupProgressTextView.text =
                context?.resources?.getString(R.string.start_signup_3page)

        } else if (select == "findPassword") {

            binding.signupMainInfoTextView.text =
                context?.resources?.getString(R.string.start_findPassword_3page_main)
            binding.signupSubInfoTextView.text =
                context?.resources?.getString(R.string.start_findPassword_3page_sub)
            binding.signupUserInfoEditText.hint =
                context?.resources?.getString(R.string.start_findPassword_3page_hint)
            binding.signupProgressTextView.text =
                context?.resources?.getString(R.string.start_findPassword_3page)

        }

        binding.signupSubInfoTextView.visibility = View.VISIBLE
        binding.signupUserInfoEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
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
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewSignUpThirdPage() <- password


    // FM 회원가입 / 비밀번호 찾기 4페이지 호출
    private fun viewSignUpFourthPage() {

        val select = startViewModel.layoutLiveData.value

        if (select == "signup") {

            binding.signupMainInfoTextView.text =
                context?.resources?.getString(R.string.start_signup_4page_main)
            binding.signupUserInfoEditText.hint =
                context?.resources?.getString(R.string.start_signup_4page_hint)
            binding.signupProgressTextView.text =
                context?.resources?.getString(R.string.start_signup_4page)

        } else if (select == "findPassword") {

            binding.signupMainInfoTextView.text =
                context?.resources?.getString(R.string.start_findPassword_4page_main)
            binding.signupUserInfoEditText.hint =
                context?.resources?.getString(R.string.start_findPassword_4page_hint)
            binding.signupProgressTextView.text =
                context?.resources?.getString(R.string.start_findPassword_4page)

        }

        binding.signupSubInfoTextView.text = null
        binding.signupSubInfoTextView.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.signupUserInfoEditText.text = null
        binding.signupValidTextView.visibility = View.INVISIBLE
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.INVISIBLE
        binding.signupCheckEmailTextView.text = null
        binding.signupCheckNicknameTextView.text = null
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupPrevButton.visibility = View.VISIBLE
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewSignUpFourthPage() <- rePassword


    // FM 회원가입 5페이지 호출
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

    } // viewSignUpFifthPage() <- nickname


    // FM 회원가입 6페이지 호출
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

    } // viewSignUpSixthPage() <- checkInfo


    // 소셜 회원가입 2페이지 호출
    private fun viewSignUpSocialSecondPage() {

        binding.signupSocialMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_6page_main)
        binding.signupSocialInputLayout.visibility = View.GONE
        binding.signupSocialCheckEmailTextView.text = startViewModel.userID
        binding.signupSocialCheckNicknameTextView.text = startViewModel.userNickname
        binding.signupSocialCheckInfoLayout.visibility = View.VISIBLE
        binding.signupSocialPrevButton.visibility = View.VISIBLE
        binding.signupSocialNextButton.text =  context?.resources?.getString(R.string.complete)
        binding.signupSocialProgressTextView.text = context?.resources?.getString(R.string.start_signup_google_2page)

    } // viewSignUpSocialSecondPage() <- checkInfo


    // 비밀번호 정규식 확인
    private fun passwordValidView() {

        startViewModel.passwordValid.observe(viewLifecycleOwner) {

            if (it) {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_3page_valid_true)
                binding.signupValidTextView.visibility = View.VISIBLE

            } else {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_3page_valid_false)
                binding.signupValidTextView.visibility = View.VISIBLE

            }

        }

    } // passwordValidView()


    // FM 회원가입 완료
    private fun finishSignup() {

        startViewModel.isSavedUserInfo.observe(viewLifecycleOwner) {

            if (it) {

                viewSignUpFirstPage()
                binding.signupLayout.visibility = View.GONE
                saveUserInfoToShared()
                toSplashFragment()

            }

        }

    } // finishSignup()


    // 로그인시 회원 정보 확인
    private fun loginCheck() {

        startViewModel.isPossibleLogin.observe(viewLifecycleOwner) {

            if (it) {

                saveUserInfoToShared()
                toSplashFragment()

            } else {

                binding.failureLoginTextView.visibility = View.VISIBLE

            }

        }

    } // loginCheck()


    // 사용자 이메일로 인증 번호 전송
    private fun sendEmail() {

        val email = startViewModel.userID
        val authNumber = startViewModel.authNumber

        GmailSender().sendMail(authNumber, email)

    } // sendEmail()


    // 타이머 남은 시간에 따라 사용자에게 표기
    private fun timer() {

        startViewModel.authTime.observe(viewLifecycleOwner) {

            val time = startViewModel.authTime.value

            if (time!! >= 0) {

                val sec = if (time % 60 < 10) "0" + (time % 60).toString() else (time % 60)
                val min = "0" + (time / 60)

                binding.signupValidTextView.text = "$min : $sec"

            }

        }

        startViewModel.isTimeOutAuth.observe(viewLifecycleOwner) {

            if (!it) {

                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_2page_valid_timeOut)

            }

        }

    } // timer()


    // FM 회원 정보 SharedPreferences에 저장
    private fun saveUserInfoToShared() {

        val sharedPreferences = activity?.getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        val edit = sharedPreferences?.edit()
        (activity as MainActivity).nickname = startViewModel.userNickname

        edit?.putString("nickname", startViewModel.userNickname)
        edit?.putString("type", "FM")
        edit?.apply()

    } // saveUserInfoToShared()


    // 소셜 로그인 회원 정보 SharedPreferences에 저장
    private fun saveUserInfoToShared(nickname: String, type: String) {

        val sharedPreferences = activity?.getSharedPreferences("loginInfo", AppCompatActivity.MODE_PRIVATE)
        val edit = sharedPreferences?.edit()
        (activity as MainActivity).nickname = nickname

        edit?.putString("nickname", nickname)
        edit?.putString("type", type)
        edit?.apply()

    } // saveUserInfoToShared()


    // 소셜 로그인 타입에 따라 로그인 실행
    private fun socialLoginCheck() {

        startViewModel.socialType.observe(viewLifecycleOwner) {

            when(it) {

                "google" -> googleLogin()
                "kakao" -> kakaoLogin()
                "naver" -> naverLogin()

            }

        }

    } // socialLoginCheck()


    // 구글 로그인
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

    } // googleLogin()


    // Firebase 인증
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

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

    } // firebaseAuthWithGoogle()


    // 카카오 로그인
    private fun kakaoLogin() {

        KakaoSdk.init(requireContext(), SocialAccount.KAKAO_NATIVE_APP_KEY)

        val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

            if (error != null) {

                Log.d(TAG, "로그인 실패 $error")

            } else if (token != null) {

                Log.d(TAG, "로그인 성공 ${token.accessToken}")

            }

        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {

            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->

                if (error != null) {

                    Log.e(TAG, "로그인 실패 $error")

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {

                        return@loginWithKakaoTalk

                    } else {

                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback)

                    }
                } else if (token != null) {

                    UserApiClient.instance.me { user, error ->

                        if (error != null) {

                            Log.d(TAG, "kakaoLogin : 사용자 정보 요청 실패 $error")

                        } else if (user != null) {

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

            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = mCallback)

        }

    } // kakaoLogin()


    // 네이버 로그인
    private fun naverLogin() {

        val naverClientId = SocialAccount.NAVER_CLIENT_ID
        val naverClientSecret = SocialAccount.NAVER_CLIENT_PW
        val naverClientName = "네이버 아이디 로그인"

        NaverIdLoginSDK.initialize(requireActivity(), naverClientId, naverClientSecret, naverClientName)

        var naverToken: String

        val profileCallback = object: NidProfileCallback<NidProfileResponse> {

            override fun onSuccess(result: NidProfileResponse) {

                val id = result.profile?.email.toString()
                val profileImage = result.profile?.profileImage.toString()
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

        val oauthLoginCallback = object: OAuthLoginCallback {

            override fun onSuccess() {

                naverToken = NaverIdLoginSDK.getAccessToken().toString()

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

    } // naverLogin()


    // 프래그먼트 전환
    private fun toSplashFragment() {

        (activity as MainActivity).changeFragment("splash")

    } // toSplashFragment()


    // 소셜 로그인 확인
    private fun socialLogin() {

        startViewModel.socialLoginCheck.observe(viewLifecycleOwner) {

            if (it) {

                val nickname = startViewModel.userNickname
                val type = startViewModel.type

                saveUserInfoToShared(nickname, type)
                toSplashFragment()

            }

        }

        startViewModel.isSignedUpUser.observe(viewLifecycleOwner) {

            if (it) {

                val nickname = startViewModel.userNickname
                val type = startViewModel.type

                saveUserInfoToShared(nickname, type)
                toSplashFragment()

            } else {

                startViewModel.changeLayout("signupSocial")

            }

        }

    } // socialLogin()


}