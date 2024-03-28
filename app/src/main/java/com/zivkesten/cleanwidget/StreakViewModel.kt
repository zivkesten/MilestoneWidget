package com.zivkesten.cleanwidget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel

sealed class UiState(open val resId: Int, open val text: String) {
    data class QuestionState(override val resId: Int, override val text: String): UiState(resId, text)
    data class AnswerState(override val resId: Int, override val text: String): UiState(resId, text)

    companion object {
        fun initial() = QuestionState(R.drawable.yedid_ask, "באיזה יום התחלת את ההפסקה, או באיזה יום אתה מתכנן להפסיק?")

    }
}

class StreakViewModel: ViewModel() {
    var state by mutableStateOf<UiState>(UiState.initial())

    fun datePicked() {
        state = UiState.AnswerState(R.drawable.yedid_ans, "יופי סגור את האפליקציה והבט בווידג׳ט")
    }
}