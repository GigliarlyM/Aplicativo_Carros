package com.tentesion.myapplication.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tentesion.myapplication.R

class CalcularAutonomiaActivity : AppCompatActivity() {

    lateinit var etPreco: EditText
    lateinit var etKm: EditText
    lateinit var ibExit: ImageButton
    lateinit var btnCalcular: Button
    lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
        setupViews()
        setupListener()
        setupCachedResult()
    }

    private fun setupCachedResult() {
        val valorCalculado = getSharedPref()
        tvResultado.text = valorCalculado.toString()
    }

    fun setupViews() {
        etPreco = findViewById(R.id.et_preco)
        etKm = findViewById(R.id.et_percorrido)
        ibExit = findViewById(R.id.ib_exit)
        btnCalcular = findViewById(R.id.btn_calcular)
        tvResultado = findViewById(R.id.tv_resultado)
    }

    fun setupListener() {
        ibExit.setOnClickListener { finish() }
        btnCalcular.setOnClickListener {
            val valorPreco = etPreco.text.toString().toFloat()
            val valorKm = etKm.text.toString().toFloat()

            val resultado = valorPreco * valorKm
            saveSharedPref(resultado)

            tvResultado.setText(resultado.toString())
        }
    }

    fun saveSharedPref(resultado: Float) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat(getString(R.string.saved_calc), 1f)
            apply()
        }
    }

    fun getSharedPref(): Float {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getFloat(getString(R.string.saved_calc), 0.0f)
    }

}