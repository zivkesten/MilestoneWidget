package com.zivkesten.cleanwidget.presentation.screens

import android.app.DatePickerDialog
import android.content.Context
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivkesten.cleanwidget.R
import com.zivkesten.cleanwidget.presentation.PlaySoundButton
import com.zivkesten.cleanwidget.presentation.UiState
import java.time.LocalDate
import java.util.Calendar

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
            contentDescription = "image"
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = state.text,
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
                    PlaySoundButton(Modifier.align(Alignment.CenterHorizontally)) { onPlay() }
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

                    PlaySoundButton(Modifier.align(Alignment.CenterHorizontally)) { onPlay() }

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
