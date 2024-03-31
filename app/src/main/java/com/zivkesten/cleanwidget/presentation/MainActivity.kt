package com.zivkesten.cleanwidget.presentation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.zivkesten.cleanwidget.MediaManager
import com.zivkesten.cleanwidget.UpdateWidgetWorker
import com.zivkesten.cleanwidget.ui.theme.CleanWidgetTheme
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        val onUpdate = {
            //finishAffinity()
            exitProcess(0)
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
                        MediaManager.playOrStopSound(context, "morning.mp3")
                    },
                    onClose = onUpdate
                )
            }
        }
    }

    private fun scheduleWidgetUpdate(appContext: Context) {
        val now = LocalDateTime.now()
        val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
        val initialDelay = Duration.between(now, nextMidnight).toMinutes()

        val updateRequest = PeriodicWorkRequestBuilder<UpdateWidgetWorker>(24, TimeUnit.HOURS, 15, TimeUnit.MINUTES)
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork("widgetUpdate", ExistingPeriodicWorkPolicy.UPDATE, updateRequest)
    }

    override fun onDestroy() {
        MediaManager.mediaPlayer?.release()
        MediaManager.mediaPlayer = null
        super.onDestroy()
    }
}



@Composable
fun MainScreen(
    context: Context, state: UiState,
    onPicked: (LocalDate) -> Unit,
    onClose: () -> Unit,
    onPlay: () -> Unit
) {
    Column {
        Image(
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = state.resId),
            contentDescription = ""
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = state.text,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(30.dp),
                style = TextStyle(
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            when (state) {
                is UiState.QuestionState -> {
                    Button(
                        onClick = { showDatePicker(context, onPicked) },
                        modifier = Modifier.align(CenterHorizontally).fillMaxWidth().padding(8.dp)
                    ) {
                        Text(text = "בחר תאריך", fontSize = 20.sp)
                    }
                    PlaySoundButton(Modifier.align(CenterHorizontally)) { onPlay() }
                }

                is UiState.AnswerState -> {
                    Button(
                        onClick = {
                            // exitProcess(0)
                            onClose()
                        },
                        modifier = Modifier.align(CenterHorizontally)
                    ) {
                        Text("סגור את המסך")
                    }

                    PlaySoundButton(Modifier.align(CenterHorizontally)) { onPlay() }

                }
            }
        }
    }
}

private fun showDatePicker(context: Context, onPicked: (LocalDate) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(context, { _, year, month, dayOfMonth ->
        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        onPicked(selectedDate)

    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
}

@Composable
fun PlaySoundButton(modifier: Modifier, onPlay: () -> Unit) {
    Button(
        modifier = modifier.then(Modifier.fillMaxWidth().padding(8.dp)),
        onClick = onPlay ) {
        Text(text = "נגן תירגול בוקר", fontSize = 20.sp)
    }
}



