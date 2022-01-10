package com.app.ripost.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ripost.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ViewVideoBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_view_video, container, false)
        return view
    }
}