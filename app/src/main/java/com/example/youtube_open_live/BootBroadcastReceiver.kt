package com.example.youtube_open_live

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

class BootBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("BootBroadcastReceiver", "**HERE WE ARE")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i("BootBroadcastReceiver", "**Wait for 10 seconds pls ")
            Handler(Looper.getMainLooper()).postDelayed({
                Log.i("BootBroadcastReceiver", "**10 seconds passed now triggering Main Activity through foreground Service ")
                val serviceIntent = Intent(context, BootService::class.java)
                context.startForegroundService(serviceIntent)
                Log.i("BootBroadcastReceiver", "**foreground Service successfully")
                Toast.makeText(context, "Loading stream...", Toast.LENGTH_LONG).show()
            }, 10000) // Delay of 10 seconds
        }
    }
}
