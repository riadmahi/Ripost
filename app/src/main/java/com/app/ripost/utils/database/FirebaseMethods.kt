package com.app.ripost.utils.database

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.app.ripost.R
import com.app.ripost.utils.DateUtils
import com.app.ripost.utils.models.Group
import com.app.ripost.utils.models.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.lang.reflect.Field


class FirebaseMethods(private val context: Context) {

    private val auth = Firebase.auth
    private val uid = auth.uid.toString()
    private val db = FirebaseFirestore.getInstance()


    /**
     * @param username
     * Check if the username already exists in the db
     */
    fun checkIfUsernameExist(username: String) : Boolean {
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
                        usernameExist = true
                        break
                    }
                }
            }
            if (task.result!!.size() == 0) {
                Log.d(TAG, "User not exists")
            }
        }
        return usernameExist
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

    fun uploadProfilePhoto(imgUri: Uri, callback: FirebaseCallback){
        val uid = auth.uid.toString()
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

        val docRef = db.collection(context.getString(R.string.dbname_users)).document(auth.uid.toString())
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
        val followData = hashMapOf(
                uid to DateUtils().getTimestamp()
        )
        db.collection(context.getString(R.string.dbname_followers)).document(userID).set(followData)
        db.collection(context.getString(R.string.dbname_users)).document(userID).update(
                context.getString(R.string.field_followers), FieldValue.increment(1)
        )
    }

    fun addFollowing(userID: String){
        val followData = hashMapOf(
                userID to DateUtils().getTimestamp()
        )
        db.collection(context.getString(R.string.dbname_following)).document(uid).set(followData)
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
        val data = hashMapOf(
                id to permission
        )
        db.collection(context.getString(R.string.dbname_notifications)).document(userID).set(data)

        val dataWaitList =  hashMapOf(
                userID to DateUtils().getTimestamp()
        )
        db.collection(context.getString(R.string.dbname_wait_list)).document(uid).set(dataWaitList)
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
        val data =  hashMapOf(
                userID to DateUtils().getTimestamp()
        )
        val myData =  hashMapOf(
                uid to DateUtils().getTimestamp()
        )
        db.collection(context.getString(R.string.dbname_friends)).document(uid).set(data)
        db.collection(context.getString(R.string.dbname_friends)).document(userID).set(myData)
        //Start a new conversation
        val id = db.collection("Groups").document().id
        val members : ArrayList<String> = ArrayList()
        members.add(uid)
        members.add(userID)
        val seen : ArrayList<String> = ArrayList()
        val group = Group(id, uid, DateUtils().getTimestamp(), members, "New conversation", seen)
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



    companion object{
        private const val TAG = "FirebaseMethods"
    }
}