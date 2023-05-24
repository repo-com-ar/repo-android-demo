package ar.com.repo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build

class RepoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = resources.getString(R.string.channel_id)
            val channelName = resources.getString(R.string.channel_name)
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = resources.getString(R.string.app_name)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            val group = NotificationChannelGroup(channelId, channelName)

            notificationManager.createNotificationChannelGroup(group)
            channel.group = channelId
        }
    }
}