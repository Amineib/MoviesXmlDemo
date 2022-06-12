package com.naar.myapplication.util

import com.naar.myapplication.R
import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "a48952ed90a177f26bb620a6fca58be9"
private const val API_KEY_NAME = "api_key"

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter(API_KEY_NAME, API_KEY)
            .build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}