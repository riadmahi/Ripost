package com.app.ripost.utils.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.ripost.ui.video.VideoFragment

class VideoPagerAdapter(private var videos: MutableList<String>, fm: FragmentManager,
                        lifecycle: Lifecycle):
        FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return videos.size
    }


    override fun createFragment(position: Int): Fragment {
        return VideoFragment(videos[position])
    }



}