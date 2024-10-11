package com.example.tinyhometasksapp.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



fun stringDateTimeToReadable(datetime: String): String {
    val date = LocalDateTime.parse(datetime)
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
    return date.format(formatter)
}