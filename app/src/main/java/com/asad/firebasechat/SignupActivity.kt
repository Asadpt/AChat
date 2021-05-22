package com.asad.firebasechat

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.asad.firebasechat.utility.MyPreferences
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var fireStoreDB = Firebase.firestore
    private var mStorageRef = FirebaseStorage.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        bindProgressButton(signup)
        auth = Firebase.auth
        supportActionBar?.hide()
        bindViews()
    }

    private fun bindViews() {
        signup?.setOnClickListener { signup() }
        back?.setOnClickListener { onBackPressed() }
    }

    private fun signup(){
        val email = emailID?.text?.toString()
        val password = password?.text?.toString()
        val name:String? = username?.text?.toString()

        if(name?.isEmpty() == true){
            Toast.makeText(this,"name can not be empty",Toast.LENGTH_SHORT).show()
            return
        }else if (email?.isEmpty() == true){
            Toast.makeText(this,"email can not be empty",Toast.LENGTH_SHORT).show()
            return
        }else if (password?.isEmpty() == true){
            Toast.makeText(this,"password can not be empty",Toast.LENGTH_SHORT).show()
            return
        }

        signup.showProgress {
            progressColor = Color.WHITE
            buttonText = "SIGNING UP ..."
        }

        auth?.createUserWithEmailAndPassword(email!!,password!!).addOnCompleteListener { task->
            if(task.isSuccessful){

                var ref =  "Users"
                var user = UserData()
                user.userId = auth?.currentUser?.uid
                user.emailId = auth?.currentUser?.email
                user.name = name?:""
                user?.online = true

                FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
                    if(task?.isSuccessful)
                        user.token = task?.result
                    else
                        user?.token = null

                    fireStoreDB.collection("Users").document(user.userId!!).set(user).addOnSuccessListener {
                        if(task.isSuccessful){
                            try{
                                MyPreferences.saveUser(user)
                                Toast.makeText(this,"User created",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,LoginActivity::class.java))
                                finish()
                            }catch(e:Exception){
                                Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
                            }

                        }else{
                            Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }else{
                Toast.makeText(this,task.exception?.message, Toast.LENGTH_SHORT).show()
                signup.hideProgress("SIGN UP")
            }
        }
    }
}