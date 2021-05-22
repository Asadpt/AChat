package com.asad.firebasechat

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.asad.firebasechat.utility.MyPreferences
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class MyManager {
    private var fireStoreDB = Firebase.firestore
    private var mStorageRef = FirebaseStorage.getInstance().reference

    fun sendFileToFireStorage(userData: UserData,file: File?){

        val fileUri: Uri = Uri.fromFile(file)

        val riversRef: StorageReference? = mStorageRef?.child("profileImages/"+userData.userId+"/" + userData.emailId)

        riversRef?.putFile(fileUri)
            ?.addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content

                riversRef.getDownloadUrl().addOnSuccessListener{ uri ->
                    var fileUrl = uri.toString()
                    userData?.profileImageUrl = fileUrl
                    MyPreferences.saveUser(userData)
                    updateProfileImage(userData)
                }


            }
            ?.addOnFailureListener { e->
                var exe = e
                Log.i("TAGTAG", exe.toString())
            }
    }

    private fun updateProfileImage(userData: UserData) {
        var a = userData.userId
        var feedReference = fireStoreDB?.collection("Users")
        feedReference?.document(userData.userId!!).update("profileImageUrl",userData.profileImageUrl).addOnCompleteListener {task ->
            if(task.isSuccessful){
                var a = "jahgsad"
            }else{
                var aa = task.exception?.message
            }
        }
    }

}