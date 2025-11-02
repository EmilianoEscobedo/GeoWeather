package com.istea.geoweather.data.api

import com.istea.geoweather.BuildConfig
import com.istea.geoweather.data.api.NetworkConfig.CONNECT_TIMEOUT
import com.istea.geoweather.data.api.NetworkConfig.READ_TIMEOUT
import com.istea.geoweather.data.api.NetworkConfig.WRITE_TIMEOUT
import com.istea.geoweather.data.util.ApiConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkConfig {
    const val CONNECT_TIMEOUT = 15L
    const val READ_TIMEOUT = 20L
    const val WRITE_TIMEOUT = 20L
}

object OpenWeatherClient {
    private const val BASE_URL = BuildConfig.OPEN_WEATHER_BASE_URL
    private const val API_KEY = BuildConfig.OPEN_WEATHER_API_KEY

    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        val originalUrl = original.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter(ApiConstants.APP_ID, API_KEY)
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()

        chain.proceed(newRequest)
    }

    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(apiKeyInterceptor)
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()

    val api: OpenWeatherService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OpenWeatherService::class.java)
    }
}