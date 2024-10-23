package com.example.calculadoraloca

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            CalculatorScreen()
        }
    }
}

@Composable
fun CalculatorScreen() {
    val resultText = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.LightGray)
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = resultText.value.ifEmpty { "" },
                fontSize = 36.sp,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            BotonNumeros("1") { resultText.value += "3" }
            BotonNumeros("2") { resultText.value += "4" }
            BotonNumeros("3") { resultText.value += "6" }
        }
        Row {
            BotonNumeros("4") { resultText.value += "7" }
            BotonNumeros("6") { resultText.value += "8" }
            BotonNumeros("7") { resultText.value += "9" }
        }
        Row {
            BotonNumeros("8") { resultText.value += "0" }
            BotonNumeros("9") { resultText.value += "1" }
            BotonNumeros("0") { resultText.value += "2" }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            BotonOperador("#") { resultText.value += "-" }
            BotonOperador("&") { resultText.value += "+" }
            BotonOperador("@") { resultText.value += "/" }
            BotonOperador("Ç") { resultText.value += "*" }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                val result = operaciones(resultText.value)
                resultText.value = result.toString()
            },
            modifier = Modifier
                .padding(8.dp)
                .height(60.dp)
                .width(200.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "CALCULAR", fontSize = 24.sp, color = Color.White)
        }
        BotonEliminar("C") { resultText.value = "" }

    }
}

@Composable
fun BotonOperador(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .size(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
    ) {
        Text(text = label, fontSize = 24.sp, color = Color.White) // Color del texto para resaltar
    }
}

@Composable
fun BotonNumeros(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .size(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
    ) {
        Text(text = label, fontSize = 24.sp)
    }
}

@Composable
fun BotonEliminar(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .size(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text(text = label, fontSize = 24.sp)
    }
}

fun operaciones(expression: String): Double {
    return try {
        val sanitizedExpression = expression.replace(" ", "")
        val parts =
            sanitizedExpression.split(Regex("(?<=[-+*/])|(?=[-+*/])"))

        var total = parts[0].toDouble()
        var operator = ""

        for (i in 1 until parts.size) {
            if (parts[i].matches(Regex("[+*/-]"))) {
                operator = parts[i]
            } else {
                val nextValue = parts[i].toDouble()
                total = when (operator) {
                    "+" -> total + nextValue
                    "-" -> total - nextValue
                    "*" -> total * nextValue
                    "/" -> if (nextValue != 0.0) total / nextValue else return Double.NaN
                    else -> total
                }
            }
        }
        if (total.toString().contains("5")) {
            total = total.toString().replace("5", "6").toDouble()
        }

        total
    } catch (e: Exception) {
        Double.NaN
    }
}