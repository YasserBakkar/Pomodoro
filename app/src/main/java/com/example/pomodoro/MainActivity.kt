package com.example.pomodoro

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import android.widget.Toast
import com.example.pomodoro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val START_TIME_IN_MILLIS: Long = 25 * 60 * 1000
    var remainingTime: Long = START_TIME_IN_MILLIS
    var timer: CountDownTimer? = null
    var isTimerRunning = false
    val REMAINING_TIME = "remainingTime"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            if (!isTimerRunning) {
                startTimer(START_TIME_IN_MILLIS)
                binding.tvTakePomodoro.text = resources.getText(R.string.keep_going)
            }
        }

        binding.tvReset.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer(startTime: Long) {
        timer = object : CountDownTimer(startTime, 1 * 1000) {

            override fun onTick(timeLeft: Long) {
                remainingTime = timeLeft
                updateTimerText()
                binding.progressBar.progress =
                    remainingTime.toDouble().div(START_TIME_IN_MILLIS.toDouble()).times(100).toInt()
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Finish!!", Toast.LENGTH_SHORT).show()
                isTimerRunning = false

            }
        }.start()
        isTimerRunning = true
    }

    private fun resetTimer() {
        timer?.cancel()
        remainingTime = START_TIME_IN_MILLIS
        updateTimerText()
        binding.tvTakePomodoro.text = resources.getText(R.string.take_pomodoro)
        isTimerRunning = false
        binding.progressBar.progress = 100
    }

    private fun updateTimerText() {
        val minute = remainingTime.div(1000).div(60)
        val second = remainingTime.div(1000) % 60
        val formattedTime = String.format("%02d:%02d", minute, second)
        binding.tvTimer.text = formattedTime
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(REMAINING_TIME, remainingTime)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedTime = savedInstanceState.getLong(REMAINING_TIME)

        if (savedTime != START_TIME_IN_MILLIS) {
            startTimer(savedTime)
        }

    }
}