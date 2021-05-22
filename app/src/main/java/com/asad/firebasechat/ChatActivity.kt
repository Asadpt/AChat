package com.asad.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.asad.firebasechat.firebase.SendNotification
import com.asad.firebasechat.utility.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.row_user.view.*
import kotlinx.coroutines.runBlocking
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var adptChat = ADPT_Chat()
    var chatmanager = ChatManager()
    var userid:String? = null
    var remoteUser:UserData? = null
    var currentUser:FirebaseUser? = null
    var roomId:String? = null
    var imgRequestOption= RequestOptions().fitCenter().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL)
    var isListening:Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatmanager = ViewModelProvider(this).get(ChatManager::class.java)
        supportActionBar?.hide()
        userid = intent?.getStringExtra("userid")
        auth = Firebase.auth
        currentUser = auth?.currentUser
        bindViews()
    }



    private fun bindViews() {

        chatmanager.perticualUser.observe(this){
            it?.let {
                remoteUser = it
                if(it.online){onlineIndicator?.setImageResource(R.drawable.icon_online) }
                else{onlineIndicator?.setImageResource(R.drawable.icon_offline)}
                if(it?.profileImageUrl != null){ Glide.with(this@ChatActivity).load(it?.profileImageUrl).apply(imgRequestOption).into(profile_image)}
                username?.text = it?.name
                if(roomId == null){
                    var list = arrayListOf<String>(currentUser?.email?:"",it?.emailId?:"")
                    list.sort()
                    roomId = list.get(0)+list.get(1)
                    if(roomId!=null)
                    chatmanager?.addingSnapshotListener(roomId!!)

                }





            }
        }

        chatmanager?.chat?.observe(this){
            it?.let {
                adptChat?.chatList?.add(it)
                adptChat.notifyDataSetChanged()
                if(adptChat?.chatList?.size > 1)
                chatRecyclerview?.smoothScrollToPosition(adptChat?.chatList?.size -1)
            }


        }

        if(userid != null){
//            chatmanager?.userdata?.observe(this){
//                it?.let {
//                    if(it?.profileImageUrl != null)
//                        remoteUser = it
//                        Glide.with(this@ChatActivity).load(it?.profileImageUrl).apply(imgRequestOption).into(profile_image)
//                    username?.text = it?.name
//                    if(roomId == null){
//                        var list = arrayListOf<String>(currentUser?.email?:"",it?.emailId?:"")
//                        list.sort()
//                        roomId = list.get(0)+list.get(1)
//                    }
//                    if(!isListening){
//                        addSnapshotListener()
//                        isListening = true
//                    }
//                }
//            }
            chatmanager?.snapShotForUser(userid!!)
//            chatmanager?.getUserDetails(userid)

        }
        chatRecyclerview?.layoutManager = LinearLayoutManager(this)
        chatRecyclerview?.adapter = adptChat

        send?.setOnClickListener {
        sendMessage()
        }

        profileLayout?.setOnClickListener {
            var intent = Intent(this,UserProfileActivity::class.java)
            intent?.putExtra("remoteuser",remoteUser)
            startActivity(intent)

        }



    }
    //asad


    private fun addSnapshotListener() {
        chatmanager?.addingSnapshotListener(roomId!!)

    }

    private fun sendMessage() {
        if(roomId != null && (message?.text?.isNotEmpty() == true)){
            var msg = message?.text?.toString()
            var chat = Chat()
            chat.text = msg
            chat.time = System.currentTimeMillis()
            chat.senderId = currentUser?.uid
            chat?.receiverId = remoteUser?.userId
            chatmanager.sendMessage(roomId!!,chat)

            if(!(remoteUser?.online == true))
            SendNotification().sendNotification(remoteUser?.token?:"",MyPreferences?.getuser()?.name?:"",message?.text?.toString()?:"")
            message?.text = null



        }

    }

}