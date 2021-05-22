package com.asad.firebasechat.firebase

import android.util.Log
import com.asad.firebasechat.retrofit.ApiClient
import com.asad.firebasechat.retrofit.ApiServices
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendNotification() {

    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey = "key=AAAA-qvzf28:APA91bFyuDbg37pYiuaxdwqDKa88Ny4hHSraQ3UW2Yb_HZpBrRQrQ_KyHK-fdMpOp67wlUVlCrOL9s3Wsc_yVxMbEBicZqLyiOU72tvf2ySeY9PYxors6lvXh7qbwPPI_iRql_h4WB2V"
    var apiServices:ApiServices? = null

    fun sendNotification(userFcmToken:String,title:String,body:String){
        if(apiServices == null)
            apiServices= ApiClient().getClient()?.create(ApiServices::class.java)
        var josnBody=jsonBodyForNotification(userFcmToken,title,body)
        var call: Call<JSONObject>? = null
        call = apiServices!!.sendNotificationMessage("application/json",fcmServerKey, josnBody!!)
        call.enqueue(object : Callback<JSONObject> {
            override fun onFailure(call: Call<JSONObject>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<JSONObject>?, response: Response<JSONObject>?) {}
        })
    }

    private fun jsonBodyForNotification(userFcmToken:String,title:String,body:String): RequestBody? {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        try {

            val mainObj = JSONObject()
                mainObj.put("to", userFcmToken)
                val notiObject = JSONObject()
                notiObject.put("title", title)
                notiObject.put("body", body)
                mainObj.put("notification", notiObject)
            return RequestBody.create(JSON, mainObj.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return null
    }


}