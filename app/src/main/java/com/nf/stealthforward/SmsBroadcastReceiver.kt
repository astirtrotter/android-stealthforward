package com.nf.stealthforward

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.telephony.SmsMessage
import android.util.Log


class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SmsBroadcastReceiver", "onReceive")

        if (!"android.provider.Telephony.SMS_RECEIVED".equals(intent.action)) return;

        val bundle = intent.extras
        if (bundle != null) {
            val smsArray = bundle.get("pdus") as? Array<*> ?: return
            val sb = StringBuilder()
            var sender = ""
            for (sms in smsArray) {
                val smsMessage = getIncomingMessage(sms!!, bundle)
                sender = smsMessage.displayOriginatingAddress
                sb.append(smsMessage.displayMessageBody)
            }
//            MainActivity.inst.onReceiveSMS(sender, sb.toString())

            Toast.makeText(context, "${sender} : ${sb.toString()}", Toast.LENGTH_LONG).show()

            abortBroadcast()
        }
    }

    private fun getIncomingMessage(sms: Any, bundle: Bundle) : SmsMessage {
        val smsMessage: SmsMessage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = bundle.getString("format")
            smsMessage = SmsMessage.createFromPdu(sms as ByteArray, format)
        } else {
            smsMessage = SmsMessage.createFromPdu(sms as ByteArray)
        }
        return smsMessage
    }
}
