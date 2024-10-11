package com.example.tinyhometasksapp.repository

import com.example.tinyhometasksapp.api.RetrofitInstance
import com.example.tinyhometasksapp.model.Task
import retrofit2.Response

class Repository {

    suspend fun getTask(id: String): Response<Task> {
        return RetrofitInstance.api.getTask(id)
    }

    suspend fun getTasks(completed: String, sortBy: String): Response<List<Task>> {
        return RetrofitInstance.api.getTasks(completed, sortBy)
    }
}