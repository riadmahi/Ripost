package com.app.ripost.UI.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import kotlinx.android.synthetic.main.fragment_biography.view.*
class BiographyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_biography, container, false)

        if(tag == "FROM_SIGN_UP"){
            view.toolbar.visibility = View.GONE
            view.progressView.visibility = View.VISIBLE
        }else{
            view.progressView.visibility = View.GONE
            view.toolbar.visibility = View.VISIBLE
        }
        return view
    }
}