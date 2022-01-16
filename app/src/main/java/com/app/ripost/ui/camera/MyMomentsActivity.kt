package com.app.ripost.ui.camera

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.ripost.R
import com.app.ripost.utils.adapters.MyMomentGridViewAdapter
import kotlinx.android.synthetic.main.activity_my_moments.*

class MyMomentsActivity : AppCompatActivity() {

    private var videos: MutableList<String> = mutableListOf()
    private var durations: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_moments)
        getAllVideosFromGallery()

        momentsGridView.horizontalSpacing = 3
        momentsGridView.verticalSpacing = 3
        close.setOnClickListener {
            finish()
        }
    }


    @SuppressLint("SetTextI18n")
    @Suppress("deprecation")
    private fun getAllVideosFromGallery(){
        videos.clear()
        val projection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID, MediaStore.Video.Media.HEIGHT,MediaStore.Video.Media.WIDTH, MediaStore.Video.Media.DURATION)
        } else {
            arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID, MediaStore.Video.Media.HEIGHT,MediaStore.Video.Media.WIDTH, "0")
        }

        val cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, null , null
        )

        if (cursor!!.moveToFirst()){
            do{
                val videoPath = cursor.getString(0)
                //val videoID = cursor.getInt(1)
                //val videoH = cursor.getInt(2)
                //val videoW = cursor.getInt(3)
                val duration = cursor.getInt(4)
                Log.d(TAG, "getAllVideosFromGallery: duration: $duration")
                if(duration < 20000) {
                    videos.add(videoPath)

                    durations.add((duration/1000).toString())
                }
            } while (cursor.moveToNext())
        }
        videos.reverse()
        if(videos.size == 0){

            momentsGridView.visibility = View.GONE
            noMoments.visibility = View.VISIBLE
        }
        momentsNumber.text = "${videos.size} Videos"
        val adapter = MyMomentGridViewAdapter(videos, durations, this, object : MyMomentGridViewAdapter.GridViewCallback {
            override fun onVideoClicked(video: String) {
                Log.d(TAG, "onVideoClicked: video clicked: $video")
                val fragment = ViewMomentFragment()
                val args = Bundle()
                args.putString("EXTRA_VIDEO", video)
                fragment.arguments = args
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, "FROM_MOMENT").commit()
            }
        })
        momentsGridView.adapter = adapter
        adapter.notifyDataSetChanged()

        cursor.close()
    }

    companion object{
        private const val TAG = "MyMomentsBottomSheet"
    }
}