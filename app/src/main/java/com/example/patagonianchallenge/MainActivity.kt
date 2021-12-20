package com.example.patagonianchallenge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.patagonianchallenge.databinding.ActivityMainBinding
import java.lang.Exception
import android.app.ActivityManager


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivityTag"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                it.extras?.let {
                    val millis = intent.getLongExtra(getString(R.string.countdown), 10000)
                    Log.i(TAG, "Minutes left: ${millis/1000/60} Seconds left: ${millis/1000}")
                    if (millis == -1L) {
                        addSessionCount(this@MainActivity)
                        Log.i(TAG, getString(R.string.countdown_finished))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(isTimerServiceRunning()) {
            stopService(Intent(this, TimerService::class.java))
            Log.i(TAG, getString(R.string.stopped_service))
        }
        registerReceiver(broadcastReceiver, IntentFilter(TimerService.COUNTDOWN_REF))
        Log.i(TAG, getString(R.string.registered_broadcast))
    }

    override fun onPause() {
        super.onPause()
        if(isTimerServiceRunning()) {
            stopService(Intent(this, TimerService::class.java))
            Log.i(TAG, getString(R.string.stopped_service))
        } else {
            startService(Intent(this, TimerService::class.java))
            Log.i(TAG, getString(R.string.started_service))
        }

    }

    override fun onStop() {
        try {
            registerReceiver(broadcastReceiver, IntentFilter(TimerService.COUNTDOWN_REF))
            Log.i(TAG, getString(R.string.registered_broadcast))
        } catch (e: Exception) {

        }
        super.onStop()
    }

    override fun onDestroy() {
        stopService(Intent(this, TimerService::class.java))
        super.onDestroy()
        Log.i(TAG, getString(R.string.stopped_service))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPref.init(this)
        viewModel.showDialog.observe(this, {
            viewModel.hideView()
            it?.let {
                if (it) createDialog(readSessionCountOnSharePref())
            }
        })

        viewModel.showView.observe(this, {
            it?.let {
                if (it) {
                    binding.textviewSessionCount.visibility = View.VISIBLE
                    binding.textviewSessionCount.text = getString(
                        R.string.sessions_counter_message,
                        readSessionCountOnSharePref()
                    )
                } else {
                    binding.textviewSessionCount.visibility = View.GONE
                }
            }
        })

        binding.buttonSeeSessionCount.setOnClickListener {
            viewModel.showSessionCount(readSessionCountOnSharePref())
        }
    }

    private fun addSessionCount(context: Context) {
        SharedPref.init(context)
        val sessionCount = readSessionCountOnSharePref()
        SharedPref.write(SharedPref.sessionCount, sessionCount + 1)
    }

    private fun readSessionCountOnSharePref() = SharedPref.read(SharedPref.sessionCount, 0)

    private fun createDialog(sessionCounter: Int) {
        AlertDialog.Builder(this).apply {
            title = getString(R.string.sessions_counter)
            setMessage(getString(R.string.sessions_counter_message, sessionCounter))
            setPositiveButton(getString(R.string.ok), null)
            setCancelable(true)
            setOnCancelListener {
                viewModel.hideDialog()
            }
            show()
        }
    }

    private fun isTimerServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (TimerService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
}