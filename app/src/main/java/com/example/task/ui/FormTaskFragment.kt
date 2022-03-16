package com.example.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.task.R
import com.example.task.databinding.FragmentFormTaskBinding
import com.example.task.helper.FirebaseHelper
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: com.example.task.model.Task
    private var newTask: Boolean = true
    private var statusTask: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.btnSave.setOnClickListener { validateTask() }

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            statusTask = when (id) {
                R.id.rbTodo -> 0
                R.id.rbDoing -> 1
                else -> 2
            }

        }
    }

    private fun validateTask() {

        val description = binding.description.text.toString()

        if (description.isNotEmpty()) {

            binding.progressBar.isVisible = true

            if (newTask) task = com.example.task.model.Task()
            task.description = description
            task.status = statusTask

            saveTask()

        } else {
            Toast.makeText(
                requireContext(),
                "Informe uma descrição para a Tarefa",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun saveTask() {
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (newTask){ // Nova Tarefa
                        findNavController().popBackStack()
                        Toast.makeText(requireContext(), "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show()
                    } else {//Editando tarefa
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(), "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro ao salvar", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .addOnFailureListener { task ->
                binding.progressBar.isVisible = false
                Toast.makeText(requireContext(), "Não foi possível salvar", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}