package com.asad.firebasechat.utility

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class AChatApplication : Application(),LifecycleObserver {

    private lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        MyPreferences.init(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
    var user = MyPreferences.getuser()
        if(user?.emailId == auth?.currentUser?.email)
            Firebase.firestore?.collection("Users").document(user?.userId!!).update("online",false)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        var user = MyPreferences.getuser()
        var currentUser = auth.currentUser
        if(user == null)
            return
        if(currentUser == null)
            return

        if(user?.emailId == currentUser.email) {
            try{
                Firebase.firestore?.collection("Users").document(user?.userId!!).update("online", true)
            }catch(e:Exception){
                Log.e("foreground error",e.message!!)
            }
        }
    }


}