package com.example.retrofit.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("Response") val response: String,
    @SerialName("Error") val error: String
)