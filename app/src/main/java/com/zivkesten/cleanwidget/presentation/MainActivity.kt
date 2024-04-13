package com.zivkesten.cleanwidget.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.zivkesten.cleanwidget.presentation.screens.MainScreen
import com.zivkesten.cleanwidget.services.UpdateWidgetWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        setContent {
            val viewModel: StreakViewModel = viewModel()
            MainScreen(context, viewModel.state,
                onPicked =  {
                    viewModel.datePicked(context, it)
                    scheduleWidgetUpdate(context)
                },
            )
        }
    }

    private fun scheduleWidgetUpdate(appContext: Context) {
        val updateRequest = PeriodicWorkRequestBuilder<UpdateWidgetWorker>(
            24,
            TimeUnit.HOURS,
            15,
            TimeUnit.MINUTES
        ).build()
        WorkManager
            .getInstance(appContext)
            .enqueueUniquePeriodicWork(
                "widgetUpdate",
                ExistingPeriodicWorkPolicy.UPDATE,
                updateRequest
            )
    }
}



