package com.app.ripost.ui.camera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import kotlinx.android.synthetic.main.snippet_toolbar.*

class UploadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        toolbarText.text = "Upload your video"
    }
}