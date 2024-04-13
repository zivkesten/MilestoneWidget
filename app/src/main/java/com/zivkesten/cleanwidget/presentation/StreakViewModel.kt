package com.zivkesten.cleanwidget.presentation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivkesten.cleanwidget.presentation.StreakWidgetGlance.Companion.START_DATE_KEY
import com.zivkesten.cleanwidget.services.PreferenceService
import kotlinx.coroutines.launch
import java.time.LocalDate

class StreakViewModel: ViewModel() {
    var state by mutableStateOf<UiState>(UiState.initial())

    fun datePicked(context: Context, selectedDate: LocalDate) {
        viewModelScope.launch {
            PreferenceService.saveStartDate(context, START_DATE_KEY.name, selectedDate)
            StreakWidgetGlance().updateAll(context)
        }
    }
}