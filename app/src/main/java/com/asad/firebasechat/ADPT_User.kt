package com.asad.firebasechat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.row_user.view.*

class ADPT_User :RecyclerView.Adapter<ADPT_User.UsersHolder>() {

    var userList:MutableList<UserData> = ArrayList<UserData>()
    var context:Context? = null
    var currentUser: FirebaseUser? = Firebase.auth.currentUser
    var imgRequestOption= RequestOptions().fitCenter().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL)


    class UsersHolder(itemView: View) :RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {

        var view = LayoutInflater.from(parent?.context).inflate(R.layout.row_user,parent,false)
        context = parent.context
        return UsersHolder(view)
    }

    override fun getItemCount(): Int {
        return userList?.size
    }

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {
        var user = userList.get(position)
        if(user != null){
            if(currentUser?.uid != user.userId){
                holder?.itemView?.username?.text = user?.name
                if(user.online)
                    holder?.itemView?.onlineIndicator?.setImageResource(R.drawable.icon_online)
                else
                    holder?.itemView?.onlineIndicator?.setImageResource(R.drawable.icon_offline)

                holder?.itemView?.setOnClickListener {
                    var intent = Intent(context,ChatActivity::class.java)
                    intent.putExtra("userid",user.userId)
                    context?.startActivity(intent)

                }

                if(user?.profileImageUrl != null)
                Glide.with(context!!).load(user?.profileImageUrl?:"").apply(imgRequestOption).into(holder.itemView?.profile_image)
            }

        }
    }


}