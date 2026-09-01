package com.jointsense.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JointSenseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JointSenseApp()
                }
            }
        }
    }
}

// ==========================================
// THEME & COLORS (Medical App Style)
// ==========================================
val PrimaryTeal = Color(0xFF00B4D8)
val DarkTeal = Color(0xFF0077B6)
val LightBg = Color(0xFFF8FAFC)
val SurfaceWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1E293B)
val TextGray = Color(0xFF64748B)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    secondary = DarkTeal,
    background = LightBg,
    surface = SurfaceWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    error = Color(0xFFEF4444)
)

@Composable
fun JointSenseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}

// ==========================================
// NAVIGATION ENUM
// ==========================================
enum class Screen { Login, Dashboard }

@Composable
fun JointSenseApp() {
    var currentScreen by remember { mutableStateOf(Screen.Login) }

    when (currentScreen) {
        Screen.Login -> LoginScreen(onLoginSuccess = { currentScreen = Screen.Dashboard })
        Screen.Dashboard -> DashboardScreen(onLogout = { currentScreen = Screen.Login })
    }
}

// ==========================================
// LOGIN SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1500) // Simulate network call
            isLoading = false
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo Placeholder
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PrimaryTeal.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Favorite, contentDescription = "Logo", tint = PrimaryTeal, modifier = Modifier.size(40.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "JointSense",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryTeal
        )
        Text(
            text = "Clinical Assistant",
            fontSize = 16.sp,
            color = TextGray
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Doctor ID or Email") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PrimaryTeal,
                unfocusedBorderColor = Color.LightGray
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PrimaryTeal,
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { isLoading = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// DASHBOARD SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit) {
    var gyro by remember { mutableFloatStateOf(0f) }
    var piezo by remember { mutableFloatStateOf(0f) }
    var rom by remember { mutableIntStateOf(120) }
    
    // Clinical Input
    var painScore by remember { mutableFloatStateOf(0f) }
    var klGrade by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while(true) {
            gyro = (-10..10).random().toFloat()
            piezo = (0..5).random().toFloat() / 10f
            rom = (110..130).random()
            delay(1000)
        }
    }

    val riskScore = 68 + (painScore.toInt() * 2) + (klGrade.toInt() * 4)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Assessment", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceWhite,
                    titleContentColor = TextDark
                )
            )
        },
        containerColor = LightBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Patient Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .background(PrimaryTeal.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("JD", color = PrimaryTeal, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("John Doe", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Age: 54 | Male | ID: #98342", color = TextGray, fontSize = 14.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("LIVE SENSOR TELEMETRY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Sensor Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SensorCard(title = "Gait Symmetry", value = "$gyro °/s", modifier = Modifier.weight(1f))
                SensorCard(title = "Crepitus", value = "$piezo V", modifier = Modifier.weight(1f))
                SensorCard(title = "Flexion ROM", value = "$rom°", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("CLINICAL INPUT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Clinical Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pain Severity (0-10): ${painScore.toInt()}", fontWeight = FontWeight.Medium)
                    Slider(
                        value = painScore,
                        onValueChange = { painScore = it },
                        valueRange = 0f..10f,
                        colors = SliderDefaults.colors(thumbColor = PrimaryTeal, activeTrackColor = PrimaryTeal)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Prior X-Ray KL Grade: KL ${klGrade.toInt()}", fontWeight = FontWeight.Medium)
                    Slider(
                        value = klGrade,
                        onValueChange = { klGrade = it },
                        valueRange = 0f..4f,
                        steps = 3,
                        colors = SliderDefaults.colors(thumbColor = PrimaryTeal, activeTrackColor = PrimaryTeal)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Risk Score Result
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (riskScore >= 80) Color(0xFFFEF2F2) else PrimaryTeal.copy(alpha = 0.05f)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("FUSION AI RISK SCORE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$riskScore",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (riskScore >= 80) Color(0xFFDC2626) else PrimaryTeal
                    )
                    Text("/ 100", color = TextGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val statusText = if (riskScore >= 85) "Severe Risk · Immediate specialist referral"
                                    else if (riskScore >= 70) "Moderate Risk · Refer for imaging"
                                    else "Low Risk · Routine monitoring"
                    
                    Text(statusText, fontWeight = FontWeight.Medium, color = if (riskScore >= 80) Color(0xFFDC2626) else DarkTeal, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun SensorCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 11.sp, color = TextGray, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryTeal)
        }
    }
}
