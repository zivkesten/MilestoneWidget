package com.zivkesten.cleanwidget

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Image
import androidx.glance.LocalContext
import androidx.lifecycle.lifecycleScope
import com.zivkesten.cleanwidget.ui.theme.CleanWidgetTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        val lifecycleCorutine = lifecycleScope
        setContent {
            CleanWidgetTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(context)
            }
        }
    }
}

@Composable
fun MainScreen(context: Context) {
    val coroutineScope = rememberCoroutineScope()
    Column {
        Text(
            text = "באיזה יום התחלת את ההפסקה, או באיזה יום אתה מתכנן להפסיק?",
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(30.dp),
            style = TextStyle(
                fontSize = 30.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { showDatePicker(context, coroutineScope) },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "בחר תאריך")
        }
    }
}

private fun showDatePicker(context: Context, scope: CoroutineScope) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(context, { _, year, month, dayOfMonth ->
        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        val selectedDateString = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        //SharedPreferencesUtil.saveStartDate(context, selectedDateString)
        Log.d("Zivi", "sleecteDAte: $selectedDate")
        scope.launch {
            StreakWidgetGlance.updateWidget(context, selectedDate)
        }
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
}
