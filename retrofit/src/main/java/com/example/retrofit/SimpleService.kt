package com.example.retrofit

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface SimpleService {

    @GET(".")
    suspend fun getData(
        @Query("s") query: String,
        @Query("page") page : Int = 1
    ): ResponseBody
}