package com.example.inspectorapp

import android.app.job.JobParameters
import android.app.job.JobService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class AppJobService : JobService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)


    override fun onStartJob(parameters: JobParameters): Boolean {
        info("onStartJob")

        scope.launch {
            info("Background work")
            jobFinished(parameters, false)
        }

        return true     // Our task will run in background, we will take care of notifying the finish.
    }

    override fun onStopJob(parameters: JobParameters): Boolean {
        info("onStartJob")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}