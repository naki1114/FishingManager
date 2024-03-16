package com.example.fishingmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import com.example.fishingmanager.databinding.FragmentEnterBinding
import kotlin.concurrent.thread

class EnterFragment : Fragment() {

    lateinit var binding : FragmentEnterBinding
    lateinit var nickname: String
    private lateinit var animation : Animation

    private val enterThread = thread(false) {

        Thread.sleep(2500)
        (activity as MainActivity).changeFragment("start")

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter, container, false)

        return binding.root

    } // onCreateView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setVariable()
        enterThread.start()

    } // onViewCreated()


    // 초기화
    fun setVariable() {

        animation = AnimationUtils.loadAnimation(activity, R.anim.splash_alpha)
        binding.enterImage.startAnimation(animation)

    } // setVariable()


}