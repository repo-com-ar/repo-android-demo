package ar.com.repo

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.androidbrowserhelper.trusted.LauncherActivity
import com.google.firebase.messaging.FirebaseMessaging
import defaultPrefs
import get
import set

class RepoLauncherActivity : LauncherActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        updateToken()
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun getLaunchingUrl(): Uri {
        val uri =  super.getLaunchingUrl()

        return if (uri.queryParameterNames.contains("terminal")) {
            val prefs = defaultPrefs(this)
            val token: String = prefs["token", ""] as String
            Uri.parse(uri.toString()).buildUpon()
                .clearQuery()
                .appendQueryParameter("token", token)
                .build()
        } else if (intent.extras?.containsKey("destino") == true) {
            val destino = intent.extras?.get("destino").toString()
            Uri.parse(destino)
        } else {
            uri
        }
    }

    private fun updateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("ar.com.repo", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            val prefs = defaultPrefs(this)
            prefs["token"] = token

            //launchTwa()
        })
    }
}