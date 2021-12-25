package com.app.ripost.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_search.view.back
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        view.toolbarText.text = "Forgot password"
        PushDownAnim.setPushDownAnimTo(view.back).setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
        }
        return view
    }
}