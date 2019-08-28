package com.nf.stealthforward.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import com.nf.stealthforward.listener.SmsListener


class SmsBroadcastReceiver : BroadcastReceiver() {
    companion object {
        var smsListener: SmsListener? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.provider.Telephony.SMS_RECEIVED" != intent.action) return;

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

            smsListener?.onSmsReceived(sender, sb.toString())

            abortBroadcast()
        }
    }

    private fun getIncomingMessage(sms: Any, bundle: Bundle) : SmsMessage {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = bundle.getString("format")
            SmsMessage.createFromPdu(sms as ByteArray, format)
        } else {
            SmsMessage.createFromPdu(sms as ByteArray)
        }
    }
}
