package com.nf.stealthforward.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nf.stealthforward.config.Config
import com.nf.stealthforward.service.BackgroundService

class BootCompletedReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        Config.load(context)
        BackgroundService.start(context);
    }
}
