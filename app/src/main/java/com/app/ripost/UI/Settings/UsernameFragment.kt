package com.app.ripost.UI.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class UsernameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_username, container, false)
        view.toolbarText.text = "Username"
        PushDownAnim.setPushDownAnimTo(view.back).setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
        }
        return view
    }
}