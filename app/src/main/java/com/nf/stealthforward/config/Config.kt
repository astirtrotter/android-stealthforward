package com.nf.stealthforward.config

import android.content.Context
import android.util.Log

object Config {
    private val TAG = Config::class.java.simpleName
    private val NAME = Config::class.java.name
    private val RECEIVER_KEY = "receiver-key"
    private val BODY_SYNTAX = "body-syntax"

    var receiverKey = "NoReceiverKey"
    var bodySyntax = ""

    private fun getSharedPreferences(context: Context)
        = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun load(context: Context) {
        Log.d(TAG, "config loaded")
        receiverKey = getSharedPreferences(context).getString(RECEIVER_KEY, receiverKey)!!
        bodySyntax = getSharedPreferences(context).getString(BODY_SYNTAX, bodySyntax)!!
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