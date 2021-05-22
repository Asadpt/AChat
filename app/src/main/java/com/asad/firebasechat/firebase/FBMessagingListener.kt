package com.asad.firebasechat.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.asad.firebasechat.R
import com.asad.firebasechat.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FBMessagingListener: FirebaseMessagingService() {

    lateinit var notificationManager: NotificationManager
    var notificationId = 1
    var channelId = "channel-01"
    var channelName = "Channel Name"
    var importance = NotificationManager.IMPORTANCE_HIGH

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var title = message.notification?.title
        var msg = message.notification?.body
        callNotificationBuilder()


    }

    fun callNotificationBuilder() {
        val intent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val mBuilder = NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.icon_logo).setContentTitle("asad").setContentText("hello asad")

//        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
//        stackBuilder.addNextIntent(intent)
//        val resultPendingIntent: PendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, mBuilder.build())
    }
}