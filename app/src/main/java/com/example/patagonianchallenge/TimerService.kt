package com.example.patagonianchallenge

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class TimerService : Service() {

    private lateinit var countDownTimer: CountDownTimer
    private val intent = Intent(COUNTDOWN_REF)

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Started Timer Service")
        countDownTimer = object : CountDownTimer(TOTAL_TIME.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                intent.putExtra("countdown", millisUntilFinished)
                sendBroadcast(intent)
            }

            override fun onFinish() {
                intent.putExtra("countdown", -1L)
                sendBroadcast(intent)
            }
        }
        countDownTimer.start()
    }

    companion object {
        const val COUNTDOWN_REF = "com.example.patagonianchallenge"
        const val TAG = "TimerService"
        //milliseconds * seconds * minutes
        const val TOTAL_TIME = 1000 * 60 * 10;
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        super.onDestroy()
    }
}