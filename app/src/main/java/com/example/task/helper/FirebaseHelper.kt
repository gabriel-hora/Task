package com.example.task.helper

import com.example.task.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {

    companion object {

        fun getDatabase() = FirebaseDatabase.getInstance().reference //Recuperar a Instância do Firebase (Referência)

        private fun getAuth() = FirebaseAuth.getInstance() //Recuperar a instância do usuário (Verificar se está "Autenticado no Banco")

        fun getIdUser() = getAuth().uid //Recuperar o ID do usuario Logado

        fun inAutenticated() = getAuth().currentUser != null //Verificar se está logado no APP


        // Validação de erros em Inglês e transformar em PT
        fun validError(error: String): Int {
            return when {
                error.contains("There is no user record corresponding to this identifier") -> {
                    R.string.account_not_registered_register_fragment
                }
                error.contains("The email address is badly formatted") -> {
                    R.string.invalid_email_register_fragment
                }
                error.contains("The password is invalid or the user does not have a password") -> {
                    R.string.invalid_password_register_fragment
                }
                error.contains("The email address is already in use by another account") -> {
                    R.string.email_in_use_register_fragment
                }
                error.contains("The given password is invalid. [ Password should be at least 6 characters ]") -> {
                    R.string.strong_password_register_fragment
                }
                else -> {
                    R.string.error_generic
                }
            }
        }
    }

}