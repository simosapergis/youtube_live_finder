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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BootBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("BootBroadcastReceiver", "**HERE WE ARE")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i("BootBroadcastReceiver", "**Wait for 15 seconds pls ")
            Handler(Looper.getMainLooper()).postDelayed({
                Log.i("BootBroadcastReceiver", "**15 seconds passed now triggering Main Activity through foreground Service ")
                val serviceIntent = Intent(context, BootService::class.java)
                context.startForegroundService(serviceIntent)
                Log.i("BootBroadcastReceiver", "**foreground Service successfully")
                Toast.makeText(context, "Loading video...", Toast.LENGTH_LONG).show()
            }, 15000) // Delay of 10 seconds
        }
//        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
//            Log.i("BootBroadcastReceiver", "**Device booted, starting MainActivity in 15 seconds")
//            Handler(Looper.getMainLooper()).postDelayed({
//                Log.i("BootBroadcastReceiver", "**15 seconds passed now triggering Main Activity")
//                try {
//                    val activityIntent = Intent(context, MainActivity::class.java)
//                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context.startActivity(activityIntent)
//                    Log.i("BootBroadcastReceiver", "**Main Activity intent fired successfully")
//                }catch (e: Exception) {
//                    Log.e("BootBroadcastReceiver", "**ERROR THROWN $e")
//                }
//            }, 10000) // Delay of 2 seconds
//        }

    }

    private fun runApp(context: Context) {
        // Create an intent to launch your main activity
        Log.d("BootBroadcastReceiver", "**1 Minute passed, Running app")

        try {
            Toast.makeText(context, "**1 Minute passed, in try", Toast.LENGTH_LONG).show()
            val activityIntent = Intent(context, MainActivity::class.java)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(activityIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "**1 Minute passed, in ex $e", Toast.LENGTH_LONG).show()
        }

    }
}
