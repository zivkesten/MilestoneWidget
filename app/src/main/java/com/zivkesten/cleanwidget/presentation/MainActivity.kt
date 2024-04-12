package com.zivkesten.cleanwidget.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.zivkesten.cleanwidget.R
import com.zivkesten.cleanwidget.presentation.screens.MainScreen
import com.zivkesten.cleanwidget.services.MediaService
import com.zivkesten.cleanwidget.services.UpdateWidgetWorker
import com.zivkesten.cleanwidget.ui.theme.CleanWidgetTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        val onUpdate = {
            finishAffinity()
        }
        setContent {
            val viewModel: StreakViewModel = viewModel()
            CleanWidgetTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(context, viewModel.state,
                    onPicked =  {
                        viewModel.datePicked(context, it)
                        scheduleWidgetUpdate(context)
                    },
                    onPlay = {
                        MediaService.playOrStopSound(context, "morning.mp3")
                    },
                    onClose = onUpdate
                )
            }
        }
    }

    private fun scheduleWidgetUpdate(appContext: Context) {
        val updateRequest = PeriodicWorkRequestBuilder<UpdateWidgetWorker>(24, TimeUnit.HOURS, 15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork("widgetUpdate", ExistingPeriodicWorkPolicy.UPDATE, updateRequest)
    }

    override fun onDestroy() {
        MediaService.mediaPlayer?.release()
        MediaService.mediaPlayer = null
        super.onDestroy()
    }
}

@Composable
fun PlaySoundButton(modifier: Modifier, onPlay: () -> Unit) {
    Button(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)),
        onClick = onPlay ) {
        Text(text = stringResource(R.string.play_medidation_text), fontSize = 20.sp)
    }
}



