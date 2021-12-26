package com.app.ripost.ui.camera

import android.R.attr.path
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.option
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.snippet_toolbar.*
import java.io.File


class UploadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        toolbarText.text = "Upload your video"

        val videoPath = intent?.getStringExtra("EXTRA_VIDEO")
        val saveString = intent?.getStringExtra("EXTRA_SAVE")
        val isSave = saveString != "false"
        val media = MediaMetadataRetriever()
        media.setDataSource(videoPath)
        val duration = media.extractMetadata(METADATA_KEY_DURATION)
        seekBar.max = duration!!.toInt()
        Log.d(TAG, "onCreate: duration: $duration")
        var extractedImage = media.getFrameAtTime(0)
        thumbnail.setImageBitmap(extractedImage)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                media.setDataSource(videoPath)
                extractedImage = media.getFrameAtTime(p1.toLong()*1000)
                Log.d(TAG, "onProgressChanged: time: $p1")
                thumbnail.setImageBitmap(extractedImage)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    companion object{
        private const val TAG = "UploadActivity"
    }


}