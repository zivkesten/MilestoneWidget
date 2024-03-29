package com.zivkesten.cleanwidget.presentation

import android.content.Context
import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object PreferenceManager {
    fun saveStartDate(context: Context, key: String, date: LocalDate) {

        val sharedPreferences = context.getSharedPreferences(
            StreakWidgetGlance.START_DATE,
            Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            // Convert LocalDate to String
            val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            Log.w("Zivi", "save dateString $dateString")
            putString(key, dateString)
            apply()
        }
        val l = getLocalDate(context, key)
        Log.w("Zivi", "get dateString $l")
    }

    fun getLocalDate(context: Context, key: String): LocalDate? {
        val sharedPreferences = context.getSharedPreferences(
            StreakWidgetGlance.START_DATE,
            Context.MODE_PRIVATE)

        val dateString = sharedPreferences.getString(key, null) ?: return null
        Log.d("Zivi", "dateString $dateString")
        // Convert String to LocalDate
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}