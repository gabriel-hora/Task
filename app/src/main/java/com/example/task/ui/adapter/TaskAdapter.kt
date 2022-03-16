package com.example.task.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.ItemAdapterBinding
import com.example.task.model.Task

class TaskAdapter(
    private val context: Context,
    private val taskList: List<Task>,
    val taskSelected: (Task, Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    companion object {
        val SELECT_BACK: Int = 1
        val SELECT_REMOVE: Int = 2
        val SELECT_EDIT: Int = 3
        val SELECT_DETAILS: Int = 4
        val SELECT_NEXT: Int = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskAdapter.MyViewHolder, position: Int) {
        val task = taskList[position]

        holder.binding.textTitle.text = task.description

        holder.binding.btnDelete.setOnClickListener { taskSelected(task, SELECT_REMOVE) }
        holder.binding.btnEdit.setOnClickListener { taskSelected(task, SELECT_EDIT) }
        holder.binding.btnDetails.setOnClickListener { taskSelected(task, SELECT_DETAILS) }

        when (task.status) {
            0 -> {
                holder.binding.ibBack.isVisible = false
                holder.binding.ibNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_doing)
                )

                holder.binding.ibNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }
            1 -> {
                holder.binding.ibNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_done)
                )

                holder.binding.ibBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_todo)
                )

                holder.binding.ibNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
                holder.binding.ibBack.setOnClickListener { taskSelected(task, SELECT_BACK) }

            }
            else -> {
                holder.binding.ibNext.isVisible = false

                holder.binding.ibBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_doing)
                )

                holder.binding.ibBack.setOnClickListener { taskSelected(task, SELECT_BACK) }

            }
        }
    }

    override fun getItemCount() = taskList.size

    inner class MyViewHolder(val binding: ItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)
}