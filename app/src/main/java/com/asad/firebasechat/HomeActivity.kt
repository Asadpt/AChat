package com.asad.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.asad.firebasechat.manager.HomeManager
import com.asad.firebasechat.utility.MyPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var adptUser = ADPT_User()
    var homeManager : HomeManager ? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        homeManager = ViewModelProvider(this).get(HomeManager::class.java)
        bindViews()
    }

    private fun bindViews() {
        userRecyclerview?.layoutManager = LinearLayoutManager(this)
        userRecyclerview.adapter = adptUser

        getUserList()
        listenerForUserList()
        homeManager?.userData?.observe(this) {
            var modifieduser = it


            for ((index, user) in adptUser.userList.withIndex()) {
                if(user?.userId == modifieduser.userId){
                    adptUser?.userList?.set(index,modifieduser)
                    adptUser?.notifyItemChanged(index)
                }
            }

        }

    }

    private fun listenerForUserList() {
        homeManager?.observeUser()
    }

    private fun logoutUser(){

        var user = MyPreferences.getuser()
        if(user != null)
            Firebase.firestore?.collection("Users").document(user?.userId!!).update("online", false)
        auth.signOut()
        MyPreferences.clearUser()
        finish()
    }

    private fun showProfile(){
        startActivity(Intent(this,ProfileActivity::class.java))

    }



    private fun getUserList() {
        homeManager?.getUserList(object :CallBack{
            override fun onFailure() {
            }

            override fun onSuccess(response: Any?) {
                if(response != null){
                    if(response is ArrayList<*>){
                        response as ArrayList<UserData>

                        var list = response
                        var newList :ArrayList<UserData> = ArrayList()
                        for(item in list){
                            if(item.userId != auth?.currentUser?.uid)
                                newList.add(item)
                        }
                        if(newList?.isNotEmpty()){
                            adptUser?.userList = newList
                        }else{
                            adptUser?.userList = ArrayList()
                        }
                        adptUser?.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
            return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout ->logoutUser()
            R.id.profile ->showProfile()
        }
        return super.onOptionsItemSelected(item)
    }
}