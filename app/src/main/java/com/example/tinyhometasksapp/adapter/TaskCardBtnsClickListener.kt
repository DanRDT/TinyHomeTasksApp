package com.example.tinyhometasksapp.adapter

import com.example.tinyhometasksapp.model.Task

interface TaskCardBtnsClickListener {
    fun onEditClick(task: Task)
    fun onCompletedStatusClick(task: Task)
    fun onDeleteClick(task: Task)
}