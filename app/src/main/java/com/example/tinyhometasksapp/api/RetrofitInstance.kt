package com.example.tinyhometasksapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://2735-2600-387-15-1611-00-b.ngrok-free.app "

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TasksAPI by lazy {
        retrofit.create(TasksAPI::class.java)
    }



}
