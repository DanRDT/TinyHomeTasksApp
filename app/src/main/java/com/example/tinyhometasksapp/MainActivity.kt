package com.example.tinyhometasksapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.example.tinyhometasksapp.adapter.TasksAdapter
import com.example.tinyhometasksapp.repository.Repository

class MainActivity : AppCompatActivity() {

    private lateinit var tasksRecycleView: RecyclerView
    private lateinit var viewModel: MainViewModel
    private val tasksAdapter by lazy { TasksAdapter() }

    private var completed = "All"
    private var sortBy = "Due"
    private var sortDirection = "Ascending"

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
        updateTasks()

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val completedData = data.getStringExtra("completed")
                    val sortByData = data.getStringExtra("sort_by")
                    val sortDirectionData = data.getStringExtra("sort_direction")
                    if (completedData != null) completed = completedData
                    if (sortByData != null) sortBy = sortByData
                    if (sortDirectionData != null) sortDirection = sortDirectionData

                    Toast.makeText(this, "Showing: $completed\nSorted by $sortBy in $sortDirection order", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: Couldn't Update Filters", Toast.LENGTH_SHORT).show()
                }

                updateTasks()
            }
        }

        val settingsButton = findViewById<ImageButton>(R.id.settingsBtn)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)

            intent.putExtra("completed", completed)
            intent.putExtra("sort_by", sortBy)
            intent.putExtra("sort_direction", sortDirection)

            resultLauncher.launch(intent)
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

        if (completed == getString(R.string.all)) {
            // TODO
        } else {
            val completedValue = if (completed == getString(R.string.complete)) "true" else "false"

            viewModel.getTasks(completedValue, sortDirectionValue + sortByValue)
            viewModel.myResponseTasks.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    response.body()?.let {tasksAdapter.setData(it)}
                } else {
                    Toast.makeText(this, response.code().toString(), Toast.LENGTH_LONG).show()
                }
            })
        }

    }

}