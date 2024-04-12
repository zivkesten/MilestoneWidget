package com.zivkesten.cleanwidget.services

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.zivkesten.cleanwidget.presentation.StreakWidgetGlance

class StreakWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StreakWidgetGlance()
}


