package com.app.ripost.ui.camera

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import kotlinx.android.synthetic.main.activity_preview.*
import java.io.File

class PreviewActivity : AppCompatActivity() {

    private var isSaved = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        val shapeTransformation = btnSave.drawable as AnimatedVectorDrawable
        btnSave.setOnClickListener {
            isSaved = if(!isSaved) {
                shapeTransformation.start()
                true
            }else{
                shapeTransformation.reset()
                false
            }
        }

        btnUpload.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }




        back.setOnClickListener {
            //Open the bottom sheet to know the user intentions
            val bottomSheet = ClosePreviewBottomSheet()
            bottomSheet.show(supportFragmentManager, "Preview")
        }

        val videoFile = intent.getStringExtra("EXTRA_VIDEO")
        Log.d(TAG, "onCreate: file path: $videoFile")
        videoView.setVideoURI(Uri.fromFile(File(videoFile!!)))
        videoView.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    companion object{
        private const val TAG = "PreviewActivity"
    }
}