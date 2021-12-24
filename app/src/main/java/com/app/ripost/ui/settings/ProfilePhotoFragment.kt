package com.app.ripost.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import com.app.ripost.ui.signUp.SignUpActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_profile_photo.view.*
import kotlinx.android.synthetic.main.snippet_toolbar.view.*


class ProfilePhotoFragment  : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_photo, container, false)
        if(tag == "FROM_SIGN_UP"){
            view.toolbar.visibility = View.GONE
            view.progressView.visibility = View.VISIBLE
        }else{
            view.tvProfilePhoto.visibility = View.GONE
            view.progressView.visibility = View.GONE
            view.toolbar.visibility = View.VISIBLE
            view.toolbarText.text = "Profile Photo"
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
            (activity as SignUpActivity).openBiographyFragment()
        }

        PushDownAnim.setPushDownAnimTo(view.importBox).setOnClickListener {
            openGalleryForImage()
        }
        PushDownAnim.setPushDownAnimTo(view.imageView).setOnClickListener {
            openGalleryForImage()
        }
        return view
    }
    @Suppress("DEPRECATION")
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            view?.imageView?.setImageURI(data?.data) // handle chosen image
            view?.importBox?.visibility = View.GONE
            view?.imageView?.visibility = View.VISIBLE
        }
    }

    companion object{
        private const val REQUEST_CODE = 100
    }
}