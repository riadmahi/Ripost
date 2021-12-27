package com.app.ripost.ui.video

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaItem.fromUri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DataSource
import kotlinx.android.synthetic.main.snippet_video_reaction.view.*

class VideoFragment() : Fragment() {
    @SuppressLint("RestrictedApi")

    private var mPlayer: Player? = null
    private lateinit var playerView: PlayerView
    private var isLiked = false
    private val videoURL = ""//"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        playerView = view.findViewById(R.id.videoPlayer)
        initPlayer()
        view.like.setOnClickListener{
            isLiked = if(!isLiked) {

                view.like.setMinAndMaxProgress(0.0f, 0.5f)
                view.like.playAnimation()
                true
            }else {

                view.like.setMinAndMaxProgress(0.0f, 0f)
                view.like.playAnimation()
                false
            }
        }
        return view
    }

    private fun initPlayer(){
        mPlayer = ExoPlayer.Builder(requireContext()).build()

        // Bind the player to the view.
        playerView.player = mPlayer

        //setting exoplayer when it is ready.
        mPlayer!!.playWhenReady = true

        // Set the media source to be played.
        mPlayer!!.setMediaItem(buildMediaSource())

        // Prepare the player.
        mPlayer!!.prepare()

    }

    private fun buildMediaSource(): MediaItem {
        // Create a data source factory.
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        // Create a progressive media source pointing to a stream uri.
        val mediaSource: ProgressiveMediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(fromUri(videoURL))

        return mediaSource.mediaItem
    }
    companion object{
        private const val TAG = "VideoFragment"
    }
    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        //release player when done
        mPlayer!!.stop()
        mPlayer!!.playWhenReady = false
        mPlayer!!.stop()
        mPlayer!!.seekTo(0)
        mPlayer!!.release()
        mPlayer = null
    }


    override fun onStart() {
        super.onStart()
        initPlayer()

    }

    override fun onResume() {
        super.onResume()
        initPlayer()
        Log.d(TAG, "onResume: resume")
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        Log.d(TAG, "onPause: pause")
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        Log.d(TAG, "onDestroy: destroy")
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDetach() {
        super.onDetach()
        releasePlayer()
        Log.d(TAG, "onDetach: detached")
    }

}