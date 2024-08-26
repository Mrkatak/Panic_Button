package com.example.punicbutton.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL_MAIN = "http://172.16.100.135/button/"
    private const val BASE_URL_ESP = "http://172.16.100.135/button/esp_iot/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofitMain = Retrofit.Builder()
        .baseUrl(BASE_URL_MAIN)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitESP = Retrofit.Builder()
        .baseUrl(BASE_URL_ESP)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiServiceMain: ApiService = retrofitMain.create(ApiService::class.java)
    val apiServiceEsp: ApiService = retrofitESP.create(ApiService::class.java)
}