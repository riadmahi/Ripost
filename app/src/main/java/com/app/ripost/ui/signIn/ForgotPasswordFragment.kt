package com.app.ripost.ui.signIn

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*
import kotlinx.android.synthetic.main.snippet_toolbar.view.*

class ForgotPasswordFragment : Fragment(){

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)


        view.toolbarText.text = "Forgot my password"
        view.back.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
        }

        PushDownAnim.setPushDownAnimTo(view.reset).setOnClickListener {
            Firebase.auth.sendPasswordResetEmail(view.email.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireActivity(), "Email sent, please check your inbox.", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .remove(this)
                            .commit()
                    }else{
                        Toast.makeText(requireActivity(), "Error, we can't reset your password.\n Please call the support.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        return view
    }
}