package com.nf.stealthforward.config

import android.content.Context
import android.content.SharedPreferences

object Config {
    private val NAME = Config::class.java.name
    private val RECEIVER_KEY = "receiver-key"
    private val BODY_SYNTAX = "body-syntax"

    var receiverKey = "NoReceiverKey"
    var bodySyntax = ""

    private fun getSharedPreferences(context: Context)
        = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun load(context: Context) {
        receiverKey = getSharedPreferences(context).getString(RECEIVER_KEY, receiverKey)!!
        bodySyntax = getSharedPreferences(context).getString(RECEIVER_KEY, bodySyntax)!!
    }

    fun saveReceiverKey(context: Context, key: String) {
        receiverKey = key
        getSharedPreferences(context)
            .edit()
            .putString(RECEIVER_KEY, receiverKey)
            .apply()
    }

    fun saveBodySyntax(context: Context, syntax: String) {
        bodySyntax = syntax
        getSharedPreferences(context)
            .edit()
            .putString(BODY_SYNTAX, bodySyntax)
            .apply()
    }
}