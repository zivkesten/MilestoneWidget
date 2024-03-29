package com.zivkesten.cleanwidget.domain

import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun LocalDate.getStreakCount(): Int {
    // Current date
    val currentDate = LocalDate.now()

    // Calculate the difference in days
    val daysBetween = ChronoUnit.DAYS.between(this, currentDate)

    // Check if the date is in the future
    if (daysBetween < 0) {
        return 0
    }

    // Return the number of days as an Int
    return daysBetween.toInt()
}
