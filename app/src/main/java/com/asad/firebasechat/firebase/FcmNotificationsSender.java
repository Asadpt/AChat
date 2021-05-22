package com.asad.firebasechat.firebase;

import android.app.Activity;
import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender  {

    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;


//    private RequestQueue requestQueue;
//    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
//    private final String fcmServerKey ="AAAA-qvzf28:APA91bFyuDbg37pYiuaxdwqDKa88Ny4hHSraQ3UW2Yb_HZpBrRQrQ_KyHK-fdMpOp67wlUVlCrOL9s3Wsc_yVxMbEBicZqLyiOU72tvf2ySeY9PYxors6lvXh7qbwPPI_iRql_h4WB2V";

    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;


    }





//    public void SendNotifications() {
//
//        requestQueue = Volley.newRequestQueue(mActivity);
//        JSONObject mainObj = new JSONObject();
//        try {
//            mainObj.put("to", userFcmToken);
//            JSONObject notiObject = new JSONObject();
//            notiObject.put("title", title);
//            notiObject.put("body", body);
//            notiObject.put("icon", "icon"); // enter icon that exists in drawable only
//
//
//
//            mainObj.put("notification", notiObject);
//
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                    // code run is got response
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // code run is got error
//
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//
//
//                    Map<String, String> header = new HashMap<>();
//                    header.put("content-type", "application/json");
//                    header.put("authorization", "key=" + fcmServerKey);
//                    return header;
//
//
//                }
//            };
//            requestQueue.add(request);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//    }
}
