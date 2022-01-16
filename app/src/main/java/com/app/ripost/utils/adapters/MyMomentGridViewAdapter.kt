package com.app.ripost.utils.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.ripost.R
import com.bumptech.glide.Glide
import java.io.File

class MyMomentGridViewAdapter  (val videos: MutableList<String>, val durations: MutableList<String>, val context: Context,
                                private val mGridViewCallback: GridViewCallback) : BaseAdapter() {


    interface GridViewCallback{
        fun onVideoClicked(video:String )
    }
    private var mContext: Context = context
    override fun getCount(): Int {
        return videos.size
    }

    override fun getItem(i: Int): Any {
        return videos[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }


    @SuppressLint("ViewHolder")
    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        var mView = view
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_moments_thumbnail, viewGroup, false)
        }
        val imageView: ImageView = mView!!.findViewById(R.id.thumbnail)
        val duration: TextView = mView.findViewById(R.id.time)

        duration.text = durations[i]
        Glide
            .with(mContext)
            .asBitmap()
            .load(Uri.fromFile(File(videos[i])))
            .centerCrop()
            .into(imageView)

        imageView.setOnClickListener {
            mGridViewCallback.onVideoClicked(videos[i])
        }

        return mView
    }
}