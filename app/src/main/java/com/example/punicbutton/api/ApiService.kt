package com.example.punicbutton.api

import com.example.punicbutton.allclass.DeviceState
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("nomor_rumah") nomorRumah: String,
        @Field("sandi") sandi: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("registrasi.php")
    fun register(
        @Field("nomor_rumah") nomorRumah: String,
        @Field("sandi") sandi: String
    ): Call<RegisterResponse>

    @GET("monitor.php")
    fun getMonitorData(): Call<MonitorResponse>

    @GET("rekap.php")
    fun getRekapData(): Call<RekapResponse>

    @GET("status.php")
    fun getStatus(): Call<StatusResponse>

    @GET("proses.php")
    fun toggleDevice(
        @Query("board") board: Int,
        @Query("state") state: Int): Call<DeviceState>

}
