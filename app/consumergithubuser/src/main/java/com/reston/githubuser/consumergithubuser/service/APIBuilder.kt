package com.reston.githubuser.consumergithubuser.service

import com.google.gson.GsonBuilder
import com.reston.githubuser.consumergithubuser.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIBuilder {
    var apiClient: APIClient

    init {
        apiClient = retrofitBuilder().create(APIClient::class.java)
    }

    private fun okHttpClient(): OkHttpClient {
        val myClient = OkHttpClient.Builder()
        val intercept = HttpLoggingInterceptor()
        intercept.level = HttpLoggingInterceptor.Level.BODY
        return myClient
            .addInterceptor(intercept)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun retrofitBuilder(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .client(okHttpClient())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}