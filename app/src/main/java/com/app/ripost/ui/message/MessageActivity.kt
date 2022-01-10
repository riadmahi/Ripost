package com.app.ripost.ui.message

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.ui.settings.DisplayNameFragment
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.adapters.MessageRecyclerAdapter
import com.app.ripost.utils.database.FirebaseCallbackMsg
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.database.FirebaseRetrieveUserCallback
import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.Message
import com.app.ripost.utils.models.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_message.*
import java.util.ArrayList

class MessageActivity : AppCompatActivity() {

    private var mGroup: Group? = null
    private var mUser: User? = null
    private var mMessages: MutableList<Message> = mutableListOf()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: MessageRecyclerAdapter? = null
    private var isSeen = false

    private var mUsers : MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        Log.d(TAG, "onCreate: started.")
        isSeen = intent.getBooleanExtra("IS_SEEN", false)
        mGroup = intent.getParcelableExtra("EXTRA_GROUP")
        if(mGroup?.name == "null"){
            mUser = intent.getParcelableExtra("EXTRA_USER")
            groupName.text = mUser?.displayName
            Glide.with(this)
                    .load(mUser?.photoUrl)
                    .into(profile_photo)
        }else{
            groupName.text = mGroup?.name
        }

        if (!isSeen){
            FirebaseMethods(this).setMessageSeenInGroup(mGroup!!)
        }

        FirebaseMethods(this@MessageActivity).getMessageInRealTime(mGroup?.groupID.toString(),mMessages, object: FirebaseCallbackMsg{
            override fun onSuccess(messages: MutableList<Message>) {
                mMessages = messages
                for(user in mGroup?.members!!){
                    FirebaseMethods(this@MessageActivity).retrieveUserInformationFromUID(user, object : FirebaseRetrieveUserCallback{
                        override fun onFinish(user: User) {
                            mUsers.add(user)
                            setAdapter()
                        }
                    })
                }
            }
        })



        messageInput.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                arrow.rotation = if (p0?.length == 0) 180F else 90F
                gallery.visibility = if(p0?.length == 0) View.VISIBLE else View.GONE
                send.visibility = if(p0?.length == 0) View.GONE else View.VISIBLE

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        PushDownAnim.setPushDownAnimTo(send).setOnClickListener {
            val message = Message(messageInput.text.toString(), DateUtils().getTimestamp(), FirebaseAuth.getInstance().uid.toString())
            FirebaseMethods(this).sendMessage(mGroup?.groupID.toString(), message)
            FirebaseMethods(this).updateLastGroupMessage(mGroup?.groupID.toString(), message)
            send.hideSoftInput()
            messageInput.setText("")
        }
        val config = SlidrConfig.Builder().position(SlidrPosition.RIGHT)
            .build()

        members.setOnClickListener {
            val fragment = ViewMembersFragment()
            val args = Bundle()
            args.putParcelable("EXTRA_GROUP", mGroup)
            args.putParcelableArrayList("EXTRA_ARRAY_USER", ArrayList<Parcelable>(mUsers))
            fragment.arguments = args
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment, "FROM_MESSAGE")
                    .commit()
        }
        Slidr.attach(this, config)

    }

    private fun setAdapter(){
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        if (adapter == null){
            messageRecycler.layoutManager = layoutManager
            adapter = MessageRecyclerAdapter(this, mMessages, mGroup!!, mUsers)
            messageRecycler.adapter = adapter
        }else{
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun View.hideSoftInput() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun openFriendMessageInformationBottomSheet(user: User, message: Message){
        val bottomSheet = FriendMessageBottomSheet()
        val args = Bundle()
        args.putParcelable("EXTRA_MESSAGE", message)
        args.putParcelable("EXTRA_USER", user)
        bottomSheet.arguments = args
        bottomSheet.show(supportFragmentManager, "FRIEND_MESSAGE")
    }
    companion object{
        private const val TAG = "MessageActivity"
    }




}