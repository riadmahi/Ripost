package com.app.ripost.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.app.ripost.ui.signUp.SignUpActivity
import com.app.ripost.utils.database.FirebaseMethods
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_display_name.view.*
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class DisplayNameFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_display_name, container, false)

        if(tag == "FROM_SIGN_UP"){
            view.toolbar.visibility = View.GONE
            view.progressView.visibility = View.VISIBLE

        }else{
            view.progressView.visibility = View.GONE
            view.toolbar.visibility = View.VISIBLE
            view.toolbarText.text = "Display name"
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
            view.progress_bar.visibility = View.VISIBLE
            FirebaseMethods(requireContext()).updateDisplayName(view.displayName.text.toString())
            view.progress_bar.visibility = View.GONE
            if(tag == "FROM_SIGN_UP")
                (activity as SignUpActivity).openBirthdayFragment()
        }
        PushDownAnim.setPushDownAnimTo(view.skip).setOnClickListener {
            (activity as SignUpActivity).openBirthdayFragment()
        }
        return view
    }


}