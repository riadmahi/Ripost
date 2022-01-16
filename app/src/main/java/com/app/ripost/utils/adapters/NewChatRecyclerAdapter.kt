package com.app.ripost.utils.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.database.FirebaseRetrieveUserCallback
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.thekhaeng.pushdownanim.PushDownAnim

class NewChatRecyclerAdapter(private val mContext: Context, private val mFriends: MutableList<String>)
    : RecyclerView.Adapter<NewChatRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                        R.layout.layout_item_new_chat,
                        parent,
                        false
                )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uid = mFriends[position]
        var isChecked = false
        FirebaseMethods(mContext).retrieveUserInformationFromUID(uid, object : FirebaseRetrieveUserCallback{
            override fun onFinish(user: User) {
                holder.displayName.text = user.displayName
                Glide.with(mContext)
                        .load(user.photoUrl)
                        .into(holder.profilePhoto)
            }
        })

        val shapeTransformation = holder.btnCheck.drawable as AnimatedVectorDrawable
        PushDownAnim.setPushDownAnimTo(holder.btnCheck).setOnClickListener {
            isChecked = if(!isChecked){
                shapeTransformation.start()
                true
            }else{
                shapeTransformation.reset()
                false
            }
        }

    }

    override fun getItemCount(): Int {
        return mFriends.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profilePhoto = itemView.findViewById<ShapeableImageView>(R.id.profile_photo)
        val displayName = itemView.findViewById<TextView>(R.id.displayName)
        val btnCheck = itemView.findViewById<ImageView>(R.id.checkbox)


    }



    companion object {

    }
}