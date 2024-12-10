package com.example.youtube_open_live

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    @GET("search")
    fun getLiveStream(
        @Query("part") part: String = "snippet",
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResults: String = "1",
        @Query("eventType") eventType: String = "upcoming",
        @Query("type") type: String = "video",
        @Query("key") apiKey: String
    ): Call<YoutubeResponse>
}