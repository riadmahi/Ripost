package com.app.ripost.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.app.ripost.utils.models.User
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_username.view.*
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class UsernameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_username, container, false)
        val user = arguments?.getParcelable<User>("EXTRA_USER")
        view.toolbarText.text = "Username"
        view.username.setText(user?.username)
        PushDownAnim.setPushDownAnimTo(view.back).setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
        }
        return view
    }
}