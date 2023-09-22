package com.example.inspectorapp

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import com.example.inspectorapp.ui.theme.InspectorAppTheme
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : ComponentActivity() {
    private val jobId = AtomicInteger(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InspectorAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }

    @Composable
    fun App() {
        Column(horizontalAlignment = CenterHorizontally) {
            TextButton(text = "Start Job") {
                startJob()
            }
            TextButton(text = "Start Work") {
                startWork()
            }
        }
    }

    private fun startJob() {
        val job = JobInfo.Builder(jobId.getAndIncrement(), ComponentName(this, AppJobService::class.java))
            .build()
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(job)

    }

    private fun startWork() {

    }
}

@Composable
fun TextButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text)
    }
}
