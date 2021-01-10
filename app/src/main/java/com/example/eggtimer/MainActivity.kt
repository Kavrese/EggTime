package com.example.eggtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var play = false
    private var duration: Long = 18000
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
    }

    private fun stopPlay(){
        work.setImageDrawable(getDrawable(R.drawable.play))
        seekBar.progress = 0
        play = false
        timer.cancel()
    }
}