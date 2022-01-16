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
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class ViewMembersRecyclerAdapter(private val mContext: Context, private val mGroup: Group,
                                 private val members: MutableList<User>
)
    : RecyclerView.Adapter<ViewMembersRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                        R.layout.layout_item_view_member,
                        parent,
                        false
                )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = members[position]
        if (user.uid == mGroup.createdBy){
            holder.creatorStatut.visibility = View.VISIBLE
        }
        if (checkIfUserIsModerator(user.uid)){
            holder.moderatorStatut.visibility = View.VISIBLE
        }

        Glide.with(mContext)
                .load(user.photoUrl)
                .into(holder.profilePhoto)
        holder.displayName.text = user.displayName
    }


    override fun getItemCount(): Int {
        return members.size
    }

    private fun checkIfUserIsModerator(user: String): Boolean{
        var isFind = false
        var i=0
        while(i<mGroup.moderators.size && !isFind){
            if (mGroup.moderators[i] == user)
                isFind = true
            i++
        }
        return isFind
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePhoto = itemView.findViewById<ShapeableImageView>(R.id.profile_photo)
        val displayName = itemView.findViewById<TextView>(R.id.displayName)
        val creatorStatut = itemView.findViewById<LinearLayout>(R.id.creator)
        val moderatorStatut = itemView.findViewById<LinearLayout>(R.id.moderator)
        init{
        }
    }



    companion object {

    }
}