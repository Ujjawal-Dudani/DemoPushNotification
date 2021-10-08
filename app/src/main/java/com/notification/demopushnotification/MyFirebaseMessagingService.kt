package com.notification.demopushnotification

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val chanelId ="notification_channel"
const val channelName ="com.notification.demopushnotification"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    //generate the notification
    //attach the notification created with the custom layout
    // show the notification

    // 3 Showing The Notification
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            generateNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
        }
    }
    //2 attaching the notification
    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.notification.demopushnotification", R.layout.notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.php)
        return remoteView

    }

    // 1 generating the notification
    fun generateNotification(title: String, message: String) {

        val intent = Intent(this, MainActivity::class.java)
        // Line 16: this will clear all the activity from the current stack and put these activity at top.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Line 19: we use pending intents when we have an intent that we need to use in future
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        //channel id channel name : this are the features introduced after (Oreo) this makes notifications more clear
        // this are constants in all over the app


        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, chanelId)
                .setSmallIcon(R.drawable.php)
                .setAutoCancel(true)
                .setVibrate(
                    longArrayOf(
                        1000,
                        1000,
                        1000,
                        1000
                    )
                ) // 4 X 1000 = 1TIME VIBRATE THEN WAIT THEN AGAIN SAME PROCESS
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(chanelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }
}