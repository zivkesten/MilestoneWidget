package com.zivkesten.cleanwidget.presentation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import com.zivkesten.cleanwidget.ui.theme.CleanWidgetTheme
import java.time.LocalDate
import java.util.Calendar
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        setContent {
            val viewModel: StreakViewModel = viewModel()
            CleanWidgetTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(context, viewModel.state) {
                    viewModel.datePicked(context, it)
                }
            }
        }
    }
}

@Composable
fun MainScreen(context: Context, state: UiState, onPicked: (LocalDate) -> Unit) {
    Column {
        Image(
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Inside,
            painter = painterResource(id = state.resId),
            contentDescription = ""
        )

        Column {
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
                is UiState.QuestionState ->
                    Button(
                        onClick = { showDatePicker(context, onPicked) },
                        modifier = Modifier.align(CenterHorizontally)
                    ) {
                        Text(text = "בחר תאריך")
                    }

                is UiState.AnswerState -> {
                    Button(
                        onClick = { exitProcess(0) },
                        modifier = Modifier.align(CenterHorizontally)
                    ) {
                        Text("סגור את המסך")
                    }
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
