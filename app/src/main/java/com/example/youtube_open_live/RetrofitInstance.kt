package com.example.youtube_open_live

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    class RetryInterceptor(private val maxRetry: Int) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var attempt = 0
            var response: Response
            var success = false

            do {
                attempt++
                response = chain.proceed(chain.request())
                success = response.isSuccessful
            } while (!success && attempt < maxRetry)

            return response
        }
    }

    // Create a logging interceptor to log network requests and responses
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC // Logs the request and response body
    }

    // Create OkHttp client with logging enabled
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(RetryInterceptor(maxRetry = 3))
        .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
        .readTimeout(30, TimeUnit.SECONDS)     // Increase read timeout
        .writeTimeout(30, TimeUnit.SECONDS)    // Increase write timeout
        .build()

    val api: YoutubeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YoutubeApiService::class.java)
    }
}
