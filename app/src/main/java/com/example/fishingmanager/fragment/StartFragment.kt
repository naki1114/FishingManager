package com.example.fishingmanager.fragment

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.databinding.FragmentStartBinding
import com.example.fishingmanager.other.GmailSender
import com.example.fishingmanager.viewModel.StartViewModel

class StartFragment : Fragment() {

    private lateinit var startViewModel : StartViewModel
    private lateinit var binding : FragmentStartBinding

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

        loginCheck()
        timer()
    }

    // Custom Method

    private fun changeLayout() {

        startViewModel.layoutLiveData.observe(viewLifecycleOwner) {

            when(startViewModel.layoutLiveData.value) {

                "login" -> {
                    binding.loginLayout.visibility = View.VISIBLE
                    binding.signupLayout.visibility = View.GONE
                    binding.startLayout.visibility = View.GONE
                }
                "signup" -> {
                    binding.loginLayout.visibility = View.GONE
                    binding.signupLayout.visibility = View.VISIBLE
                    binding.startLayout.visibility = View.GONE
                }
                else -> {
                    binding.loginLayout.visibility = View.GONE
                    binding.signupLayout.visibility = View.GONE
                    binding.startLayout.visibility = View.VISIBLE
                }

            }

        }

    } // changeLayout

    private fun changeSignupPage() {

        // id observe
        startViewModel.isUsableEmail.observe(viewLifecycleOwner) {

            if (startViewModel.isUsableEmail.value == true) {

                viewSecondPage()
                sendEmail()

            }
            else if (startViewModel.isUsableEmail.value == false) {

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

            if (startViewModel.isCorrectAuthNumber.value == true) {

                viewThirdPage()

            }
            else {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_2page_valid_mismatch)
                binding.signupValidTextView.visibility = View.VISIBLE

            }

        } // authNumber observe

        // password observe
        startViewModel.isUsablePassword.observe(viewLifecycleOwner) {

            if (startViewModel.isUsablePassword.value == true) {

                viewFourthPage()

            }

        } // password observe

        // rePassword observe
        startViewModel.isUsableRePassword.observe(viewLifecycleOwner) {

            if (startViewModel.isUsableRePassword.value == true) {

                viewFifthPage()

            }
            else {

                binding.signupValidTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_4page_valid)
                binding.signupValidTextView.visibility = View.VISIBLE

            }

        } // rePassword observe

        // nickname observe
        startViewModel.isUsableNickname.observe(viewLifecycleOwner) {

            when (startViewModel.isUsableNickname.value) {

                0 -> {

                    viewSixthPage()

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

            when (startViewModel.pageNumberLiveData.value) {

                1 -> viewFirstPage()
                2 -> viewSecondPage()
                3 -> viewThirdPage()
                4 -> viewFourthPage()
                5 -> viewFifthPage()

            }

        } // pageNumber observe

    } // changeSignupPage

    private fun viewFirstPage() {

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

    private fun viewSecondPage() {

        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_2page_main)
        binding.signupSubInfoTextView.text = null
        binding.signupSubInfoTextView.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
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

    private fun viewThirdPage() {

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

    private fun viewFourthPage() {

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

    private fun viewFifthPage() {

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

    private fun viewSixthPage() {

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

    private fun passwordValidView() {

        startViewModel.passwordValid.observe(viewLifecycleOwner) {

            if (startViewModel.passwordValid.value == true) {

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

            if (startViewModel.isSavedUserInfo.value == true) {

                viewFirstPage()
                binding.signupLayout.visibility = View.GONE
                (activity as MainActivity).changeFragment("home")

            }

        }

    } // finishSignup

    private fun loginCheck() {

        startViewModel.isPossibleLogin.observe(viewLifecycleOwner) {

            if (startViewModel.isPossibleLogin.value == true) {

                (activity as MainActivity).changeFragment("home")

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

                requireActivity().runOnUiThread {

                    binding.signupValidTextView.text = "$min : $sec"

                }

            }

        }

        startViewModel.isTimeOutAuth.observe(viewLifecycleOwner) {

            if (startViewModel.isTimeOutAuth.value == false) {

                binding.signupValidTextView.text = context?.resources?.getString(R.string.start_signup_2page_valid_timeOut)

            }

        }

    } // timer

}