package com.app.ripost.UI.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.app.ripost.Utils.Adapters.VideoPagerAdapter
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val videos : MutableList<String> = mutableListOf()
        videos.add("https://www.tiktok.com/@guizziii/video/7012168623456406789?is_from_webapp=1&sender_device=pc&web_id6928755581160605189")
        videos.add("https://www.tiktok.com/@diesl2143/video/7042718524204305670?is_from_webapp=1&sender_device=pc&web_id6928755581160605189")
        val adapter = VideoPagerAdapter(videos, requireActivity().supportFragmentManager, requireActivity().lifecycle)
        view.homeViewPager.adapter = adapter

        return view
    }
}