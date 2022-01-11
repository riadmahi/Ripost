package com.app.ripost.utils.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.ui.message.MessageActivity
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.Message
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth

class MessageRecyclerAdapter (private val mContext: Context, private val mMessages: MutableList<Message>, private val group: Group,
                              private val mUsers: MutableList<User>
)
    : RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == MY_MESSAGE)
            return ViewHolder(
                    LayoutInflater.from(mContext).inflate(
                            R.layout.layout_item_message_right,
                            parent,
                            false
                    )
            )
        else
            return ViewHolder(
                    LayoutInflater.from(mContext).inflate(
                            R.layout.layout_item_message_left,
                            parent,
                            false
                    )
            )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val msg = mMessages[position]
        if(position == 0){
            if(getItemViewType(position) == MY_MESSAGE){
                holder.myMessageDate.text = DateUtils().getDate(msg.dateCreated)
            }else{
                holder.friendMessageDate.text = DateUtils().getDate(msg.dateCreated)
            }
        }

        //Show the current date like Monday 22 December
        if(position in 1 until itemCount){
            val myLastMessage = mMessages[position-1]
            if(DateUtils().getDay(msg.dateCreated) == DateUtils().getDay(myLastMessage.dateCreated)){
                if(getItemViewType(position) == MY_MESSAGE){
                    holder.myMessageDate.visibility = View.GONE
                }else{
                    holder.friendMessageDate.visibility = View.GONE
                }
            }else{
                if(getItemViewType(position) == MY_MESSAGE){
                    holder.myMessageDate.text = DateUtils().getDate(msg.dateCreated)
                }else{
                    holder.friendMessageDate.text = DateUtils().getDate(msg.dateCreated)
                }
            }
        }

        //Hide Profile Photo and Time stamp for the message with the same minutes

        if(position in 0 until itemCount-1){
            val mNextMessage = mMessages[position+1]
            if(DateUtils().getTime(msg.dateCreated) == DateUtils().getTime(mNextMessage.dateCreated)
                    && msg.sendBy == mNextMessage.sendBy){
                if(getItemViewType(position) == MY_MESSAGE){
                    holder.myTimestamp.visibility = View.GONE
                    holder.myProfilePhoto.visibility = View.GONE
                }else{
                    holder.friendTimestamp.visibility = View.GONE
                    holder.friendProfilePhoto.visibility = View.GONE
                }
            }
        }

        val currentUser = getUserFromList(msg.sendBy)
        holder.msg = msg
        holder.user = currentUser
        Log.d(TAG, "onBindViewHolder: mUsers: $mUsers")
        Log.d(TAG, "onBindViewHolder: current user: $currentUser")
        if(getItemViewType(position) == MY_MESSAGE){
            holder.myTextMessage.text = msg.message
            holder.myTimestamp.text =  DateUtils().getTime(msg.dateCreated)
            Glide.with(mContext)
                    .load(currentUser.photoUrl)
                    .into(holder.myProfilePhoto)
        }else{
            holder.friendTextMessage.text = msg.message
            holder.friendTimestamp.text =  DateUtils().getTime(msg.dateCreated)
            Glide.with(mContext)
                    .load(currentUser.photoUrl)
                    .into(holder.friendProfilePhoto)
        }


    }

    private fun getUserFromList(userID: String): User{
        var i=0
        var isFind = false
        var user = User()
        while(i<mUsers.size && !isFind){
            if (mUsers[i].uid == userID) {
                isFind = true
                user = mUsers[i]
            }
            i++
        }

        return user

    }

    override fun getItemCount(): Int {
        return mMessages.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val myTextMessage = itemView.findViewById<TextView>(R.id.textMessageRight)
        val myProfilePhoto = itemView.findViewById<ShapeableImageView>(R.id.profilePhotoRight)
        val myTimestamp = itemView.findViewById<TextView>(R.id.timestampRight)
        val myMessageDate = itemView.findViewById<TextView>(R.id.messageDateRight)


        val friendTextMessage = itemView.findViewById<TextView>(R.id.textMessageLeft)
        val friendProfilePhoto = itemView.findViewById<ShapeableImageView>(R.id.friendProfilePhoto)
        val friendTimestamp = itemView.findViewById<TextView>(R.id.timestampLeft)
        val friendMessageCard = itemView.findViewById<RelativeLayout>(R.id.messageCard)
        val friendMessageDate = itemView.findViewById<TextView>(R.id.messageDateLeft)
        var user: User? = null
        var msg: Message? = null


        init {
                friendMessageCard?.setOnLongClickListener {
                    Log.d(TAG, "LONG PRESSED")
                    (mContext as MessageActivity).openFriendMessageInformationBottomSheet(
                        user!!,
                        msg!!
                    )
                    false
                }


        }
    }

    override fun getItemViewType(position: Int): Int {
        if(mMessages[position].sendBy == FirebaseAuth.getInstance().uid)
            return MY_MESSAGE
        return FRIEND_MESSAGE
    }

    companion object {
        private const val TAG = "ChatRecyclerAdapter"
        private const val MY_MESSAGE = 0
        private const val FRIEND_MESSAGE = 1
    }
}