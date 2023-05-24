package ar.com.repo

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import defaultPrefs
import set
import kotlin.random.Random

class RepoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        val prefs = defaultPrefs(this)
        prefs["token"] = token

        val url = "https://app.repo.com.ar/android"
        val uri = Uri.parse(url).buildUpon()
            .appendQueryParameter("token", token)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.setPackage("ar.com.repo")
        intent.component = ComponentName(this, RepoLauncherActivity::class.java)

        startActivity(intent)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        var url = ""
        if (message.data.containsKey("destino")) {
            url = message.data["destino"].toString()
        }
        if (url.isEmpty()) {
            url = "https://app.repo.com.ar/android"
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.setPackage("ar.com.repo")
        intent.component = ComponentName(this, RepoLauncherActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, resources.getString(R.string.channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["body"])
                .setColor(resources.getColor(R.color.notification))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notification = notificationBuilder.build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random(System.currentTimeMillis()).nextInt(), notification)
    }

    override fun handleIntentOnMainThread(intent: Intent?): Boolean {
        return super.handleIntentOnMainThread(intent)
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
    }
}