package com.asad.firebasechat

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.row_chat_left.view.*
import kotlinx.android.synthetic.main.row_chat_right.view.*

class ADPT_Chat:RecyclerView.Adapter<ADPT_Chat.chatHolder>() {

    var chatList:MutableList<Chat> = ArrayList<Chat>()
    var context:Context? = null
    var user: FirebaseUser? = Firebase.auth?.currentUser

    class chatHolder(itemView: View) :RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatHolder {

        var view:View? = null

        when(viewType){
            1->{
                view = LayoutInflater.from(parent?.context).inflate(R.layout.row_chat_left,parent,false)
            }
            2->{
                view = LayoutInflater.from(parent?.context).inflate(R.layout.row_chat_right,parent,false)
            }
        }

        context = parent?.context
        return chatHolder(view!!)
    }

    override fun getItemCount(): Int {
        return chatList?.size
    }

    override fun onBindViewHolder(holder: chatHolder, position: Int) {
        var msg = chatList?.get(position)
        when(holder?.itemViewType){
            1->{
                holder?.itemView.messageTextLegt?.text = msg.text
            }
            2->{
                holder?.itemView?.messageTextRight?.text = msg.text
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        var item = chatList?.get(position)
        if(item?.senderId == user?.uid )
            return 2
        else
            return 1

    }
}