package com.nf.stealthforward.api

import com.nf.stealthforward.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private val BASE_URL = "https://stealthforward-c7e33.firebaseapp.com"
    private val TIMEOUT = 10L
    private var retrofit: API? = null

    val retrofitClient: API
        get() {
            if (retrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BASIC
                val okHttpClientBuilder = OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build()
                    .create(API::class.java)
            }
            return retrofit!!
        }
}