package com.app.ripost.ui.search

import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ripost.R
import com.app.ripost.utils.adapters.SearchUsersRecyclerAdapter
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.database.FirebaseRetrieveUserCallback
import com.app.ripost.utils.models.User
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mUsersAdapter: SearchUsersRecyclerAdapter? = null
    private var mUsersFound: MutableList<User> = mutableListOf()
    private lateinit var mUserFoundRecycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        mUserFoundRecycler = view.searchUsersRecycler
        PushDownAnim.setPushDownAnimTo(view.back).setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
        }

        view.searchInput.requestFocusWithKeyboard()
        setAdapter()
        view.searchInput.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchUsers(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        return view
    }

    private fun searchUsers(query: String){
        mUsersFound.clear()
        FirebaseMethods(requireContext()).getCorrespondingUsersByUsername(query, object : FirebaseRetrieveUserCallback{
            override fun onFinish(user: User) {
                if(!checkIfUserInList(user.uid)) {
                    mUsersFound.add(user)
                    setAdapter()
                }
            }
        })

    }

    private fun checkIfUserInList(uid: String): Boolean{
        var found = false
        var i =0
        while(!found && i<mUsersFound.size){
            if(mUsersFound[i].uid == uid)
                found = true
            i++
        }
        return found
    }

    private fun setAdapter(){
        layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        if (mUsersAdapter == null){
            mUserFoundRecycler.layoutManager = layoutManager
            mUsersAdapter = SearchUsersRecyclerAdapter(requireContext(), mUsersFound)
            mUserFoundRecycler.adapter = mUsersAdapter
        }else{
            mUsersAdapter!!.notifyDataSetChanged()
        }
    }


    fun EditText.requestFocusWithKeyboard() {
        post {
            dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
            setSelection(length())
        }
    }
}