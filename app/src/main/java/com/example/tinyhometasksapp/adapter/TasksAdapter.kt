package com.example.tinyhometasksapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tinyhometasksapp.R
import com.example.tinyhometasksapp.model.Task
import com.example.tinyhometasksapp.util.stringDateTimeToReadable

class TasksAdapter (private val listener: TaskCardBtnsClickListener): RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private var tasksList = emptyList<Task>()

    inner class TaskViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescriptionText)
        val createdDate: TextView = itemView.findViewById(R.id.createdDateText)
        val dueDate: TextView = itemView.findViewById(R.id.dueDateText)
        val editBtn: ImageButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteBtn)
        val completedCheckBox: CheckBox = itemView.findViewById(R.id.completedCheckBox)

        fun bind(task: Task) {
            editBtn.setOnClickListener { listener.onEditClick(task) }
            deleteBtn.setOnClickListener {
                listener.onDeleteClick(task)
                disableCard()
            }
            completedCheckBox.setOnClickListener {
                task.completed = completedCheckBox.isChecked
                listener.onCompletedStatusClick(task)
                disableCard()
            }
        }

        fun disableCard() {
            editBtn.isEnabled = false
            deleteBtn.isEnabled = false
            completedCheckBox.isEnabled = false
        }

        fun enableCard() {
            editBtn.isEnabled = true
            deleteBtn.isEnabled = true
            completedCheckBox.isEnabled = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = tasksList[position]
        holder.taskDescription.text = currentItem.taskDescription.toString()
        holder.dueDate.text = "Due: " + stringDateTimeToReadable(currentItem.dueDate)
        holder.createdDate.text = "Created: " + stringDateTimeToReadable(currentItem.createdDate)
        holder.completedCheckBox.setChecked(currentItem.completed).toString()

        holder.bind(currentItem)
        holder.enableCard()
    }

    override fun getItemCount(): Int = tasksList.size

    fun setData(newTasks: List<Task>) {
        tasksList = newTasks
        notifyDataSetChanged()
    }
}