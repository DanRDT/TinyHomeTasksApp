package com.example.tinyhometasksapp.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



fun stringDateTimeToReadable(dateTime: String): String {
    val date = LocalDateTime.parse(dateTime)
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
    return date.format(formatter)
}

fun dateTimeObjectToISO8601(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return dateTime.format(formatter)
}