package com.nf.stealthforward.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nf.stealthforward.service.BackgroundService

class RestartBackgroundServiceListener : BroadcastReceiver() {

    companion object {
        val ACTION: String = RestartBackgroundServiceListener::class.java.name
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (!ACTION.equals(intent.action)) return
        BackgroundService.start(context)
    }
}
