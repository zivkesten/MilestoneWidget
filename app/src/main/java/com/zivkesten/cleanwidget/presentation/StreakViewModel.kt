package com.zivkesten.cleanwidget.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zivkesten.cleanwidget.R



class StreakViewModel: ViewModel() {
    var state by mutableStateOf<UiState>(UiState.initial())

    fun datePicked() {
        state = UiState.AnswerState(R.drawable.yedid_ans, "יופי סגור את האפליקציה והבט בווידג׳ט")
    }
}