package com.app.ripost.utils.database

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.app.ripost.R
import com.app.ripost.utils.models.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

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

    companion object{
        private const val TAG = "FirebaseMethods"
    }
}