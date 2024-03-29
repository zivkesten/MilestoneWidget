package com.zivkesten.cleanwidget.presentation

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.zivkesten.cleanwidget.R
import com.zivkesten.cleanwidget.domain.getStreakCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class StreakWidgetGlance : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            StreakWidgetContent(context)
        }
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            SMALL_SQUARE,
            HORIZONTAL_RECTANGLE,
            BIG_SQUARE
        )
    )

    @Composable
    fun StreakWidgetContent(context: Context) {
        val streakCount = PreferenceManager.getStreakCount(context)
        val size = LocalSize.current
        val big = size.height >= HORIZONTAL_RECTANGLE.height
        val mod = if (big) {
            GlanceModifier.background(ColorProvider(Color.Transparent))
        } else {
            GlanceModifier.background(ColorProvider(Color.Black))
        }
        Box {
            if (big) {
                Image(
                    provider = ImageProvider(R.drawable.yedid_ans),
                    contentDescription = ""
                )
            }

            Column(
                modifier = GlanceModifier.fillMaxSize().then(mod),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally) {
                Box(modifier = GlanceModifier.defaultWeight()) {
                    // This empty container acts as a flexible spacer
                }
                Log.d("Zivi", "size ${size.width}")
                val text = when {
                    size.width < 200.dp -> streakCount.toString()
                  //  (size.width > 100.dp && size.width < 200.dp) -> "ימים $streakCount"
                    else -> "אתה נקי כבר $streakCount ימים"
                }
                Log.d("Zivi", "text $text")

                Text(
                    text = text,
                    modifier = GlanceModifier.padding(16.dp),
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        textAlign = TextAlign.Center,
                        fontSize = if (size.width < 250.dp) 20.sp else 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }

    companion object {
        const val PREFERENCES_NAME = "streak_count"
        val STREAK_COUNT_KEY = intPreferencesKey("widget_preferences")
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)

        suspend fun updateWidget(context: Context, selectedDate: LocalDate) {
            // Persist the streak count
            PreferenceManager.saveStreakCount(context, selectedDate.getStreakCount())

            // Trigger widget update
            CoroutineScope(Dispatchers.IO).launch {
                StreakWidgetGlance().updateAll(context)
            }
        }
    }
}