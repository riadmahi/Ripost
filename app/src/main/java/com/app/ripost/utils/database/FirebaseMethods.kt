package com.app.ripost.utils.database

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.app.ripost.R
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.StringManipulation
import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.Message
import com.app.ripost.utils.models.Post
import com.app.ripost.utils.models.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


class FirebaseMethods(private val context: Context) {

    private val auth = Firebase.auth
    private val uid = auth.uid.toString()
    private val db = FirebaseFirestore.getInstance()


    /**
     * @param username
     * Check if the username already exists in the db
     */
    fun checkIfUsernameExist(username: String, callback: FirebaseCallbackBool) {
        var usernameExist = false
        val userRef = db.collection(context.getString(R.string.dbname_users))
        val query: Query = userRef.whereEqualTo(
                context.getString(R.string.field_username),
                username
        )
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (documentSnapshot in task.result!!) {
                    val user = documentSnapshot.getString(context.getString(R.string.field_username))
                    if (user.equals(username)) {
                        Log.d(TAG, "Username already exists")
                        callback.onSuccess(true)
                        usernameExist = true
                        break
                    }
                }
                if(!usernameExist)
                    callback.onSuccess(false)
            }
            if (task.result!!.size() == 0) {
                Log.d(TAG, "User not exists")
            }
        }
    }

    fun updateDisplayName(displayName: String){
        db.collection(context.getString(R.string.dbname_users)).document(uid).update(
                context.getString(
                        R.string.field_display_name
                ), displayName
        )
    }

    fun updateBirthday(birthday: String){
        db.collection(context.getString(R.string.dbname_users)).document(uid).update(
                context.getString(
                        R.string.field_birthday
                ), birthday
        )
    }

    fun updateBiography(biography: String){
        db.collection(context.getString(R.string.dbname_users)).document(uid).update(
                context.getString(
                        R.string.field_biography
                ), biography
        )
    }

    fun updatePrivate(private: Boolean){
        db.collection(context.getString(R.string.dbname_users)).document(uid).update(
                context.getString(
                        R.string.field_private
                ), private
        )
    }

    fun updateToken(token: String){
        db.collection(context.getString(R.string.dbname_users)).document(uid).update(
                context.getString(
                        R.string.field_token
                ), token
        )
    }

    fun uploadProfilePhoto(imgUri: Uri, callback: FirebaseCallback){
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
            .child("USERS/$uid/PROFILE_PHOTO")
        val uploadTask: UploadTask?
        uploadTask = storageReference.putFile(imgUri)//Uri.fromFile(File(imgUri))
        uploadTask.addOnSuccessListener(OnSuccessListener {
            storageReference.downloadUrl
                    .addOnSuccessListener { url ->
                        Log.d(TAG, "uploadProfilePhoto: profile photo upload successful")
                        db.collection(context.getString(R.string.dbname_users))
                                .document(uid).update(
                                        context.getString(R.string.field_photo_url),
                                        url.toString()
                                )
                        callback.onFinish()
                    }
        }).addOnFailureListener {
            Log.w(TAG, "uploadProfilePhoto: profile photo upload failed.")
            Toast.makeText(context, "Upload photo failed, please reattempt.", Toast.LENGTH_SHORT).show()
            callback.onFailure()
        }.addOnProgressListener {
            val progress: Double =
                (100.0 * it.bytesTransferred )/ it.totalByteCount
            callback.progressUpload(progress.toInt())
        }
    }

    fun getUserInformation(callback: FirebaseRetrieveUserCallback) {
        val docRef = db.collection(context.getString(R.string.dbname_users)).document(uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if(documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(User::class.java)!!
                Log.d(TAG, "getUserInformation: user $user")
                callback.onFinish(user)
            }
        }
    }


    fun retrieveUserInformationFromUID(userID: String, callback: FirebaseRetrieveUserCallback) {
        val docRef = db.collection(context.getString(R.string.dbname_users)).document(userID)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if(documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(User::class.java)!!
                Log.d(TAG, "getUserInformation: user $user")
                callback.onFinish(user)
            }
        }
    }




    fun getCorrespondingUsersByUsername(username: String, callback: FirebaseRetrieveUserCallback){
        db.collection(context.getString(R.string.dbname_users))
            .whereGreaterThanOrEqualTo(context.getString(R.string.field_username), username)
            .whereLessThanOrEqualTo(context.getString(R.string.field_username), "$username\uF7FF")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    callback.onFinish(user)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }

    fun checkIfIsFollower(userID: String, callback: FirebaseCallbackSuccess){

        db.collection(context.getString(R.string.dbname_followers))
                .document(userID)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                       val document = it.result
                        if(document.exists())
                            if (document.get(uid) != null){
                                Log.d(TAG, "checkIfIsFollower: SUCESS")
                                callback.onSuccess()
                            }
                    }
                }
    }

    fun addFollower(userID: String){
        db.collection(context.getString(R.string.dbname_followers)).document(userID).update(uid, DateUtils().getTimestamp())
        db.collection(context.getString(R.string.dbname_users)).document(userID).update(
                context.getString(R.string.field_followers), FieldValue.increment(1)
        )
    }

    fun addFollowing(userID: String){
        db.collection(context.getString(R.string.dbname_following)).document(uid).update(userID, DateUtils().getTimestamp())
        db.collection(context.getString(R.string.dbname_users)).document(uid).update(
                context.getString(R.string.field_following), FieldValue.increment(1)
        )
    }

    fun removeFollower(userID: String){
        db.collection(context.getString(R.string.dbname_followers)).document(userID).update(uid, FieldValue.delete())
        db.collection(context.getString(R.string.dbname_users)).document(userID).update(
                context.getString(R.string.field_followers), FieldValue.increment(-1)
        )
    }

    fun removeFollowing(userID: String){
        db.collection(context.getString(R.string.dbname_following)).document(uid).update(userID, FieldValue.delete())
        db.collection(context.getString(R.string.dbname_users)).document(uid).update(
                context.getString(R.string.field_following), FieldValue.increment(-1)
        )
    }

    fun askPermissionToFollow(userID: String){
        val id = db.collection("Notifications").document().id
        //uid|dateCreated|request(askpermission, like...)
        val permission = uid +"|"+DateUtils().getTimestamp()+"|ASK_FOLLOW"

        db.collection(context.getString(R.string.dbname_notifications)).document(userID).update(id, permission)

        db.collection(context.getString(R.string.dbname_wait_list)).document(uid).update(userID, DateUtils().getTimestamp())
    }


    fun waitTheFollowPermission(userID: String, callback: FirebaseCallbackSuccess){
        db.collection(context.getString(R.string.dbname_wait_list)).document(userID).get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val document = it.result
                        if(document.exists())
                            if (document.get(uid) != null){
                                callback.onSuccess()
                            }
                    }
                }
    }

    fun removeUserInWaitList(userID: String){
        db.collection(context.getString(R.string.dbname_wait_list)).document(userID).update(uid, FieldValue.delete())
    }

    fun addFriend(userID: String){
        db.collection(context.getString(R.string.dbname_friends)).document(uid).update(userID, DateUtils().getTimestamp())
        db.collection(context.getString(R.string.dbname_friends)).document(userID).update(uid, DateUtils().getTimestamp())
        //Start a new conversation
        val id = db.collection("Groups").document().id
        val members : ArrayList<String> = ArrayList()
        members.add(uid)
        members.add(userID)
        val seen : ArrayList<String> = ArrayList()
        val colors : ArrayList<String> = ArrayList()
        colors.add("$uid|#19D099")
        colors.add("$userID|#C7B4FF")
        val group = Group(id, uid, DateUtils().getTimestamp(), members, "New conversation", seen, "", "null", DateUtils().getTimestamp(), seen)
        db.collection(context.getString(R.string.dbname_groups)).document(id).set(group)
    }
    fun removeFriend(userID: String){
        db.collection(context.getString(R.string.dbname_friends)).document(userID).update(uid, FieldValue.delete())
        db.collection(context.getString(R.string.dbname_friends)).document(uid).update(userID, FieldValue.delete())
    }

    fun isFollowingMe(userID: String, callback: FirebaseCallbackSuccess){
        db.collection(context.getString(R.string.dbname_following)).document(userID).get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val document = it.result
                        if(document.exists())
                            if (document.get(uid) != null)
                                callback.onSuccess()
                    }
                }
    }

    fun getAllUserFriends(callback: FirebaseCallbackFriends){

        val mFriends : MutableList<String> = mutableListOf()
        db.collection(context.getString(R.string.dbname_friends)).document(uid).get().addOnCompleteListener {
            if (it.isSuccessful){
                val map: Map<String, Any> = it.result.data as Map<String, Any>
                for ((key) in map) {
                    mFriends.add(key)
                    Log.d(TAG, "getAllUserFriends: key = $key")
                }

                callback.onSuccess(mFriends)
            }
        }

    }

    fun getAllUserGroups(callback: FirebaseCallbackGroups){
        db.collection(context.getString(R.string.dbname_groups)).whereArrayContains(context.getString(R.string.field_members), uid).get().addOnSuccessListener{ groups->
            Log.d(TAG, "getAllUserGroups: success")
            Log.d(TAG, "getAllUserGroups: ${groups.metadata}, ${groups.size()}")
            val mGroups: MutableList<Group> = mutableListOf()
            for(group in groups){
                val groupFind = group.toObject(Group::class.java)
                Log.d(TAG, "getAllUserGroups: group find : $groupFind")
                mGroups.add(groupFind)
                Log.d(TAG, "getAllUserGroups: $mGroups")
            }

            callback.onSuccess(mGroups)

        }
    }

    fun sendMessage(groupID: String, msg: Message){
        db.collection(context.getString(R.string.dbname_messages)).document(groupID).collection("Message").document(msg.messageID).set(msg)
    }



    fun getMessageInRealTime(groupID: String, messages: MutableList<Message>, callback: FirebaseCallbackMsg){
        db.collection(context.getString(R.string.dbname_messages))
                .document(groupID).collection("Message").orderBy(context.getString(R.string.field_date_created)).addSnapshotListener{ snapshots, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                //New Message
                                messages.add(dc.document.toObject(Message::class.java))
                            }
                            DocumentChange.Type.MODIFIED -> {
                                modifyMessageInList(messages, dc.document.toObject(Message::class.java))
                            }
                            DocumentChange.Type.REMOVED -> messages.remove(dc.document.toObject(Message::class.java))
                        }
                    }
                    callback.onSuccess(messages)

        }
    }

    private fun modifyMessageInList(messages: MutableList<Message>, newMessage: Message){
        var msgFind = false
        var i = 0
        while(i<messages.size && !msgFind){
            if(messages[i].dateCreated == newMessage.dateCreated)
                msgFind = true
            i++
        }
    }

    fun setMessageSeenInGroup(group: Group){
        db.collection(context.getString(R.string.dbname_groups))
                .document(group.groupID).update("seenBy", FieldValue.arrayUnion(uid))
    }


    fun updateLastGroupMessage(groupID: String, msg: Message){
        db.collection(context.getString(R.string.dbname_groups)).document(groupID).update("recentMessage", msg.message)
        db.collection(context.getString(R.string.dbname_groups)).document(groupID).update("lastMessageSendAt", msg.dateCreated)
    }

    fun removeModerator(groupID: String, moderator: String){
        db.collection(context.getString(R.string.dbname_groups)).document(groupID).update("moderators", FieldValue.arrayRemove(moderator))
    }

    fun addModerator(groupID: String, moderator: String){
        db.collection(context.getString(R.string.dbname_groups)).document(groupID).update("moderators", FieldValue.arrayUnion(moderator))
    }

    fun deleteGroup(groupID: String){
        deleteAllGroupMessages(groupID)
        db.collection(context.getString(R.string.dbname_groups)).document(groupID).delete()
    }

    fun leaveGroup(group: Group){
        val uidNewCreator = if(group.members[0] == uid) group.members[1] else group.members[0]
        when {
            group.members.size == 2 -> {
                //Delete the group.
                deleteAllGroupMessages(group.groupID)
                db.collection(context.getString(R.string.dbname_groups)).document(group.groupID).delete()
            }
            uid==group.createdBy -> {
                //Transmit the creator role
                db.collection(context.getString(R.string.dbname_groups)).document(group.groupID).update("createdBy", uidNewCreator)
                db.collection(context.getString(R.string.dbname_groups)).document(group.groupID).update("members", FieldValue.arrayRemove(uid))
            }
            else -> {
                db.collection(context.getString(R.string.dbname_groups)).document(group.groupID).update("members", FieldValue.arrayRemove(uid))
            }
        }
    }

    fun kickUserInGroup(userKickID: String, groupID: String){
        db.collection(context.getString(R.string.dbname_groups)).document(groupID).update("members", FieldValue.arrayRemove(userKickID))
    }

    fun removeMessage(messageID: String, groupID: String){
        db.collection(context.getString(R.string.dbname_messages)).document(groupID).collection("Message").document(messageID).delete()
    }

    fun findMessageThanksToID(groupID: String, messageID: String): String{
        return ""
    }

    private fun deleteAllGroupMessages(groupID: String){
        db.collection(context.getString(R.string.dbname_messages))
                .document(groupID).delete()
    }

    fun getRandomID(): String{
        return db.collection(context.getString(R.string.dbname_messages)).document().id
    }


    fun uploadPostProcess(thumbInBitmap: Bitmap, videoUri: Uri,
    public: Boolean, share: Boolean, comments: Boolean, description: String, callback: FirebaseCallback){
        val id = getRandomID()
        var progress = 0.0
        val baos = ByteArrayOutputStream()
        thumbInBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
                .child("POSTS/${id}/THUMBNAIL")
        val uploadTask: UploadTask?
        uploadTask = storageReference.putBytes(data)
        uploadTask.addOnSuccessListener(OnSuccessListener {
            storageReference.downloadUrl
                    .addOnSuccessListener { photoUrl ->
                        Log.d(TAG, "uploadPostProcess: upload thumbnail successful")
                        val storageReference2: StorageReference = FirebaseStorage.getInstance().reference
                                .child("POSTS/${id}/VIDEO")
                        val uploadTask2 = storageReference2.putFile(videoUri)

                        uploadTask2.addOnSuccessListener{
                            storageReference2.downloadUrl
                                    .addOnSuccessListener { url ->
                                        Log.d(TAG, "uploadPostProcess: upload video successful")
                                        val post = Post(
                                                id,
                                                photoUrl.toString(),
                                                description,
                                                uid,
                                                url.toString(),
                                                0,
                                                0,
                                                0,
                                                0,
                                                DateUtils().getTimestamp(),
                                                public,
                                                context.resources.configuration.locale.displayCountry,
                                                comments,
                                                share,
                                                StringManipulation().getTags(description)
                                        )

                                        db.collection(context.getString(R.string.dbname_posts)).document(id).set(post)
                                        callback.onFinish()


                                    }
                        }.addOnFailureListener {
                            Log.w(TAG, "uploadPostProcess: upload video failed.")
                            Toast.makeText(context, "Upload video failed, please reattempt.", Toast.LENGTH_SHORT).show()
                            //DELETE THE VIDEO THUMBNAIL IF THE VIDEO DOWNLOAD FAIL.
                            FirebaseStorage.getInstance().reference
                                    .child("POSTS/${id}/THUMBNAIL").delete()
                            callback.onFailure()
                        }.addOnProgressListener {
                            progress  =
                                    (100.0 * it.bytesTransferred )/ it.totalByteCount
                            callback.progressUpload(progress.toInt())
                        }


                    }
        }).addOnFailureListener {
            Log.w(TAG, "uploadProfilePhoto: profile photo upload failed.")
            Toast.makeText(context, "Upload video failed, please reattempt.", Toast.LENGTH_SHORT).show()
            callback.onFailure()
        }.addOnProgressListener {
            progress  =
                    (100.0 * it.bytesTransferred )/ it.totalByteCount
            callback.progressUpload(progress.toInt())
        }

    }



    companion object{
        private const val TAG = "FirebaseMethods"
    }
}