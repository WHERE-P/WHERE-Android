package com.example.where.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Service {
    @POST("user/login/")
    fun login(
        @Body body: LoginRequest
    ): Call<LoginResponse>


    @POST("user/registration/")
    fun signup(
        @Body body: UserRequest
    ): Call<UserResponse>

    @GET("user/name/")
    fun getusername(
        @Header("Authorization") accessToken: String,
        @Query("name") username: String
    ): Call<NameResponse>

    @GET("state/list/")
    fun getUsersByState(
        @Header("Authorization") accessToken: String,
        @Query("grade") grade: Int,
        @Query("group") group: Int,
        @Query("name") name: String,
        @Query("state") state: String
    ): Call<List<UserWhereData>>
}