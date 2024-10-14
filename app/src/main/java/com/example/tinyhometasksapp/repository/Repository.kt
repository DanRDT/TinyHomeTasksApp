package com.example.tinyhometasksapp.repository

import com.example.tinyhometasksapp.api.RetrofitInstance
import com.example.tinyhometasksapp.model.NewTask
import com.example.tinyhometasksapp.model.Task
import retrofit2.Response
import retrofit2.http.Body

class Repository {

    suspend fun getTask(id: String): Response<Task> {
        return RetrofitInstance.api.getTask(id)
    }

    suspend fun getTasks(completed: String, sortBy: String): Response<List<Task>> {
        return RetrofitInstance.api.getTasks(completed, sortBy)
    }

    suspend fun createTask(task: NewTask): Response<Task> {
        return RetrofitInstance.api.createTask(task)
    }

    suspend fun updateTask(id: String, task: Task): Response<Task> {
        return RetrofitInstance.api.updateTask(id, task)
    }

    suspend fun deleteTask(id: String): Response<Unit> {
        return RetrofitInstance.api.deleteTask(id)
    }
}