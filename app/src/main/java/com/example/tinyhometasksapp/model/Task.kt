package com.example.tinyhometasksapp.model


data class Task(
    val id : String,
    var taskDescription : String,
    val createdDate : String,
    var dueDate : String,
    var completed : Boolean
)
