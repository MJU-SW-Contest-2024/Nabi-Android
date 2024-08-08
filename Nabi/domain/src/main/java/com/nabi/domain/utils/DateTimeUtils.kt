package com.nabi.domain.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    fun convertDateTime(input: String): Pair<String, String> {
        val dateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val dayOfWeekKorean = arrayOf("일", "월", "화", "수", "목", "금", "토")
        val firstPart = "${dateTime.year}년 ${dateTime.monthValue}월 ${dateTime.dayOfMonth}일(${dayOfWeekKorean[dateTime.dayOfWeek.ordinal]})"

        val hour = dateTime.hour
        val minute = dateTime.minute
        val period = if (hour < 12) "오전" else "오후"
        val adjustedHour = if (hour % 12 == 0) 12 else hour % 12
        val secondPart = "$period ${"%02d".format(adjustedHour)}:${"%02d".format(minute)}"

        return Pair(firstPart, secondPart)
    }
}
