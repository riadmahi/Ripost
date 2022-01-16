package com.app.ripost.ui.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import kotlinx.android.synthetic.main.fragment_view_moment.view.*
import java.io.File

class ViewMomentFragment : Fragment() {




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_moment, container, false)

        val videoPath = arguments?.getString("EXTRA_VIDEO")

        Log.d(TAG, "onCreateView: video: $videoPath")
        view.videoView.setVideoURI(Uri.fromFile(File(videoPath!!)))
        view.videoView.setOnPreparedListener {
            it.isLooping = true
        }
        view.videoView.start()

        view.back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        view.selectVideo.setOnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
                    .putExtra("EXTRA_VIDEO", videoPath)
                    .putExtra("EXTRA_SAVE", "true")
            startActivity(intent)
        }
        return view
    }

    companion object{
        private const val TAG = "ViewMomentFragment"
    }


}