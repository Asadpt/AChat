package com.asad.firebasechat.utility

import android.content.Context
import android.content.SharedPreferences
import com.asad.firebasechat.UserData
import com.google.gson.Gson

object MyPreferences {

    private const val NAME = "AChat"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)
    private val USER = "user"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun saveUser(user: UserData?) {
        val editor: SharedPreferences.Editor? = preferences?.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        editor?.putString(USER, json)
        editor?.commit()
    }

    fun getuser(): UserData? {
        val gson = Gson()
        val json: String? = preferences?.getString(USER, "")
        return gson.fromJson(json, UserData::class.java)
    }

    fun clearUser(){
        val editor: SharedPreferences.Editor? = preferences?.edit()
        editor?.putString(USER, null)
        editor?.commit()
    }





}