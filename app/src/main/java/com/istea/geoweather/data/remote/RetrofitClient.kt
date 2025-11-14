package com.istea.geoweather.data.remote

import com.istea.geoweather.BuildConfig
import com.istea.geoweather.data.api.OpenWeatherService // Asumiendo que esta es tu interfaz de API
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Cliente HTTP ÚNICO que añade la clave de API a CADA petición para OpenWeatherMap
    private val httpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Muestra el cuerpo de la petición en modo debug para facilitar la depuración
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                // Obtenemos la petición original
                val originalRequest = chain.request()
                // Obtenemos la URL original
                val originalHttpUrl = originalRequest.url

                // Añadimos el parámetro 'appid' a la URL con tu clave de API
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("appid", BuildConfig.OPEN_WEATHER_API_KEY)
                    .build()

                // Creamos una nueva petición con la URL modificada
                val requestBuilder = originalRequest.newBuilder().url(url)
                val newRequest = requestBuilder.build()

                // Procedemos con la nueva petición
                chain.proceed(newRequest)
            }
            .build()
    }

    // Instancia ÚNICA de Retrofit configurada para OpenWeatherMap
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            // URL base de la API de OpenWeatherMap
            .baseUrl("https://api.openweathermap.org/")
            .client(httpClient) // Usamos nuestro cliente personalizado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicio público para usar la API.
    // Ahora solo necesitas un servicio, ya que todo apunta a OpenWeatherMap.
    val service: OpenWeatherService by lazy {
        retrofit.create(OpenWeatherService::class.java)
    }
}
