package com.nf.stealthforward.listener

interface SmsListener {
    fun onSmsReceived(sender: String, body: String)
}