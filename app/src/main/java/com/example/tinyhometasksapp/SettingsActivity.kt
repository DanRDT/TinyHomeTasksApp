package com.example.tinyhometasksapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {



    private lateinit var filterRadioGroup: RadioGroup
    private lateinit var sortByRadioGroup: RadioGroup
    private lateinit var sortDirectionRadioGroup: RadioGroup
    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRadioGroups()

        saveBtn = findViewById(R.id.saveSettingsBtn)
        saveBtn.setOnClickListener {
            val intent = Intent()

            val selectedFilter = getSelectedRadioButtonText(filterRadioGroup)
            val selectedSortBy = getSelectedRadioButtonText(filterRadioGroup)
            val selectedSortDirection = getSelectedRadioButtonText(filterRadioGroup)

            intent.putExtra("completed", selectedFilter)
            intent.putExtra("sort_by", selectedSortBy)
            intent.putExtra("sort_direction", selectedSortDirection)

            setResult(RESULT_OK, intent)
            finish()
        }


        val intent = intent
        val completed = intent.getStringExtra("completed")
        val sortBy = intent.getStringExtra("sort_by")
        val sortDirection = intent.getStringExtra("sort_direction")
        Toast.makeText(this, sortBy, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, sortDirection, Toast.LENGTH_SHORT).show()

        val allString = getString(R.string.all)
        val completeString = getString(R.string.complete)
        val incompleteString = getString(R.string.incomplete)
        val dueString = getString(R.string.due)
        val createdString = getString(R.string.created)
        val ascendingString = getString(R.string.ascending)
        val descendingString = getString(R.string.descending)

        when (completed) {
            completeString -> {
                filterRadioGroup.check(R.id.completeRadioBtn)
            }
            incompleteString -> {
                filterRadioGroup.check(R.id.incompleteRadioBtn)
            }
            allString -> {
                filterRadioGroup.check(R.id.allRadioBtn)
            }
        }

        when (sortBy) {
            dueString -> {
                sortByRadioGroup.check(R.id.dueRadioBtn)
            }
            createdString -> {
                sortByRadioGroup.check(R.id.createdRadioBtn)
            }
        }

        when (sortDirection) {
            ascendingString -> {
                sortDirectionRadioGroup.check(R.id.ascRadioBtn)
            }
            descendingString -> {
                sortDirectionRadioGroup.check(R.id.descRadioBtn)
            }
        }

    }

    private fun setupRadioGroups() {
        filterRadioGroup = findViewById(R.id.filtersRadioGroup)
        sortByRadioGroup = findViewById(R.id.sortByRadioGroup)
        sortDirectionRadioGroup = findViewById(R.id.sortDirectionRadioGroup)
    }

    private fun getSelectedRadioButtonText(radioGroup: RadioGroup): String {
        val selectedId = radioGroup.checkedRadioButtonId
        val selectedRadioButton: RadioButton? = radioGroup.findViewById(selectedId)

        return selectedRadioButton?.text?.toString().toString()
    }

}