package com.app.ripost.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.utils.adapters.ChatRecyclerAdapter
import com.app.ripost.utils.adapters.SearchUsersRecyclerAdapter
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mChatAdapter: ChatRecyclerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        PushDownAnim.setPushDownAnimTo(view.newChat).setOnClickListener {
            (activity as MainActivity).openNewChatFragment()
        }
        return view
    }
}