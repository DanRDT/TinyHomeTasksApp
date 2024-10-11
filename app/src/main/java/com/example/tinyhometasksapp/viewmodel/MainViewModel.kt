package com.example.tinyhometasksapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tinyhometasksapp.model.Task
import com.example.tinyhometasksapp.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val myResponseTask: MutableLiveData<Response<Task>> = MutableLiveData()
    val myResponseTasks: MutableLiveData<Response<List<Task>>> = MutableLiveData()

    fun getTask(id: String) {
        viewModelScope.launch {
            val response = repository.getTask(id)
            myResponseTask.value = response
        }
    }

    fun getTasks(completed: Boolean) {
        viewModelScope.launch {
            val response = repository.getTasks(completed)
            myResponseTasks.value = response
        }
    }
}