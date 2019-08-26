package com.nf.stealthforward

interface SmsListener {
    fun onSmsReceived(sender: String, body: String)
}