package com.app.ripost.utils.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.ui.home.ChatFragment
import com.app.ripost.ui.home.MainActivity
import com.app.ripost.ui.message.MessageActivity
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.database.FirebaseRetrieveUserCallback
import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.thekhaeng.pushdownanim.PushDownAnim

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
        val group = mGroups[position]
        val isSeen = checkIfUserSeenTheChat(group.seenBy)
        if (!isSeen){
            holder.seenIndicator.setCardBackgroundColor(mContext.getColor(R.color.primary))
        }

        holder.timestamp.text = DateUtils().timeLeft(group.lastMessageSendAt)

        if (group.name =="null"){
            //It's not a group
            val friendUid = if(group.members[0]==FirebaseAuth.getInstance().currentUser?.uid)
                group.members[1]
            else
                group.members[0]

            var mUser: User? = null
            FirebaseMethods(mContext).retrieveUserInformationFromUID(friendUid, object: FirebaseRetrieveUserCallback{
                override fun onFinish(user: User) {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(user.photoUrl)
                            .into(holder.profilePhoto)
                    mUser = user
                }
            })

            PushDownAnim.setPushDownAnimTo(holder.chatCard).setOnClickListener {
                val intent = Intent(mContext, MessageActivity::class.java).putExtra("EXTRA_GROUP", group)
                        .putExtra("EXTRA_USER", mUser)
                        .putExtra("IS_SEEN", isSeen)
                mContext.startActivity(intent)

                holder.seenIndicator.setCardBackgroundColor(mContext.getColor(R.color.dark))

            }

        }



    }

    private fun checkIfUserSeenTheChat(seen: MutableList<String>): Boolean{
        var isFind = false
        var i=0
        while(i<seen.size && !isFind){
            if (seen[i]==FirebaseAuth.getInstance().uid)
                isFind = true
            i++
        }
        return isFind
    }

    override fun getItemCount(): Int {
        return mGroups.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profilePhoto = itemView.findViewById<ShapeableImageView>(R.id.profile_photo)
        val chatCard = itemView.findViewById<LinearLayout>(R.id.chatCard)
        val seenIndicator = itemView.findViewById<CardView>(R.id.seenIndicator)
        val timestamp = itemView.findViewById<TextView>(R.id.timestamp)

        init{
            chatCard.setOnLongClickListener {
                (mContext as MainActivity).openChatInformationFragment()
                false
            }
        }
    }



    companion object {
        private const val TAG = "ChatRecyclerAdapter"

    }
}