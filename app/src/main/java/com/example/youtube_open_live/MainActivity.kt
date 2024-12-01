package com.example.youtube_open_live

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var apiResponseTextView: TextView
    private val apiKey = "AIzaSyDKD7DkL6i84LCAFgA1UeQ2kWJhS4Z_I4k"
    //private val channelId = "UCzDwvIL79I7G5nS6wl4OZQw" //Ενορία Αγίου Παύλου Πειραιά
    //private val channelId = "UCzDwvIL79I7G5nS6wl4OZQw" //Ενορία Αγίου Παύλου Πειραιά
    private val channelId = "UCoMdktPbSTixAyNGwb-UYkQ" //Random channel id for test


    override fun onResume() {
        super.onResume()
        // Code to execute when the activity becomes visible and interactive
        Log.d("MainActivity", "onResume called")
        Toast.makeText(this, "**On Resume Called", Toast.LENGTH_LONG).show()
        // Example: Start refreshing data or resume animations
        //doStuff()
    }


    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called")
        Toast.makeText(this, "**On Pause called", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        apiResponseTextView = findViewById(R.id.apiResponseTextView)

        Log.i("MainActivity", "on create loaded")
        val videoId = "3YixFv5E8m0" // Replace with the actual video ID
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        intent.putExtra("force_fullscreen", true) // Optional: Force full-screen mode

        CoroutineScope(Dispatchers.Main).launch{
            delay(5000)

            fetchUpcomingLiveEvents()
        }


        // Start the YouTube app
        startActivity(intent)

        // Optionally, finish the app if you don't want it to remain running
        finish()
    }


    private fun fetchUpcomingLiveEvents() {
        Log.i("MainActivity", "fetchUpcomingLiveEvents called with increased timeout 3")

        apiResponseTextView.text = "fetching"
        val call = RetrofitInstance.api.getUpcomingLiveStreams(channelId = channelId, apiKey = apiKey)

        call.enqueue(object : Callback<YoutubeResponse> {

            override fun onResponse(call: Call<YoutubeResponse>, response: Response<YoutubeResponse>) {
                Log.i("MainActivity", "response message is ${response.message()}")

                if (response.isSuccessful) {
                    val liveStreams = response.body()?.items
                    liveStreams?.forEach {
                        Log.i("MainActivity", "Upcoming Live Stream: ${it.snippet.title} at ${it.snippet.publishedAt} videoId ${it.id}")
                        apiResponseTextView.text = "Upcoming Live Stream videoId: ${it.id.videoId}"
//                        launchYoutube(it.id.videoId)
                        launchYoutube("2ClljZaK6_A")
                    }
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

    private fun launchYoutube(videoId: String) {
        Log.i("MainActivity", "Launch yt with url and boot")
        Toast.makeText(this, "**Device booted, starting MainActivity right now, with yt pkg", Toast.LENGTH_LONG).show()

        //val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))

//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))

//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        intent.setPackage("com.google.android.youtube.tv")
        intent.putExtra("VIDEO_ID", videoId)
        intent.putExtra("force_fullscreen", true) // Optional: Force full-screen mode
        // Start the YouTube app
        startActivity(intent)

        // Optionally, finish the app if you don't want it to remain running
        //finish()
    }
}