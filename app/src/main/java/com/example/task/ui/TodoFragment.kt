package com.example.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.R
import com.example.task.databinding.FragmentTodoBinding
import com.example.task.helper.BaseFragment
import com.example.task.helper.FirebaseHelper
import com.example.task.model.Task
import com.example.task.ui.adapter.TaskAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class TodoFragment : BaseFragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val taskList = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()
        getTasks()
    }

    private fun initClicks() {
        binding.fabAdd.setOnClickListener() {
            val action = HomeFragmentDirections
                .actionHomeFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun getTasks() {
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        taskList.clear()
                        for (snap in snapshot.children) {
                            val task = snap.getValue(Task::class.java) as Task

                            if (task.status == 0) taskList.add(task)
                        }

                        taskEmpty()
                        binding.progressBar.isVisible = false

                        taskList.reverse()
                        initAdapter()
                    } else {
                        binding.textInfo.text = "Nenhuma tarefa Cadastrada"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "erro", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun taskEmpty() {
        binding.textInfo.text = if(taskList.isEmpty()){
            getText(R.string.text_task_list_empty_todo_fragment)
        } else {
            ""
        }
    }

    private fun initAdapter() {
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        taskAdapter = TaskAdapter(requireContext(), taskList) { task, select ->
            optionSelect(task, select)
        }
        binding.rvTask.adapter = taskAdapter
    }

    private fun optionSelect(task: Task, select: Int) {
        when (select) {
            TaskAdapter.SELECT_REMOVE -> {
                deleteTask(task)
            }
            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            TaskAdapter.SELECT_NEXT -> {
                task.status = 1
                updateTask(task)
            }
        }
    }

    private fun updateTask(task: Task) {
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Tarefa salva com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao salvar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { task ->
                binding.progressBar.isVisible = false
                Toast.makeText(requireContext(), "Não foi possível salvar", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun deleteTask(task: Task) {
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .removeValue()

        taskList.remove(task)
        taskAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}