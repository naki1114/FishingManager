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

            }
            else {

                binding.validTextView.text = context?.resources?.getString(R.string.start_signup_1page_valid)
                binding.validTextView.visibility = View.VISIBLE

            }

        } // id observe

        // authNumber observe
        startViewModel.isCorrectAuthNumber.observe(viewLifecycleOwner) {

            if (startViewModel.isCorrectAuthNumber.value == true) {

                viewThirdPage()

            }

        }

        // password observe
        startViewModel.isUsablePassword.observe(viewLifecycleOwner) {

            if (startViewModel.isUsablePassword.value == true) {

                viewFourthPage()
                binding.validTextView.visibility = View.INVISIBLE

            }

        } // password observe

        // rePassword observe
        startViewModel.isUsableRePassword.observe(viewLifecycleOwner) {

            if (startViewModel.isUsableRePassword.value == true) {

                viewFifthPage()

            }
            else {

                binding.validTextView.text = context?.resources?.getString(R.string.start_signup_4page_valid)
                binding.validTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.validTextView.visibility = View.VISIBLE

            }

        } // rePassword observe

        // nickname observe
        startViewModel.isUsableNickname.observe(viewLifecycleOwner) {

            if (startViewModel.isUsableNickname.value == true) {

                viewSixthPage()

            }
            else {

                binding.validTextView.text = context?.resources?.getString(R.string.start_signup_5page_valid)
                binding.validTextView.visibility = View.VISIBLE

            }

        }

        // pageNumber observe
        startViewModel.pageNumberLiveData.observe(viewLifecycleOwner) {

            when (startViewModel.pageNumberLiveData.value) {

                1 -> viewFirstPage()
                2 -> viewSecondPage()
                3 -> viewThirdPage()
                4 -> viewFourthPage()
                5 -> viewFifthPage()

            }

        }

    } // changeSignupPage

    private fun viewFirstPage() {

        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_1page_hint)
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_1page)
        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_1page_main)
        binding.signupSubInfoTextView.text = context?.resources?.getString(R.string.start_signup_1page_sub)
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
        binding.validTextView.visibility = View.INVISIBLE
        binding.signupSubInfoTextView.visibility = View.VISIBLE
        binding.signupPrevButton.visibility = View.INVISIBLE
        binding.signupReSendLayout.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.setText(startViewModel.userID)
        binding.signupUserInfoEditText.setSelection(binding.signupUserInfoEditText.length())
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewFirstPage

    private fun viewSecondPage() {

        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_2page_hint)
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_2page)
        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_2page_main)
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
        binding.validTextView.visibility = View.INVISIBLE
        binding.signupSubInfoTextView.visibility = View.INVISIBLE
        binding.signupPrevButton.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.VISIBLE
        binding.signupUserInfoEditText.text = null
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewSecondPage

    private fun viewThirdPage() {

        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_3page_hint)
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_3page)
        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_3page_main)
        binding.signupSubInfoTextView.text = context?.resources?.getString(R.string.start_signup_3page_sub)
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.validTextView.visibility = View.INVISIBLE
        binding.signupSubInfoTextView.visibility = View.VISIBLE
        binding.signupReSendLayout.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.setText(startViewModel.userPassword)
        binding.signupUserInfoEditText.setSelection(binding.signupUserInfoEditText.length())
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewThirdPage

    private fun viewFourthPage() {

        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_4page_hint)
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_4page)
        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_4page_main)
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.validTextView.visibility = View.INVISIBLE
        binding.signupSubInfoTextView.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.text = null
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewFourthPage

    private fun viewFifthPage() {

        binding.signupUserInfoEditText.hint = context?.resources?.getString(R.string.start_signup_5page_hint)
        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_5page)
        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_5page_main)
        binding.signupSubInfoTextView.text = context?.resources?.getString(R.string.start_signup_5page_sub)
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
        binding.validTextView.visibility = View.INVISIBLE
        binding.signupSubInfoTextView.visibility = View.VISIBLE
        binding.signupUserInfoEditText.setText(startViewModel.userNickname)
        binding.signupUserInfoEditText.setSelection(binding.signupUserInfoEditText.length())
        binding.signupInputLayout.visibility = View.VISIBLE
        binding.signupCheckInfoLayout.visibility = View.GONE
        binding.signupNextButton.text = context?.resources?.getString(R.string.next)

    } // viewFifthPage

    private fun viewSixthPage() {

        binding.signupProgressTextView.text = context?.resources?.getString(R.string.start_signup_6page)
        binding.signupMainInfoTextView.text = context?.resources?.getString(R.string.start_signup_6page_main)
        binding.signupUserInfoEditText.inputType = InputType.TYPE_CLASS_TEXT
        binding.validTextView.visibility = View.INVISIBLE
        binding.signupSubInfoTextView.visibility = View.INVISIBLE
        binding.signupUserInfoEditText.text = null
        binding.signupCheckEmailTextView.text = startViewModel.userID
        binding.signupCheckNicknameTextView.text = startViewModel.userNickname
        binding.signupInputLayout.visibility = View.GONE
        binding.signupCheckInfoLayout.visibility = View.VISIBLE
        binding.signupNextButton.text = context?.resources?.getString(R.string.complete)

    } // viewSixthPage

    private fun passwordValidView() {

        startViewModel.passwordValid.observe(viewLifecycleOwner) {

            if (startViewModel.passwordValid.value == true) {

                binding.validTextView.text = "사용 가능한 비밀번호 입니다."
                binding.validTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                binding.validTextView.visibility = View.VISIBLE

            }
            else {

                binding.validTextView.text = context?.resources?.getString(R.string.start_signup_3page_valid)
                binding.validTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                binding.validTextView.visibility = View.VISIBLE

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

    }

}