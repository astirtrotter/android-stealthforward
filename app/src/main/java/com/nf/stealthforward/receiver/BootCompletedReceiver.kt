package com.nf.stealthforward.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nf.stealthforward.service.BackgroundService

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Intent(context, BackgroundService::class.java).also { context.startService(it) }
    }
}
