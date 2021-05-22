package com.asad.firebasechat

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import java.util.ArrayList

class ChatManager : ViewModel() {

    private var fireStoreDB = Firebase.firestore
    private var mStorageRef = FirebaseStorage.getInstance().reference

    var chat = MutableLiveData<Chat>()
    var userdata = MutableLiveData<UserData>()
    var perticualUser = MutableLiveData<UserData>()




    fun sendMessage(roomId:String,chat:Chat){
        var ref =  "Chat" +"/"+roomId+"/"+ "messages"
        fireStoreDB.collection(ref).add(chat).addOnSuccessListener {  }
    }

    fun getMessage(roomId:String,callBack: CallBack){

        var ref =  "Chat" +"/"+roomId+"/"+ "messages"
        fireStoreDB.collection(ref).orderBy("time").get().addOnCompleteListener { task ->
            if(task?.isSuccessful){
                val messages: MutableList<Chat> = ArrayList()

                for (document in task?.result!!) {

                    var localMessageValue = document?.toObject(Chat::class.java)
                    messages.add(localMessageValue!!)

                }
                callBack.onSuccess(messages)
            }else{

            }

        }


    }



    fun addingSnapshotListener(roomId:String){
        var ref =  "Chat" +"/"+roomId+"/"+ "messages"
        fireStoreDB?.collection(ref)?.orderBy("time")?.addSnapshotListener { snapshot, e ->
            
                if (e != null) {
                    Log.w("Value text", "Listen failed.", e)
                    return@addSnapshotListener
                }
                for (dc in snapshot!!.documentChanges) {
                    when (dc.getType()) {
                        DocumentChange.Type.ADDED -> {
                            var localMessage = dc.document.toObject(Chat::class.java)
                            chat.value = localMessage
                        }
                        DocumentChange.Type.MODIFIED -> { }
                        DocumentChange.Type.REMOVED -> { }
                    }
                }
            }
    }

    fun snapShotForUser(userId:String){
        fireStoreDB?.collection("Users").document(userId!!).addSnapshotListener{snapshot: DocumentSnapshot?, e: FirebaseFirestoreException? ->
            if (e != null) {
                Log.w("Value text", "Listen failed.", e)
                return@addSnapshotListener
            }

            if(snapshot?.exists() == true){
                var user = snapshot.toObject(UserData::class.java)
                perticualUser?.value = user!!
            }


        }
    }




    fun getUserDetails(userid:String?){

        fireStoreDB?.collection("Users").document(userid!!).get().addOnCompleteListener {
            if(it.isSuccessful){
                    var user = it?.result?.toObject(UserData::class.java)
                    userdata.value = user!!
                }
            else{

            }
        }

    }


        //commit for testing




}