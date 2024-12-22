package com.example.retrofit.interceptor

import com.example.retrofit.error.ApiException
import com.example.retrofit.error.ErrorResponse
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class ErrorHandlingInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val contentType = response.body?.contentType()
        val rawBody = response.body?.string()
        kotlin.runCatching {
            Json.decodeFromString(ErrorResponse.serializer(), rawBody.orEmpty())
        }.onSuccess {
            // we successfully parsed error response
            // which means api returned exception => throw it
            throw ApiException(code = 1, error = it.error)
        }
        return response
            .newBuilder()
            .body(rawBody?.toResponseBody(contentType))
            .build()
    }
}