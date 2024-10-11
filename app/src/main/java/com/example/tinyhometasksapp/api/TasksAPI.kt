package com.example.tinyhometasksapp.api

import com.example.tinyhometasksapp.model.Task
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TasksAPI {

    @GET("tasks/{id}")
    suspend fun getTask(
        @Path("id") id: String
    ): Response<Task>

    @GET("tasks")
    suspend fun getTasks(
        @Query("completed") completed: Boolean
    ): Response<List<Task>>

}