package com.app.ripost.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.app.ripost.ui.camera.CameraActivity
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException
import java.io.File


class VideoUtils{

    fun mergeVideos(context: Context, mVideos: MutableList<File>){


        for(i in 1 until mVideos.size){
            Log.d(TAG, "mergeVideos: file 0 : file://${mVideos[0].path}")
            Log.d(TAG, "mergeVideos: file 1 : file://${mVideos[1].path}")
            val cmd = arrayOf(
                "-y",
                "-i",
                mVideos[0].path,
                "-i",
                mVideos[i].path,
                "-preset",
                "ultrafast",
                "-filter_complex",
                "[0:1] [0:0] [1:1] [1:0] concat=n=2:v=1:a=1 [v] [a]",
                "-map",
                "[v]",
                "-map",
                "[a]",
                mVideos[0].path
            )


            /**
             *
             *  "-y","-i", "vid.mp4", "i", "vid2.mp4", "-preset", "ultrafast", "-filter_complex", "[0:v] [0:a] [1:v] [1:a] concat=n=2:v=1:a=1 [v] [a]","-map","[v]","-map","[a]","/output.mp4"}
             */
            try {

                val ffmpeg = FFmpeg.getInstance(context)
                try {
                    ffmpeg.loadBinary(object : LoadBinaryResponseHandler() {
                        override fun onFailure() {
                            Log.d(TAG, "onFailure: FFMPEG not supported!")
                        }
                    })
                } catch (e: FFmpegNotSupportedException) {
                    e.printStackTrace()
                }
                ffmpeg.execute(cmd, object : ExecuteBinaryResponseHandler() {
                    override fun onStart() {
                        (context as CameraActivity).showMergeProgression()
                    }

                    override fun onProgress(message: String?) {
                        Log.d(TAG, "onProgress: $message")

                    }

                    override fun onSuccess(message: String?) {

                        Toast.makeText(context, "Merging is successful! ", Toast.LENGTH_SHORT)
                            .show()
                        for (j in 1 until mVideos.size) {
                            mVideos[j].delete()
                        }

                        (context as CameraActivity).startPreviewActivity()
                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(
                            context,
                            "Error merge videos, please reattempt.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "onFailure: $message")

                    }

                    override fun onFinish() {
                        (context as CameraActivity).hideMergeProgression()
                    }
                })
            } catch (e: Exception) {

            } catch (e2: FFmpegCommandAlreadyRunningException) {

            }
        }



    }

    companion object{
        private const val TAG = "VideoUtils"
    }

}