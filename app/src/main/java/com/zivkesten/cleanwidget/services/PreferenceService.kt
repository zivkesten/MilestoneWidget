package com.zivkesten.cleanwidget.services

import android.content.Context
import com.zivkesten.cleanwidget.presentation.StreakWidgetGlance
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object PreferenceService {
    fun saveStartDate(context: Context, key: String, date: LocalDate) {

        val sharedPreferences = context.getSharedPreferences(
            StreakWidgetGlance.START_DATE,
            Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            // Convert LocalDate to String
            val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            putString(key, dateString)
            apply()
        }
    }

    fun getLocalDate(context: Context, key: String): LocalDate? {
        val sharedPreferences = context.getSharedPreferences(
            StreakWidgetGlance.START_DATE,
            Context.MODE_PRIVATE)

        val dateString = sharedPreferences.getString(key, null) ?: return null
        // Convert String to LocalDate
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}