package com.app.ripost.ui.message

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ripost.R
import com.app.ripost.utils.adapters.ChatRecyclerAdapter
import com.app.ripost.utils.adapters.ViewMembersRecyclerAdapter
import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.User
import kotlinx.android.synthetic.main.fragment_view_members.view.*
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class ViewMembersFragment : Fragment() {


    private var mUsers: MutableList<User> = mutableListOf()
    private var mGroup : Group? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_members, container, false)

        mGroup = arguments?.getParcelable("EXTRA_GROUP")
        Log.d(TAG, "onCreateView: group =$mGroup")
        mUsers = arguments?.getParcelableArrayList("EXTRA_ARRAY_USER")!!
        Log.d(TAG, "onCreateView: mUsers found = $mUsers")
        view.toolbarText.text = "Members"

        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        view.viewMembersRecycler.layoutManager = layoutManager
        val adapter = ViewMembersRecyclerAdapter(requireContext(), mGroup!!, mUsers)
        view.viewMembersRecycler.adapter = adapter

        view.back.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
        }
        return view
    }

    companion object{
        private const val TAG = "ViewMembersFragment"
    }
}