package com.asad.firebasechat.retrofit

import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {

    @POST("fcm/send")
    fun sendNotificationMessage(@Header("content-type") contentType: String,@Header("authorization") accessToken: String, @Body requestBody : RequestBody?): Call<JSONObject>
}