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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.bottom_sheet_friend_message.view.*

class FriendMessageBottomSheet : BottomSheetDialogFragment() {

    private var message: Message? = null
    private var user: User? = null
    private var group: Group? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_friend_message, container, false)
        message = arguments?.getParcelable<Message>("EXTRA_MESSAGE")
        user = arguments?.getParcelable<User>("EXTRA_USER")
        group = arguments?.getParcelable("EXTRA_GROUP")

        view.displayName.text = user?.displayName
        view.textMessage.text = message?.message
        view.timestampLeft.text = DateUtils().getDate(message?.dateCreated.toString())
        if (checkIfUserIsModerator() && group?.createdBy == FirebaseAuth.getInstance().uid){
            view.unPromoteModerator.visibility = View.VISIBLE
            view.unPromoteModerator.setOnClickListener {
                FirebaseMethods(requireContext()).removeModerator(group!!.groupID,  user!!.uid)
            }
        }else if(group?.createdBy == FirebaseAuth.getInstance().uid || checkIfImModerator()){
            view.promoteModerator.visibility = View.VISIBLE
            view.promoteModerator.setOnClickListener {
                FirebaseMethods(requireContext()).addModerator(group!!.groupID,  user!!.uid)
            }
        }
        return view
    }

    private fun checkIfUserIsModerator(): Boolean{
        var isFind = false
        var i=0
        while(i<group?.moderators!!.size && !isFind){
            if (group!!.moderators[i] == user?.uid)
                isFind = true
            i++
        }
        return isFind
    }

    private fun checkIfImModerator(): Boolean{
        var isFind = false
        var i=0
        while(i<group?.moderators!!.size && !isFind){
            if (group!!.moderators[i] == FirebaseAuth.getInstance().uid)
                isFind = true
            i++
        }
        return isFind
    }
}