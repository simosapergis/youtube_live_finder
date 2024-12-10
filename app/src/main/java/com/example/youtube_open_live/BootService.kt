package com.example.youtube_open_live
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi

class BootService : Service() {

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create a notification and start the service in the foreground
        createNotificationChannel()
        val notification = Notification.Builder(this, "boot_service_channel")
            .setContentTitle("Boot Service")
            .setContentText("The service is running after boot")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        startForeground(1, notification)

        Log.i("ForegroundServiceType", "STARTING INTENT")
        // Launch the MainActivity

        Handler(Looper.getMainLooper()).postDelayed({
           val activityIntent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            Log.i("ForegroundServiceType", "INTENT $activityIntent")

            startActivity(activityIntent)
        }, 3000)

        // Stop the service after completing its task
       // stopForeground(true)
        //stopSelf()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "boot_service_channel",
                "Boot Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}