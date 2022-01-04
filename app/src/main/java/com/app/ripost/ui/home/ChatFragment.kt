package com.app.ripost.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.utils.adapters.ChatRecyclerAdapter
import com.app.ripost.utils.database.FirebaseCallbackGroups
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.models.Group
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {

    private lateinit var mChatRecycler: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mChatAdapter: ChatRecyclerAdapter? = null
    private var mChat: MutableList<Group> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        mChatRecycler = view.chatRecycler
        PushDownAnim.setPushDownAnimTo(view.newChat).setOnClickListener {
            (activity as MainActivity).openNewChatFragment()
        }
        FirebaseMethods(requireContext()).getAllUserGroups(object : FirebaseCallbackGroups {
            override fun onSuccess(groups: MutableList<Group>) {
                mChat = groups


                Log.d(TAG, "onSuccess: groupfind $mChat")
                setAdapter()
            }
        })


        return view
    }

    private fun setAdapter(){
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        if (mChatAdapter == null){
            mChatRecycler.layoutManager = layoutManager
            mChatAdapter = ChatRecyclerAdapter(requireContext(), mChat)
            mChatRecycler.adapter = mChatAdapter
        }else{
            mChatAdapter!!.notifyDataSetChanged()
        }
    }

    companion object{
        private const val TAG = "ChatFragment"

    }
}