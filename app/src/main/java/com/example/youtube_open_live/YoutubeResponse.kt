package com.example.youtube_open_live

data class YoutubeResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val id: VideoId,
    val snippet: Snippet,
)

data class VideoId(
    val videoId: String,
)

data class Snippet(
    val title: String,
    val description: String,
    val publishedAt: String,
)