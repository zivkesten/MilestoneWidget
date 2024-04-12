package com.zivkesten.cleanwidget.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
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
import com.zivkesten.cleanwidget.services.PreferenceService
import java.time.LocalDate

class StreakWidgetGlance : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val localDate = PreferenceService.getLocalDate(context, START_DATE_KEY.name)
        provideContent {
            StreakWidgetContent(localDate)
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
    fun StreakWidgetContent(localDate: LocalDate?) {
        val streakCount = localDate?.getStreakCount() ?: 0
        val size = LocalSize.current
        val mod = if (size.height >= HORIZONTAL_RECTANGLE.height) {
            GlanceModifier.background(ColorProvider(Color.Transparent))
        } else {
            GlanceModifier.background(ColorProvider(Color.Black))
        }
        Box {
            if (size.height >= HORIZONTAL_RECTANGLE.height) {
                Image(
                    provider = ImageProvider(R.drawable.yedid_ans),
                    contentDescription = ""
                )
            }
            Column(
                modifier = GlanceModifier.fillMaxSize().then(mod).clickable(onClick =  actionStartActivity(MainActivity::class.java)),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally) {
                Box(modifier = GlanceModifier.defaultWeight()) {
                    // This empty container acts as a flexible spacer
                }
                val text = when {
                    size.width < 200.dp -> streakCount.toString()
                    else -> stringResource(R.string.clean_time_text, streakCount)
                }

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
        const val START_DATE = "start_date"
        val START_DATE_KEY = longPreferencesKey("start_date_key")
        private val SMALL_SQUARE = DpSize(100.dp, 100.dp)
        private val HORIZONTAL_RECTANGLE = DpSize(250.dp, 100.dp)
        private val BIG_SQUARE = DpSize(250.dp, 250.dp)
    }
}