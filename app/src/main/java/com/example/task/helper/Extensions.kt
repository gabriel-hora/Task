package com.example.task.helper

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

//Extensão para Toolbar
fun Fragment.initToolbar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true) //Habilitando o botão de voltar
    toolbar.setNavigationOnClickListener { activity?.onBackPressed() } // botão padrão do android de back
    toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))
}
