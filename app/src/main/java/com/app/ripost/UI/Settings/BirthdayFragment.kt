package com.app.ripost.UI.Settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.app.ripost.UI.SignUp.SignUpActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_birthday.view.*
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class BirthdayFragment  : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_birthday, container, false)
        Log.d(TAG, "onCreateView: started")
        if(tag == "FROM_SIGN_UP"){
            view.toolbar.visibility = View.GONE
            view.progressView.visibility = View.VISIBLE
        }else{
            view.tvBirthday.visibility = View.GONE
            view.progressView.visibility = View.GONE
            view.toolbar.visibility = View.VISIBLE
            view.toolbarText.text = "Birthday"

            PushDownAnim.setPushDownAnimTo(view.back).setOnClickListener {
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .remove(this)
                        .commit()
            }

            view.skip.visibility = View.GONE
            view.next.text = "Save"
        }
        PushDownAnim.setPushDownAnimTo(view.next).setOnClickListener{
            (activity as SignUpActivity).openProfilePhotoFragment()
        }
        return view
    }

    companion object{
        private const val TAG = "BirthdayFragment"
    }
}