package com.example.eggtimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var play = false
    private var duration: Long = 18000
    private var notificationManager: NotificationManager? = null
    private var timer =
        object :CountDownTimer(duration, 100){
        override fun onTick(p0: Long) {
            seekBar.progress = seekBar.progress + 100
        }
        override fun onFinish() {
            seekBar.progress = duration.toInt()
            Snackbar.make(img_egg, "Complete !", Snackbar.LENGTH_SHORT).show()
            Handler().postDelayed({
                stopPlay()
            }, 500)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(NotificationManager::class.java)
        work.setOnClickListener {
            if (!play){
                startPlay()
            }else{
                stopPlay()
            }
        }
    }

    private fun startPlay(){
        work.setImageDrawable(getDrawable(R.drawable.pause))
        play = true
        seekBar.setMax(duration.toInt())
        timer.start()

        sendNotification("You'll be eating soon", "Start Timer")
    }

    private fun stopPlay(){
        work.setImageDrawable(getDrawable(R.drawable.play))
        seekBar.progress = 0
        play = false
        timer.cancel()
    }

    private fun sendNotification(text: String, title: String) {
        val pIntent = PendingIntent.getActivity(applicationContext,
            101, Intent(applicationContext, MainActivity::class.java),
            FLAG_ONE_SHOT
        )

        val builder = NotificationCompat.Builder(
            applicationContext, "101"
        )
            .setSmallIcon(R.drawable.icon)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setContentTitle(title)
            .setContentIntent(pIntent)
            .addAction(R.drawable.icon, "Open", pIntent)
            .setPriority(PRIORITY_HIGH)
            .setContentText(text)

         if (createChannelNotification()) {
             notificationManager!!.notify(101, builder.build())
        } else {
            Snackbar.make(img_egg, "Error SDK !", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun createChannelNotification(): Boolean{
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "101",
                "EGGTIME",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"
            notificationChannel.setShowBadge(true)
            notificationManager!!.createNotificationChannel(notificationChannel)
            return true
        }
        return false
    }
}