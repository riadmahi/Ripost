package com.app.ripost.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ripost.R
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.models.Message
import com.app.ripost.utils.models.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_friend_message.view.*

class FriendMessageBottomSheet : BottomSheetDialogFragment() {

    private var message: Message? = null
    private var user: User? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_friend_message, container, false)
        message = arguments?.getParcelable<Message>("EXTRA_MESSAGE")
        user = arguments?.getParcelable<User>("EXTRA_USER")
        view.displayName.text = user?.displayName
        view.textMessage.text = message?.message
        view.timestampLeft.text = DateUtils().getDate(message?.dateCreated.toString())
        return view
    }
}