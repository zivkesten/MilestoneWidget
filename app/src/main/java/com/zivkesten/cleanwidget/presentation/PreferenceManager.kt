package com.zivkesten.cleanwidget.presentation

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    fun getStreakCount(context: Context): Int {
        val preferences: SharedPreferences = context.getSharedPreferences(
            StreakWidgetGlance.PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
        return preferences.getInt(
            StreakWidgetGlance.STREAK_COUNT_KEY.name,
            0
        ) // Default to 0 if not found
    }

    fun saveStreakCount(context: Context, count: Int) {
        val preferences: SharedPreferences = context.getSharedPreferences(
            StreakWidgetGlance.PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
        preferences.edit().putInt(StreakWidgetGlance.STREAK_COUNT_KEY.name, count).apply()
    }
}