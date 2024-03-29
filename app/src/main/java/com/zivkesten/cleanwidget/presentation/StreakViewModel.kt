package com.zivkesten.cleanwidget.presentation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivkesten.cleanwidget.R
import com.zivkesten.cleanwidget.presentation.StreakWidgetGlance.Companion.START_DATE_KEY
import kotlinx.coroutines.launch
import java.time.LocalDate

class StreakViewModel: ViewModel() {
    var state by mutableStateOf<UiState>(UiState.initial())

    fun datePicked(context: Context, selectedDate: LocalDate) {
        viewModelScope.launch {
            PreferenceManager.saveStartDate(context, START_DATE_KEY.name, selectedDate)
            StreakWidgetGlance.updateWidget(context)
            state = UiState.AnswerState(R.drawable.yedid_ans, "יופי סגור את האפליקציה והבט בווידג׳ט")
        }
    }
}