package com.example.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class QueryParameterInterceptor @Inject constructor(
    @ApiKey private val key: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val modifiedUrl = originalUrl.newBuilder()
            .addQueryParameter("apikey", key)
            .build()

        val modifiedRequest = originalRequest.newBuilder()
            .url(modifiedUrl)
            .build()

        return chain.proceed(modifiedRequest)
    }
}