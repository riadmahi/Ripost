package com.app.ripost.ui.camera

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.ripost.R
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.fragment_preview.*
import kotlinx.android.synthetic.main.fragment_preview.view.*

class PreviewFragment : Fragment() {

    private var isSaved = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        view.btnSave.setOnClickListener {

            val shapeTransformation = view.btnSave.drawable as AnimatedVectorDrawable
            isSaved = if(!isSaved) {
                shapeTransformation.start()

                true
            }else{
                shapeTransformation.reset()
                false
            }
        }
        return view
    }

    /**
     * val shapeTransformation = avd_record_indicator.drawable as AnimatedVectorDrawable
    shapeTransformation.start()

     */
}