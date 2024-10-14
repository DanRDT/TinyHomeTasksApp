package com.example.tinyhometasksapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tinyhometasksapp.model.NewTask
import com.example.tinyhometasksapp.model.Task
import com.example.tinyhometasksapp.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val responseTask: MutableLiveData<Response<Task>> = MutableLiveData()
    val responseTasks: MutableLiveData<Response<List<Task>>> = MutableLiveData()
    val responseCreateTask: MutableLiveData<Response<Task>> = MutableLiveData()
    val responseUpdateTask: MutableLiveData<Response<Task>> = MutableLiveData()
    val responseDeleteTask: MutableLiveData<Response<Unit>> = MutableLiveData()

    fun getTask(id: String) {
        viewModelScope.launch {
            val response = repository.getTask(id)
            responseTask.value = response
        }
    }

    fun getTasks(completed: String, sortBy: String) {
        viewModelScope.launch {
            val response = repository.getTasks(completed, sortBy)
            responseTasks.value = response
        }
    }

    fun createTask(task: NewTask) {
        viewModelScope.launch {
            val response = repository.createTask(task)
            responseCreateTask.value = response
        }
    }

    fun updateTask(id: String, task: Task) {
        viewModelScope.launch {
            val response = repository.updateTask(id, task)
            responseUpdateTask.value = response
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            val response = repository.deleteTask(id)
            responseDeleteTask.value = response
        }
    }
}