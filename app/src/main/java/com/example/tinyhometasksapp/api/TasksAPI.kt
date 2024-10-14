package com.example.tinyhometasksapp.api

import com.example.tinyhometasksapp.model.NewTask
import com.example.tinyhometasksapp.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface TasksAPI {

    @GET("tasks/{id}")
    suspend fun getTask(
        @Path("id") id: String
    ): Response<Task>

    @GET("tasks")
    suspend fun getTasks(
        @Query("completed") completed: String,
        @Query("sort_by") sortBy: String
    ): Response<List<Task>>

    @POST("tasks")
    suspend fun createTask(
        @Body() task: NewTask,
    ): Response<Task>

}