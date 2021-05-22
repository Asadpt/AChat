package com.asad.firebasechat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asad.firebasechat.R

class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}