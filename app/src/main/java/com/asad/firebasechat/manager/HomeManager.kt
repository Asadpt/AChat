package com.asad.firebasechat.manager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asad.firebasechat.CallBack
import com.asad.firebasechat.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.ArrayList

class HomeManager :ViewModel() {


    private var fireStoreDB = Firebase.firestore
    private var mStorageRef = FirebaseStorage.getInstance().reference

    var userData = MutableLiveData<UserData>()
    var userList = MutableLiveData<List<UserData>>()



    fun getUserList(callBack: CallBack){
        fireStoreDB.collection("Users").get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val users: MutableList<UserData> = ArrayList()

                for (document in task?.result!!) {

                    var user = document?.toObject(UserData::class.java)
                    users.add(user!!)

                }
                callBack.onSuccess(users)
            }else{

            }
        }
    }

    fun observeUser(){

        fireStoreDB.collection("Users").addSnapshotListener{ value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if(error != null){
                    return@addSnapshotListener
                }

            for(dc in value?.documentChanges!!){
                when (dc.getType()) {
                    DocumentChange.Type.ADDED -> {
                    }
                    DocumentChange.Type.MODIFIED -> {
                        var user = dc.document.toObject(UserData::class.java)
                        if(user.userId == Firebase.auth.currentUser?.uid)
                            return@addSnapshotListener
                        userData.value = user
                    }
                    DocumentChange.Type.REMOVED -> { }
                }

            }

        }
    }



}