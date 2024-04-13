package com.zivkesten.cleanwidget.presentation

import com.zivkesten.cleanwidget.R

sealed class UiState(open val resId: Int, open val text: String) {
    data class QuestionState(override val resId: Int, override val text: String): UiState(resId, text)
    data class AnswerState(override val resId: Int, override val text: String): UiState(resId, text)

    companion object {
        fun initial() = QuestionState(R.mipmap.ic_launcher, "Select the day you plan to start")
    }
}