package com.example.tinyhometasksapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id : String,
    var taskDescription : String,
    val createdDate : String,
    var dueDate : String,
    var completed : Boolean
): Parcelable
