package com.example.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.task.R
import com.example.task.databinding.FragmentFormTaskBinding
import com.example.task.helper.BaseFragment
import com.example.task.helper.FirebaseHelper
import com.example.task.helper.initToolbar
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase

class FormTaskFragment : BaseFragment() {

    private val args: FormTaskFragmentArgs by navArgs()

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
        initToolbar(binding.idToolbar)

        initListener()
        getArgs()
    }

    private fun getArgs() {
        args.task.let {
            if (it != null) {
                task = it
                configTask()
            }
        }
    }

    private fun configTask() {
        newTask = false
        statusTask = task.status
        binding.txtToolbar.text = "Editando tarefa.."

        binding.description.setText(task.description)
        setStatus()
    }

    private fun setStatus() {
        binding.radioGroup.check(
            when (task.status) {
                0 -> {
                    R.id.rbTodo
                }
                1 -> {
                    R.id.rbDoing
                }
                else -> {
                    R.id.rbDone
                }
            }
        )
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

            hideKeyboard()

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
                    if (newTask) { // Nova Tarefa
                        findNavController().popBackStack()
                        Toast.makeText(
                            requireContext(),
                            "Tarefa salva com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {//Editando tarefa
                        binding.progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Tarefa salva com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
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