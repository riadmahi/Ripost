package com.app.ripost.ui.camera

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import kotlinx.android.synthetic.main.activity_preview.*
import java.io.File

class PreviewActivity : AppCompatActivity() {

    private var isSaved = false
    private var videoPath = ""

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
            if(videoPath != "") {
                val intent =
                    Intent(this, UploadActivity::class.java)
                        .putExtra("EXTRA_VIDEO", videoPath)
                        .putExtra("EXTRA_SAVE", isSaved)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Error, this video can't be upload. Reattempt later."
                    , Toast.LENGTH_SHORT).show()
            }
        }




        back.setOnClickListener {
            //Open the bottom sheet to know the user intentions
            val bottomSheet = ClosePreviewBottomSheet()
            bottomSheet.show(supportFragmentManager, "Preview")
        }

        val videoFile = intent.getStringExtra("EXTRA_VIDEO")
        videoPath = videoFile + ""
        Log.d(TAG, "onCreate: file path: $videoFile")
        videoView.setVideoURI(Uri.fromFile(File(videoFile!!)))
        videoView.setOnPreparedListener { it ->
            it.isLooping = true
        }
        videoView.start()
    }

    override fun onBackPressed() {
        //Open the bottom sheet to know the user intentions
        val bottomSheet = ClosePreviewBottomSheet()
        bottomSheet.show(supportFragmentManager, "Preview")
    }

    fun closePreview(){
        finish()
    }

    fun removeUserVideo(){
        if(File(videoPath).exists()) {
            File(videoPath).delete()
            Log.d(TAG, "removeUserVideo: file is removed.")
            finish()
        }
    }
    companion object{
        private const val TAG = "PreviewActivity"
    }
}