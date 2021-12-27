package com.app.ripost.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.app.ripost.ui.signUp.SignUpActivity
import com.app.ripost.utils.database.FirebaseMethods
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_birthday.*
import kotlinx.android.synthetic.main.fragment_birthday.view.*
import kotlinx.android.synthetic.main.snippet_toolbar.view.*
import java.time.LocalDate
import java.time.Period

class BirthdayFragment  : Fragment() {

    @SuppressLint("SetTextI18n")
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
            view.progress_bar.visibility = View.VISIBLE
            val age = getAge(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            Log.d(TAG, "onCreate: age found = $age")
            if(age >= 13) {
                if(age<16)
                    FirebaseMethods(requireContext()).updatePrivate(true)
                val date = ""+datePicker.year+"-"+datePicker.month+"-"+datePicker.dayOfMonth
                FirebaseMethods(requireContext()).updateBirthday(date)
                if(tag == "FROM_SIGN_UP")
                    (activity as SignUpActivity).openProfilePhotoFragment()
            }else{
                Toast.makeText(requireActivity(), "You must be older than 13.", Toast.LENGTH_SHORT).show()
            }
            view.progress_bar.visibility = View.GONE

        }
        view.skip.setOnClickListener {
            (activity as SignUpActivity).openProfilePhotoFragment()
        }
        return view
    }

    private fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
        return Period.between(
            LocalDate.of(year, month, dayOfMonth),
            LocalDate.now()
        ).years
    }

    companion object{
        private const val TAG = "BirthdayFragment"
    }
}