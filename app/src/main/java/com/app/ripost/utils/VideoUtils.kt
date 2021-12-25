package com.app.ripost.utils

import android.content.Context
import android.widget.Toast
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.File

class VideoUtils{

    fun mergeVideos(context: Context, mVideos: MutableList<File>, filename: File){

        val numberOfMerge = mVideos.size - 1
        for(i in 0..numberOfMerge){
            val cmd = arrayOf("-y", "-i", mVideos[0].path, "-i", mVideos[i+1].path, "-filter_complex", "hstack", filename.path)
            try {
                FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                    override fun onStart() {
                    }

                    override fun onProgress(message: String?) {
                    }

                    override fun onSuccess(message: String?) {

                    }

                    override fun onFailure(message: String?) {
                        Toast.makeText(context, "Error merge videos, please reattempt.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFinish() {
                    }
                })
            } catch (e: Exception) {

            } catch (e2: FFmpegCommandAlreadyRunningException) {

            }
        }

        for(i in 0..mVideos.size){
            mVideos[i].delete()
        }

    }

}