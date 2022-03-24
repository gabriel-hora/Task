package com.example.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.task.R
import com.example.task.databinding.FragmentLoginBinding
import com.example.task.databinding.FragmentRecoverAccountBinding
import com.example.task.helper.BaseFragment
import com.example.task.helper.FirebaseHelper
import com.example.task.helper.initToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverAccountFragment : BaseFragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.idToolbar)
        initClicks()
        auth = Firebase.auth
    }

    private fun initClicks() {
        binding.btnEnviar.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim()

        if (email.isNotEmpty()) {
            hideKeyboard()
            binding.progressBar.isVisible = true
            recoverUser(email)
        } else {
            Toast.makeText(requireContext(), "informe seu e-mail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recoverUser(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "E-mail enviado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        FirebaseHelper.validError(task.exception?.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.progressBar.isVisible = false
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}