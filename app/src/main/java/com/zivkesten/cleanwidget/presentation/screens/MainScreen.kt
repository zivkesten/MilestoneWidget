package com.zivkesten.cleanwidget.presentation.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivkesten.cleanwidget.R
import com.zivkesten.cleanwidget.presentation.PlaySoundButton
import com.zivkesten.cleanwidget.presentation.StreakWidgetGlance
import com.zivkesten.cleanwidget.presentation.UiState
import com.zivkesten.cleanwidget.services.MediaService
import com.zivkesten.cleanwidget.services.PreferenceService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

@Composable
fun MainScreen(
    context: Context, state: UiState,
    streak: String,
    onPicked: (LocalDate) -> Unit,
    onClose: () -> Unit,
) {
    var timerDuration by remember { mutableStateOf(0L) }
    var timeLeft by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()


    Column(
        Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = streak,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
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
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = stringResource(R.string.selecte_date), fontSize = 20.sp)
                }
                PlaySoundButton(Modifier.align(Alignment.CenterHorizontally)) {
                    timerDuration =  MediaService.playOrStopSound(context, "morning.mp3")
                    timeLeft = timerDuration
                    coroutineScope.launch {
                        while (timeLeft > 0) {
                            delay(1000)
                            timeLeft -= 1000
                        }
                    }
                }
                AnimatedVisibility(
                    visible = timeLeft > 0,
                    enter = scaleIn(),
                ) {
                    TimerDisplay(timeLeft)
                }

            }

            is UiState.AnswerState -> {
                Button(
                    onClick = {
                        onClose()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(stringResource(R.string.close_screen_tex))
                }

                PlaySoundButton(Modifier.align(Alignment.CenterHorizontally)) {
                    timerDuration =  MediaService.playOrStopSound(context, "morning.mp3")
                    timeLeft = timerDuration
                    coroutineScope.launch {
                        while (timeLeft > 0) {
                            delay(1000)
                            timeLeft -= 1000
                        }
                    }
                }
                AnimatedVisibility(
                    visible = timeLeft > 0,
                    enter = scaleIn(),
                ) {
                    TimerDisplay(timeLeft)
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimerDisplay(timeMillis: Long) {
    val totalSeconds = timeMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val text = String.format("%02d:%02d", minutes, seconds)
    Box(Modifier.fillMaxWidth()) {
        AnimatedContent(
            modifier = Modifier.align(Alignment.Center),
            targetState = text,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }, label = ""
        ) { newText ->
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = newText,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
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
