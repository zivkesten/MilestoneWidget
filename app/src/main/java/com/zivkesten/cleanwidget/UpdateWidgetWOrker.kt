package com.zivkesten.cleanwidget

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zivkesten.cleanwidget.presentation.StreakWidgetGlance

class UpdateWidgetWorker(
    val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        // Update widget here
        StreakWidgetGlance.updateWidget(context = appContext)
        return Result.success()
    }
}
