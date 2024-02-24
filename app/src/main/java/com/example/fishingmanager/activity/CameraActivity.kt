package com.example.fishingmanager.activity

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fishingmanager.R
import com.websitebeaver.documentscanner.DocumentScanner
import com.websitebeaver.documentscanner.constants.ResponseType

class CameraActivity : AppCompatActivity() {

    val TAG = "카메라액티비티"
    val camera = DocumentScanner(
        this,
        { croppedImageResults ->
            Log.d("CameraActivity", croppedImageResults.first())
            val intent = Intent()
            intent.putExtra("image", croppedImageResults.first())
            setResult(819, intent)
            finish()
        },
        {
            // an error happened
                errorMessage -> Log.v("documentscannerlogs", errorMessage)
        },
        {
            // user canceled document scan
            Log.v("documentscannerlogs", "User canceled document scan")
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        Log.d(TAG, "onCreate()")
        camera.startScan()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }
}