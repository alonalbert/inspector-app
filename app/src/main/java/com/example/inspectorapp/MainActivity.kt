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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.lifecycle.coroutineScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
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
        val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth(),
            ) {
                TextButton("Start Job") {
                    startJob()
                }
                TextButton("Start Work") {
                    startWork()
                }

                TextButton("Network") {
                    lifecycle.coroutineScope.callUrl(snackbarHostState, "https://reqres.in/api/users")
                }
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
        val request = OneTimeWorkRequest.Builder(AppWorker::class.java).build()
        val workManager = WorkManager.getInstance(this)
        workManager.getWorkInfoByIdLiveData(request.id).observe(this) {
            info("State of ${request.id}: ${it.state}")
        }
        workManager.enqueue(request)
    }
}

@Composable
fun TextButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text)
    }
}
