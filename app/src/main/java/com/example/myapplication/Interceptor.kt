package com.example.myapplication

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val userId: String, private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("userId", userId)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}