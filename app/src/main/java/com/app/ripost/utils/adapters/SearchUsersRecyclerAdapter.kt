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
import com.app.ripost.ui.search.SearchActivity
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class SearchUsersRecyclerAdapter(private val mContext: Context, private val mUsers: MutableList<User>
)
    : RecyclerView.Adapter<SearchUsersRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == VIEW_USERS)
            return ViewHolder(
                    LayoutInflater.from(mContext).inflate(
                            R.layout.layout_item_search_user,
                            parent,
                            false
                    )
            )
        else
            return ViewHolder(
                    LayoutInflater.from(mContext).inflate(
                            R.layout.snippet_view_more_results,
                            parent,
                            false
                    )
            )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(itemCount<5 || position != itemCount-1) {
            val user = mUsers[position]
            holder.username.text = "@${user.username}"
            holder.displayName.text = user.displayName
            Glide.with(mContext)
                    .load(user.photoUrl)
                    .centerCrop()
                    .into(holder.profilePhoto)
            holder.numFollowers.text = user.followers.toString()
            if (user.biography == "")
                holder.biography.visibility = View.GONE
            else
                holder.biography.visibility = View.VISIBLE

            holder.biography.text = user.biography
            holder.userCard.setOnClickListener{
                (mContext as SearchActivity).openViewProfile(user)
            }
        }
    }

    override fun getItemCount(): Int {
        if(mUsers.size>5)
            return mUsers.size + 1
        return mUsers.size
    }



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val username = itemView.findViewById<TextView>(R.id.username)
        val displayName = itemView.findViewById<TextView>(R.id.displayName)
        val biography = itemView.findViewById<TextView>(R.id.biography)
        val profilePhoto = itemView.findViewById<ShapeableImageView>(R.id.profilePhoto)
        val numFollowers = itemView.findViewById<TextView>(R.id.numFollowers)
        val userCard = itemView.findViewById<LinearLayout>(R.id.card_search)
        init {


        }
    }

    override fun getItemViewType(position: Int): Int {
        if(itemCount<5 || position != itemCount-1)
            return VIEW_USERS
        return VIEW_LOAD_MORE
    }

    companion object{
        private const val TAG = "SearchUsersRecycler"
        private const val VIEW_USERS = 0
        private const val VIEW_LOAD_MORE = 1

    }

}