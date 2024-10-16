package com.example.tinyhometasksapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinyhometasksapp.ViewModel.MainViewModel
import com.example.tinyhometasksapp.ViewModel.MainViewModelFactory
import com.example.tinyhometasksapp.adapter.TaskCardBtnsClickListener
import com.example.tinyhometasksapp.adapter.TasksAdapter
import com.example.tinyhometasksapp.model.Task
import retrofit2.Response
import com.example.tinyhometasksapp.repository.Repository

class MainActivity : AppCompatActivity(), TaskCardBtnsClickListener {

    private lateinit var tasksRecycleView: RecyclerView
    private lateinit var viewModel: MainViewModel
    private lateinit var tasksObserver: Observer<Response<List<Task>>>
    private lateinit var deleteTaskObserver: Observer<Response<Unit>>
    private lateinit var updateTaskObserver: Observer<Response<Task>>

    private lateinit var taskPageLauncher: ActivityResultLauncher<Intent>

    private val tasksAdapter by lazy { TasksAdapter(this) }

    private var completed = "All"
    private var sortBy = "Due"
    private var sortDirection = "Ascending"

    private var gettingAllQueryActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        var queryCount = 0
        var prevResponseList: List<Task> = emptyList()

        tasksObserver = Observer { response ->
            if (response.isSuccessful) response.body()?.let {
                if (gettingAllQueryActive) {
                    queryCount++
                    if (queryCount < 2) {
                        prevResponseList = it
                    } else {
                        if (prevResponseList.isEmpty()) tasksAdapter.setData(it)
                        else if (prevResponseList[0].completed) tasksAdapter.setData(it + prevResponseList)
                        else tasksAdapter.setData(prevResponseList + it)

                        queryCount = 0
                        prevResponseList = emptyList()
                    }
                } else {
                    tasksAdapter.setData(it)

                    queryCount = 0
                    prevResponseList = emptyList()
                }
            } else Toast.makeText(this, response.code().toString(), Toast.LENGTH_LONG).show()
        }
        viewModel.responseTasks.observe(this, tasksObserver)

        updateTasks()

        deleteTaskObserver = Observer { response ->
            if (!response.isSuccessful) Toast.makeText(this, response.code().toString(), Toast.LENGTH_LONG).show()
            updateTasks()
        }
        viewModel.responseDeleteTask.observe(this, deleteTaskObserver)

        updateTaskObserver = Observer { response ->
            if (!response.isSuccessful) Toast.makeText(this, response.code().toString(), Toast.LENGTH_LONG).show()
            updateTasks()
        }
        viewModel.responseUpdateTask.observe(this, updateTaskObserver)


        val settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val completedData = data.getStringExtra("completed")
                    val sortByData = data.getStringExtra("sort_by")
                    val sortDirectionData = data.getStringExtra("sort_direction")
                    if (completedData != null) completed = completedData
                    if (sortByData != null) sortBy = sortByData
                    if (sortDirectionData != null) sortDirection = sortDirectionData

//                    Toast.makeText(this, "Showing: $completed\nSorted by $sortBy in $sortDirection order", Toast.LENGTH_SHORT).show()
                    updateTasks()

                } else Toast.makeText(this, "Error: Couldn't Update Filters", Toast.LENGTH_SHORT).show()
            }
        }

        val settingsButton = findViewById<ImageButton>(R.id.settingsBtn)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)

            intent.putExtra("completed", completed)
            intent.putExtra("sort_by", sortBy)
            intent.putExtra("sort_direction", sortDirection)

            settingsLauncher.launch(intent)
        }


        taskPageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val returnAction = data.getStringExtra("return_action")
                    if (returnAction != null) {
                        if (returnAction == "save") updateTasks()
                    } else Toast.makeText(this, "Error: No Return Value", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(this, "Error: Couldn't Update Tasks", Toast.LENGTH_SHORT).show()
            }
        }

        val addButton = findViewById<ImageButton>(R.id.addTaskBtn)
        addButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("requested_action", "create")
            taskPageLauncher.launch(intent)
        }


    }

    private fun setupRecyclerView() {
        tasksRecycleView = findViewById(R.id.TasksRecyclerView)
        tasksRecycleView.adapter = tasksAdapter
        tasksRecycleView.layoutManager = LinearLayoutManager(this)
    }

    private fun updateTasks() {
        val sortByValue = if (sortBy == getString(R.string.due)) "dueDate" else "createdDate"
        val sortDirectionValue = if (sortDirection == getString(R.string.ascending)) "+" else "-"
        val sortByQuery = sortDirectionValue + sortByValue

        if (completed == getString(R.string.all)) {
            gettingAllQueryActive = true
            viewModel.getTasks("false", sortByQuery)
            viewModel.getTasks("true", sortByQuery)

        } else {
            gettingAllQueryActive = false
            val completedValue = if (completed == getString(R.string.complete)) "true" else "false"
            viewModel.getTasks(completedValue, sortByQuery)
        }

    }

    override fun onEditClick(task: Task) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra("requested_action", "edit")
        intent.putExtra("task", task)
        taskPageLauncher.launch(intent)
    }

    override fun onDeleteClick(task: Task) {
        viewModel.deleteTask(task.id)
        Toast.makeText(this, "Deleting...", Toast.LENGTH_SHORT).show()
    }

    override fun onCompletedStatusClick(task: Task) {
        viewModel.updateTask(task.id, Task(task.id, task.taskDescription, "${task.createdDate}Z", "${task.dueDate}Z", task.completed))
        Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show()
    }
}