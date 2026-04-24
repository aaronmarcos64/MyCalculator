package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    private lateinit var displayTxt: TextView
    private lateinit var switchModo: Switch

    private var textoActual = ""
    private var valorGuardado = 0.0
    private var operacionActiva = ""
    private var limpiarSiguiente = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTxt = findViewById(R.id.screen)
        switchModo = findViewById(R.id.switchAngleMode)

        switchModo.setOnCheckedChangeListener { _, activo ->
            switchModo.text = if (activo) "Radianes" else "Grados"
        }

        iniciarEventos()
    }

    private fun iniciarEventos() {
        val botonesNumericos = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot
        )

        botonesNumericos.forEach { idBtn ->
            findViewById<Button>(idBtn).setOnClickListener { vista ->
                escribirDigito((vista as Button).text.toString())
            }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { asignarOperacion("+") }
        findViewById<Button>(R.id.btnSub).setOnClickListener { asignarOperacion("-") }
        findViewById<Button>(R.id.btnMul).setOnClickListener { asignarOperacion("*") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { asignarOperacion("/") }

        findViewById<Button>(R.id.btnSin).setOnClickListener { resolverTrigonometria("Sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { resolverTrigonometria("Cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { resolverTrigonometria("Tan") }

        findViewById<Button>(R.id.btnEq).setOnClickListener { ejecutarCalculoFinal() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { reiniciarCalculadora() }
    }

    private fun escribirDigito(digito: String) {
        if (limpiarSiguiente) {
            displayTxt.text = ""
            limpiarSiguiente = false
        }
        displayTxt.append(digito)
        textoActual = displayTxt.text.toString()
    }

    private fun asignarOperacion(simbolo: String) {
        if (textoActual.isNotEmpty()) {
            if (operacionActiva.isEmpty()) {
                valorGuardado = textoActual.toDouble()
            } else {
                valorGuardado = aplicarOperacion(valorGuardado, textoActual.toDouble(), operacionActiva)
                displayTxt.text = limpiarDecimales(valorGuardado)
            }
        }
        operacionActiva = simbolo
        limpiarSiguiente = true
    }

    private fun resolverTrigonometria(tipoFunc: String) {
        if (textoActual.isEmpty()) textoActual = "0"
        var numero = textoActual.toDouble()

        if (!switchModo.isChecked) {
            numero = Math.toRadians(numero)
        }

        val calculoTrig = when (tipoFunc) {
            "Sin" -> sin(numero)
            "Cos" -> cos(numero)
            "Tan" -> tan(numero)
            else -> 0.0
        }

        displayTxt.text = limpiarDecimales(calculoTrig)
        textoActual = calculoTrig.toString()
        valorGuardado = calculoTrig
        limpiarSiguiente = true
        operacionActiva = ""
    }

    private fun reiniciarCalculadora() {
        textoActual = ""
        valorGuardado = 0.0
        operacionActiva = ""
        limpiarSiguiente = true
        displayTxt.text = "0"
    }

    private fun aplicarOperacion(num1: Double, num2: Double, op: String): Double {
        return when (op) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> if (num2 != 0.0) num1 / num2 else 0.0
            else -> num1
        }
    }

    private fun limpiarDecimales(valFinal: Double): String {
        return if (valFinal % 1.0 == 0.0) {
            valFinal.toLong().toString()
        } else {
            valFinal.toString()
        }
    }

    private fun ejecutarCalculoFinal() {
        if (textoActual.isNotEmpty() && operacionActiva.isNotEmpty()) {
            valorGuardado = aplicarOperacion(valorGuardado, textoActual.toDouble(), operacionActiva)
            displayTxt.text = limpiarDecimales(valorGuardado)

            operacionActiva = ""
            textoActual = valorGuardado.toString()
            limpiarSiguiente = true
        }
    }
}
