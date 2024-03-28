package com.zivkesten.cleanwidget

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val STREAK_COUNT = "streak_count"
private const val PREFERENCE_NAME = "widget_preferences"

class StreakWidgetGlance : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, glanceId: GlanceId) {
        provideContent {
            StreakWidgetContent(context)
        }
    }

    @Composable
    fun StreakWidgetContent(context: Context) {
        Column(
            modifier = GlanceModifier.fillMaxSize().background(Color.Red),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val streakCount = getStreakCount(context) // This should be dynamically retrieved
            Log.d("Zivi", "count $streakCount")
            Text(text = "Streak: $streakCount", modifier = GlanceModifier.padding(16.dp))
        }
    }

    companion object {
        const val PREFERENCES_NAME = PREFERENCE_NAME
        val STREAK_COUNT_KEY = intPreferencesKey(STREAK_COUNT)

        suspend fun updateWidget(context: Context, selectedDate: LocalDate) {
            // Simulate fetching the streak count for the selected date
            val streakCount = getStreakCountForDate(selectedDate)

            // Persist the streak count
            saveStreakCount(context, streakCount)

            // Trigger widget update
            CoroutineScope(Dispatchers.IO).launch {
                StreakWidgetGlance().updateAll(context)
            }
        }

        private fun getStreakCountForDate(date: LocalDate): Int {
            // Current date
            val currentDate = LocalDate.now()

            // Calculate the difference in days
            val daysBetween = ChronoUnit.DAYS.between(date, currentDate)

            // Return the number of days as an Int
            return daysBetween.toInt()
        }

        private fun getStreakCount(context: Context): Int {
            val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            return preferences.getInt(STREAK_COUNT_KEY.name, 0) // Default to 0 if not found
        }

        private fun saveStreakCount(context: Context, count: Int) {
            val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            preferences.edit().putInt(STREAK_COUNT_KEY.name, count).apply()
        }
    }
}
