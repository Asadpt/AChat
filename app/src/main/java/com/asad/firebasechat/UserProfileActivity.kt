package com.asad.firebasechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    var remoteUser:UserData? = null
    var imgRequestOption= RequestOptions().fitCenter().override(800,800).diskCacheStrategy(
        DiskCacheStrategy.ALL)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        supportActionBar?.hide()
        remoteUser = intent?.getSerializableExtra("remoteuser") as UserData?
        if(remoteUser != null)
            updateValue()

        back?.setOnClickListener { onBackPressed() }
    }

    private fun updateValue() {
        Glide.with(this).load(remoteUser?.profileImageUrl?:"").apply(imgRequestOption).into(profileImage)
        profileName?.text = remoteUser?.name

    }
}