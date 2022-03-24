package com.example.task.model

import android.os.Parcelable
import com.example.task.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize

//Parcelable serve para nivelar os dados e colocar em um objeto parcelable.
//Usado para enviar objetos entre fragments
@Parcelize
data class Task(
    var id: String = "",
    var description: String = "",
    var status: Int = 0
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
    }
}