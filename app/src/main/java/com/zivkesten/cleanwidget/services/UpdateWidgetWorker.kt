package com.zivkesten.cleanwidget.services

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zivkesten.cleanwidget.presentation.StreakWidgetGlance

class UpdateWidgetWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        // Update widget here
        StreakWidgetGlance().updateAll(context = appContext)
        return Result.success()
    }
}
