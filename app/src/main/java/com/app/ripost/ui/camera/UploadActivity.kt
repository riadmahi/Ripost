package com.app.ripost.ui.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.ui.home.MainActivity
import com.app.ripost.utils.database.FirebaseCallback
import com.app.ripost.utils.database.FirebaseMethods
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.snippet_toolbar.*
import java.io.File


class UploadActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
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

        save.setOnClickListener{
            save.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE
            progress_bar.max = 100
            FirebaseMethods(this).uploadPostProcess(extractedImage!!, Uri.fromFile(File(videoPath!!)),
                    !switchPrivate.isChecked,
                    switchAuthorizeShares.isChecked,
                    switchAuthorizeComments.isChecked,
                    description.text.toString(),
                    object : FirebaseCallback {
                        override fun progressUpload(progress: Int) {
                            progress_bar.progress = progress
                        }

                        override fun onFinish() {
                            progress_bar.visibility = View.GONE
                            if(!isSave){
                                //Delete the video if is not save
                                File(videoPath).delete()
                            }
                            val intent = Intent(this@UploadActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure() {
                            progress_bar.visibility = View.GONE
                            save.visibility = View.VISIBLE
                        }
                    })
        }
        back.setOnClickListener {
            finish()
        }
    }

    companion object{
        private const val TAG = "UploadActivity"
    }


}