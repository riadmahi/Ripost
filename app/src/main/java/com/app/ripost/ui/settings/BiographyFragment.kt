package com.app.ripost.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.app.ripost.ui.home.MainActivity
import com.app.ripost.utils.database.FirebaseMethods
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_biography.*
import kotlinx.android.synthetic.main.fragment_biography.view.*
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class BiographyFragment : Fragment() {

    private var isSetting = false
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_biography, container, false)

        if(tag == "FROM_SIGN_UP"){
            view.toolbar.visibility = View.GONE
            view.progressView.visibility = View.VISIBLE
        }else{
            view.tvBio.visibility = View.GONE
            view.progressView.visibility = View.GONE
            view.toolbar.visibility = View.VISIBLE
            view.toolbarText.text = "Biography"
            isSetting = true

            PushDownAnim.setPushDownAnimTo(view.back).setOnClickListener {
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .remove(this)
                        .commit()
            }
            view.skip.visibility = View.GONE
            view.finish.text = "Save"
        }

        PushDownAnim.setPushDownAnimTo(view.finish).setOnClickListener {
            if (biography.text.toString() != "")
                FirebaseMethods(requireContext()).updateBiography(biography.text.toString())
            if(tag == "FROM_SIGN_UP") {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                requireActivity().finishAffinity()
                startActivity(intent)
                requireActivity().finish()
            }
        }

        view.skip.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().finishAffinity()
            startActivity(intent)
            requireActivity().finish()
        }
        return view
    }


}