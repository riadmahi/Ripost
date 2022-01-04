package com.app.ripost.utils.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.ui.home.MessageActivity
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.database.FirebaseRetrieveUserCallback
import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.Message
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.layout_item_message.view.*
import org.w3c.dom.Text

class MessageRecyclerAdapter (private val mContext: Context, private val mMessages: MutableList<Message>, private val group: Group
)
    : RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                        R.layout.layout_item_message,
                        parent,
                        false
                )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = mMessages[position]
        holder.textMessage.text = msg.message
        holder.timestamp.text =  DateUtils().getDate(msg.dateCreated)
        if(msg.sendBy == FirebaseAuth.getInstance().uid){
            holder.displayName.text = "You"
            holder.displayName.setTextColor(mContext.getColor(R.color.primary))
        }else{
            holder.displayName.text = "Friend"
        }

    }

    override fun getItemCount(): Int {
        return mMessages.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textMessage = itemView.findViewById<TextView>(R.id.msgText)
        val displayName = itemView.findViewById<TextView>(R.id.displayName)
        val timestamp = itemView.findViewById<TextView>(R.id.timestamp)


        init {


        }
    }



    companion object {
        private const val TAG = "ChatRecyclerAdapter"

    }
}