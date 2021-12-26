package com.app.ripost.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ripost.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_close_preview.view.*

class ClosePreviewBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_close_preview, container, false)
        view.cancel.setOnClickListener {
            dialog?.cancel()
        }

        view.restart.setOnClickListener {
            (requireActivity() as PreviewActivity).closePreview()
            dialog?.cancel()
        }

        view.removeVideo.setOnClickListener {
            (requireActivity() as PreviewActivity).removeUserVideo()
            dialog?.cancel()
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.MyDialog)
    }
}