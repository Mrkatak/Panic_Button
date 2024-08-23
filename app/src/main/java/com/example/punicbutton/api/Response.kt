package com.example.punicbutton.api

data class LoginResponse(
    val success: Boolean,
    val message: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String
)

data class MonitorResponse(
    val log: String,
    val waktu: String
)

data class RekapResponse(
    val data: List<RekapData>
)

data class RekapData(
    val waktu: String,
    val log: String,
    val status: String
)

data class StatusResponse(
    val logs: List<StatusLog>,
    val onlineUsers: List<String>
)

data class StatusLog(
    val waktu: String,
    val nomorRumah: String,
    val status: String
)