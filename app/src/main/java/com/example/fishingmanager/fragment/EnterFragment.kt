package com.example.fishingmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.fishingmanager.R
import com.example.fishingmanager.activity.MainActivity
import kotlin.concurrent.thread

class EnterFragment : Fragment() {

    lateinit var nickname: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_enter, container, false)

    } // onCreateView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterThread.start()

    } // onViewCreated()


    val enterThread = thread(false) {

        Thread.sleep(1000)
        (activity as MainActivity).changeFragment("start")

    }

}