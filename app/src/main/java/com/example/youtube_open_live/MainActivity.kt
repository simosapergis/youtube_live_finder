package com.example.youtube_open_live

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var apiResponseTextView: TextView
    private lateinit var webView: WebView
    private val apiKey = "AIzaSyDKD7DkL6i84LCAFgA1UeQ2kWJhS4Z_I4k"
    private val channelId = "UCzDwvIL79I7G5nS6wl4OZQw" //Ενορία Αγίου Παύλου Πειραιά
//    private val channelId = "UCoMdktPbSTixAyNGwb-UYkQ" //TEST: Random channel id for test #1 a lot of upcoming live videos
   // private val channelId = "UCH5Iqij-VpBAqUNbuzBIvpw" //TEST: Upcoming live


    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart called, starting fetching next live and starting webview")
        loadApp()
    }


    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "onStop called, stopping webView, clearing cache and history")
        webView.stopLoading()
        webView.clearCache(true)
        webView.clearHistory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.youtube_player_view)
        apiResponseTextView = findViewById(R.id.apiResponseTextView)

        // Handle new task launches
        if (intent != null) {
            val action = intent.action
            val extras = intent.extras
            Log.i("MAINACTIVITY", "Launched with action: $action, extras: $extras")
        }
    }


    private fun loadApp() {
        Log.i("MainActivity", "on create LOADED")

        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)

            fetchLiveStream()
        }

    }


    private fun fetchLiveStream() {
        Log.i("MainActivity", "fetchUpcomingLiveEvents called with increased timeout 3")

        apiResponseTextView.text = "fetching"

        //TODO Better call apis synchronously. Right now we call them asynchronously and whichever finds an active stream, either live or upcoming, loads it to webview
        callApiForEventType("live")
        callApiForEventType("upcoming")
    }

    private fun callApiForEventType(eventType: String) {
        val liveStream = RetrofitInstance.api.getLiveStream(channelId = channelId, apiKey = apiKey, eventType = eventType)

        liveStream.enqueue(object : Callback<YoutubeResponse> {

            override fun onResponse(
                call: Call<YoutubeResponse>,
                response: Response<YoutubeResponse>
            ) {
                Log.i("MainActivity", "Called youtube api for eventType ${eventType} - response message is ${response.message()}")

                if (response.isSuccessful) {

                    val liveStreams = response.body()?.items
                    liveStreams?.forEach {
                        Log.i("MainActivity","Found ${eventType} Stream*: ${it.snippet.title} at ${it.snippet.publishedAt} videoId ${it.id}")
                        apiResponseTextView.text = "${eventType} Stream : ${it.snippet.title}"
                        //loadYoutubeInWebView("2ClljZaK6_A")
                        loadYoutubeInWebView(it.id.videoId)
                        return
                    }

                    Log.i("MainActivity-->", "This should not be logged >>>")
                } else {
                    Log.e("MainActivity", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<YoutubeResponse>, t: Throwable) {
                Log.e("MainActivity", "Network Error: ${t.message}")
                apiResponseTextView.text = "Network Error: ${t.message}"
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadYoutubeInWebView(videoId: String) {

        // Enable JavaScript and additional settings
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false // Allow autoplay

        // Load YouTube video with autoplay
        val htmlData = """
    <html>
    <body style="margin:0;padding:0;">
        <iframe 
            width="100%" 
            height="100%" 
            src="https://www.youtube.com/embed/${videoId}?autoplay=1" 
            frameborder="0" 
            allow="autoplay; encrypted-media" 
            allowfullscreen>
        </iframe>
    </body>
    </html>
""".trimIndent()

        webView.loadData(htmlData, "text/html", "utf-8")
    }
}