package com.gb.stopwatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gb.stopwatch.databinding.ActivityMainBinding
import com.gb.stopwatch.model.ElapsedTimeCalculator
import com.gb.stopwatch.model.TimestampMillisecondsFormatter
import com.gb.stopwatch.model.TimestampProviderImpl
import com.gb.stopwatch.view_model.MainViewModel
import com.gb.stopwatch.view_model.StopwatchStateCalculator
import com.gb.stopwatch.view_model.StopwatchStateHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val vb: ActivityMainBinding by viewBinding(CreateMethod.INFLATE)
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val viewModel by lazy {
        MainViewModel(
            StopwatchStateHolder(
                StopwatchStateCalculator(
                    TimestampProviderImpl(), ElapsedTimeCalculator
                ),
                ElapsedTimeCalculator,
                TimestampMillisecondsFormatter
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)

        scope.launch {
            viewModel.ticker.collect {
                vb.textTime.text = it
            }
        }

        vb.buttonStart.setOnClickListener {
            viewModel.start()
        }

        vb.buttonPause.setOnClickListener {
            viewModel.pause()
        }

        vb.buttonStop.setOnClickListener {
            viewModel.stop()
        }
    }
}








