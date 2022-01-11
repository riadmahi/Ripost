package com.app.ripost.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ripost.R
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.database.FirebaseMethods
import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.Message
import com.app.ripost.utils.models.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_my_message.view.*

class MyMessageBottomSheet : BottomSheetDialogFragment() {
    private var message: Message? = null
    private var user: User? = null
    private var group: Group? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_my_message, container, false)

        message = arguments?.getParcelable<Message>("EXTRA_MESSAGE")
        user = arguments?.getParcelable<User>("EXTRA_USER")
        group = arguments?.getParcelable("EXTRA_GROUP")

        view.textMessage.text = message?.message
        view.timestampLeft.text = DateUtils().getDate(message?.dateCreated.toString())

        view.deleteMessage.setOnClickListener {
            FirebaseMethods(requireContext()).removeMessage(message!!.messageID, group!!.groupID)
            dialog?.hide()
        }
        view.reply.setOnClickListener {
            (requireActivity() as MessageActivity).showReplyBanner(message!!)
            dialog?.hide()
        }
        return view
    }
}