package com.app.ripost.utils.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.utils.models.Group
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ChatRecyclerAdapter (private val mContext: Context, private val mGroups: MutableList<Group>
)
    : RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                        R.layout.layout_item_chat,
                        parent,
                        false
                )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return mGroups.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {


        }
    }



    companion object {
        private const val TAG = "ChatRecyclerAdapter"

    }
}