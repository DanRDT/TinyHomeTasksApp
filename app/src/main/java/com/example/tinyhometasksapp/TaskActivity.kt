package com.example.tinyhometasksapp

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tinyhometasksapp.ViewModel.MainViewModel
import com.example.tinyhometasksapp.ViewModel.MainViewModelFactory
import com.example.tinyhometasksapp.model.NewTask
import com.example.tinyhometasksapp.model.Task
import com.example.tinyhometasksapp.repository.Repository
import com.example.tinyhometasksapp.util.dateTimeObjectToISO8601
import retrofit2.Response
import java.time.LocalDateTime

class TaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var dateBtn: ImageButton
    private lateinit var dateTxt: EditText
    private lateinit var descriptionTxt: EditText


    private lateinit var dateTime: LocalDateTime
    private var mode: String = ""

    private lateinit var viewModel: MainViewModel
    private lateinit var createTaskObserver: Observer<Response<Task>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.taskActivityRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val requestedAction = intent.getStringExtra("requested_action")

        dateTxt = findViewById(R.id.dateTxt)
        descriptionTxt = findViewById(R.id.taskDescriptionTxtEditor)
        val title = findViewById<TextView>(R.id.taskTitleTxt)

        when (requestedAction) {
            "create" -> {
                mode = requestedAction
                title.text = "Create"
                dateTime = LocalDateTime.now()
            }
            "edit" -> {
                mode = requestedAction
                title.text = "Edit"
                // TODO: fix dataTime
                dateTime = LocalDateTime.now()
            }
            else -> {
                Toast.makeText(this, "Error: Invalid Mode", Toast.LENGTH_SHORT).show()
                cancelPage()
            }
        }

        updateDisplayDate()

        val viewModelFactory = MainViewModelFactory(Repository())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        createTaskObserver = Observer { response ->
            if (response.isSuccessful) response.body()?.let {
                val returnIntent = Intent()
                returnIntent.putExtra("return_action", "save")
                setResult(RESULT_OK, returnIntent)
                finish()
            } else {
                Toast.makeText(this, response.code().toString(), Toast.LENGTH_LONG).show()
                resumePage()
            }
        }
        viewModel.responseCreateTask.observe(this, createTaskObserver)


        saveBtn = findViewById(R.id.saveTaskBtn)
        saveBtn.setOnClickListener {
            if (mode == "create") {
                Toast.makeText(this, "Creating...", Toast.LENGTH_SHORT).show()
                freezePage()
                viewModel.createTask(NewTask(descriptionTxt.text.toString(), dateTimeObjectToISO8601(dateTime), false))
            } else if (mode == "edit") {
                // TODO
            } else {
                Toast.makeText(this, "Error: Invalid Mode", Toast.LENGTH_SHORT).show()
                cancelPage()
            }




        }

        cancelBtn = findViewById(R.id.cancelBtn)
        cancelBtn.setOnClickListener { cancelPage() }

        dateBtn = findViewById(R.id.dateBtn)
        dateBtn.setOnClickListener(View.OnClickListener { showDatePickerDialog() })



    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            dateTime.year,
            dateTime.monthValue - 1, // needs 0-11
            dateTime.dayOfMonth
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateTime = LocalDateTime.of(year, month + 1 /* needs 1-12*/, dayOfMonth, 0, 0)
        updateDisplayDate()
    }

    private fun updateDisplayDate() {
        dateTxt.setText("${dateTime.monthValue}/${dateTime.dayOfMonth}/${dateTime.year}")
    }

    private fun cancelPage() {
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun freezePage() {
        saveBtn.isEnabled = false
        dateBtn.isEnabled = false
        cancelBtn.isEnabled = false
        descriptionTxt.isEnabled = false

        saveBtn.setTextColor(Color.WHITE)

        val rootLayout: ConstraintLayout = findViewById(R.id.taskActivityRoot)
        rootLayout.setBackgroundColor(Color.LTGRAY)
    }

    private fun resumePage() {
        dateBtn.isEnabled = true
        saveBtn.isEnabled = true
        cancelBtn.isEnabled = true
        descriptionTxt.isEnabled = true

        val rootLayout: ConstraintLayout = findViewById(R.id.taskActivityRoot)
        rootLayout.setBackgroundColor(Color.TRANSPARENT)
    }
}