package com.example.inspectorapp

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

internal class AppWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        info("doWork")
        return Result.success()
    }
}