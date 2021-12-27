package com.app.ripost.utils.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.app.ripost.R
import com.bumptech.glide.Glide

class ProfileGridViewAdapter (val photos: MutableList<String>, context: Context) : BaseAdapter() {

    private var mContext : Context = context
    override fun getCount(): Int {
        return photos.size
    }

    override fun getItem(i: Int): Any {
        return photos[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }



    @SuppressLint("ViewHolder")
    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        var mView = view
        if(mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_profile_card_video_thumbnail, viewGroup, false)
        }
        val imageView: ImageView = mView!!.findViewById(R.id.thumbnail)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(mContext)
                .asBitmap()
                .load(photos[i])
                .into(imageView)



        return mView
    }


}
