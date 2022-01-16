package com.app.ripost.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.utils.adapters.NewChatRecyclerAdapter
import com.app.ripost.utils.database.FirebaseCallbackFriends
import com.app.ripost.utils.database.FirebaseMethods
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class NewChatFragment : Fragment() {

    private lateinit var mRecycler: RecyclerView
    private var adapter: NewChatRecyclerAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mFriends: MutableList<String> = mutableListOf()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_chat, container, false)
        mRecycler = view.findViewById(R.id.newChatRecycler)
        view.toolbarText.text = "New Chat"
        view.back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        FirebaseMethods(requireContext()).getAllUserFriends(object: FirebaseCallbackFriends{
            override fun onSuccess(friends: MutableList<String>) {
                mFriends = friends
                setAdapter()
            }
        })
        return view
    }

    private fun setAdapter(){
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        if (adapter == null){
            mRecycler.layoutManager = layoutManager
            adapter = NewChatRecyclerAdapter(requireContext(), mFriends)
            mRecycler.adapter = adapter
        }else{
            adapter!!.notifyDataSetChanged()
        }
    }
}