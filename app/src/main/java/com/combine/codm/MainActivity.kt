package com.combine.codm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

val CBlack = Color(0xFF060A12)
val CNavy = Color(0xFF0C1424)
val CInput = Color(0xFF182942)
val CBlue = Color(0xFF1E88E5)
val CYellow = Color(0xFFFFC107)
val CWhite = Color(0xFFFFFFFF)
val CGray = Color(0xFF94A3B8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(background = CBlack, primary = CYellow)) {
                CombineAppRoot()
            }
        }
    }
}

@Composable
fun CombineAppRoot() {
    var screen by remember { mutableStateOf("splash") }
    when (screen) {
        "splash" -> SplashView { screen = "login" }
        "login" -> AuthView(isRegister = false, onSwitch = { screen = "register" }, onDone = { screen = "home" })
        "register" -> AuthView(isRegister = true, onSwitch = { screen = "login" }, onDone = { screen = "home" })
        "home" -> HomePreview { screen = "login" }
    }
}

@Composable
fun LogoView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(90.dp, 80.dp)) {
            val w = size.width; val h = size.height
            val crown = Path().apply {
                moveTo(w * 0.34f, h * 0.24f); lineTo(w * 0.30f, h * 0.08f)
                lineTo(w * 0.42f, h * 0.16f); lineTo(w * 0.50f, h * 0.04f)
                lineTo(w * 0.58f, h * 0.16f); lineTo(w * 0.70f, h * 0.08f)
                lineTo(w * 0.66f, h * 0.24f); close()
            }
            drawPath(crown, CYellow)
            drawArc(CWhite, 42f, 276f, false, Offset(w * 0.22f, h * 0.28f), Size(w * 0.56f, h * 0.62f), style = Stroke(14.dp.toPx()))
        }
        Text("COMBINE", color = CWhite, fontSize = 32.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic)
        Text("CALL OF DUTY MOBILE\nCOMMUNITY", color = CBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
fun SplashView(onDone: () -> Unit) {
    LaunchedEffect(Unit) { delay(2200); onDone() }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(CNavy, CBlack))).padding(32.dp)) {
        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            LogoView()
            Spacer(Modifier.height(20.dp))
            Text("Play  •  Connect  •  Compete", color = CYellow, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(Modifier.align(Alignment.BottomCenter).fillMaxWidth(0.7f), color = CYellow, trackColor = CInput)
    }
}

@Composable
fun AuthView(isRegister: Boolean, onSwitch: () -> Unit, onDone: () -> Unit) {
    var user by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(CNavy, CBlack))).verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        LogoView()
        Spacer(Modifier.height(20.dp))
        Text(if (isRegister) "Create Account" else "Login to Continue", color = CWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        if (isRegister) {
            OutlinedTextField(value = user, onValueChange = { user = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Username", color = CGray) }, leadingIcon = { Icon(Icons.Default.Person, null, tint = CBlue) }, singleLine = true, shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.height(12.dp))
        }
        OutlinedTextField(value = phone, onValueChange = { phone = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Phone Number / ID", color = CGray) }, leadingIcon = { Icon(Icons.Default.Person, null, tint = CBlue) }, singleLine = true, shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = pass, onValueChange = { pass = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Password", color = CGray) }, leadingIcon = { Icon(Icons.Default.Lock, null, tint = CBlue) }, visualTransformation = PasswordVisualTransformation(), singleLine = true, shape = RoundedCornerShape(12.dp))
        msg?.let { Spacer(Modifier.height(8.dp)); Text(it, color = CYellow, fontSize = 13.sp) }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { if (phone.isBlank() || pass.isBlank()) msg = "Jaza taarifa zote kwanza." else onDone() },
            modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CYellow, contentColor = CBlack)
        ) { Text(if (isRegister) "Register" else "Login", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold) }
        Spacer(Modifier.height(16.dp))
        Text(if (isRegister) "Already have an account? Login" else "Don't have an account? Register", color = CYellow, modifier = Modifier.clickable { onSwitch() })
    }
}

@Composable
fun HomePreview(onLogout: () -> Unit) {
    Box(Modifier.fillMaxSize().background(CBlack), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("COMBINE v1.0", color = CYellow, fontSize = 26.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
            Text("Karibu kwenye Jamii ya CODM!", color = CWhite, fontSize = 15.sp)
            Spacer(Modifier.height(20.dp))
            Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = CBlue, contentColor = CWhite)) { Text("Toka (Logout)") }
        }
    }
}
