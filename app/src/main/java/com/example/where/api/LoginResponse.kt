package com.example.where.api

data class LoginResponse(
    val access: String,
    val refresh: String,
    val user: UserInfo
)

data class UserInfo(
    val pk: Int,
    val email: String
)