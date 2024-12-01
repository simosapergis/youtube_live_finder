package com.example.youtube_open_live

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("BootBroadcastReceiver", "**Device booted, starting MainActivity in a minute")

            Toast.makeText(context, "**Device booted, starting MainActivity in a minute", Toast.LENGTH_LONG).show()

                runApp(context)

        }

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
