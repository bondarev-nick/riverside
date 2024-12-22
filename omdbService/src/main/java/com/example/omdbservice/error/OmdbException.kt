package com.example.omdbservice.error

import com.example.retrofit.error.ApiException

class OmdbException(message: String?) : Exception(message)

fun ApiException.toOmdbException() = OmdbException(message)