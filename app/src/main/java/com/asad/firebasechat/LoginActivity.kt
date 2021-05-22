package com.asad.firebasechat

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.asad.firebasechat.utility.MyPreferences
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var chatmanager :ChatManager ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        chatmanager = ViewModelProvider(this).get(ChatManager::class.java)
        auth = Firebase.auth
        bindProgressButton(login)

        bindViews()
    }

    private fun bindViews() {
        login?.setOnClickListener { loginUser() }
        go_to_signup?.setOnClickListener{(startActivity(Intent(this,SignupActivity::class.java)))}
        chatmanager?.userdata?.observe(this){
            it?.let {
                MyPreferences.saveUser(it)
                Firebase.firestore?.collection("Users").document(it?.userId!!).update("online",true)
                Toast.makeText(this@LoginActivity,"User logged in Success fully",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun loginUser() {

        var email = emailID?.text?.toString()
        var pass = password?.text?.toString()

        if(email?.isEmpty() == true){
            Toast.makeText(this,"Email can not be empty",Toast.LENGTH_SHORT).show()
            return
        }else if (pass?.isEmpty() == true){
            Toast.makeText(this,"password can not be empty",Toast.LENGTH_SHORT).show()
            return
        }

        login.showProgress{
            buttonText = "LOGING IN ..."
            progressColor = Color.WHITE
        }
        auth.signInWithEmailAndPassword(email!!,pass!!).addOnCompleteListener { task ->
            if(task.isSuccessful){

                chatmanager?.getUserDetails(auth?.currentUser?.uid)

            }else{
                Toast.makeText(this,"Something has happend",Toast.LENGTH_SHORT).show()
                login.hideProgress("LOGIN")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }


    }


    private fun reload() {
    }



}


