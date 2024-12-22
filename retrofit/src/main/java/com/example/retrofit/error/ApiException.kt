package com.example.retrofit.error

import okio.IOException

class ApiException(code: Int, error: String?) : IOException(error)