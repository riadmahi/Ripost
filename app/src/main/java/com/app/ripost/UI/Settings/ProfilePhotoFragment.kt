package com.app.ripost.UI.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.app.ripost.UI.SignUp.SignUpActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_profile_photo.view.*

class ProfilePhotoFragment  : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_photo, container, false)
        if(tag == "FROM_SIGN_UP"){
            view.toolbar.visibility = View.GONE
            view.progressView.visibility = View.VISIBLE
        }else{
            view.progressView.visibility = View.GONE
            view.toolbar.visibility = View.VISIBLE
        }
        PushDownAnim.setPushDownAnimTo(view.next).setOnClickListener{
            (activity as SignUpActivity).openBiographyFragment()
        }
        return view
    }
}