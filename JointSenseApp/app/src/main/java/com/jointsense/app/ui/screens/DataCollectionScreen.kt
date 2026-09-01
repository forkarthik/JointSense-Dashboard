package com.jointsense.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jointsense.app.DarkTeal
import com.jointsense.app.PrimaryTeal
import com.jointsense.app.SurfaceWhite
import com.jointsense.app.TextDark
import com.jointsense.app.TextGray
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataCollectionScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var gyro by remember { mutableFloatStateOf(0f) }
    var piezo by remember { mutableFloatStateOf(0f) }
    var rom by remember { mutableIntStateOf(120) }
    var isCollecting by remember { mutableStateOf(false) }

    LaunchedEffect(isCollecting) {
        if (isCollecting) {
            while (true) {
                gyro = (-10..10).random().toFloat()
                piezo = (0..5).random().toFloat() / 10f
                rom = (110..130).random()
                delay(1000)
            }
        } else {
            gyro = 0f
            piezo = 0f
            rom = 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensor Telemetry", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceWhite,
                    titleContentColor = TextDark
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            if (isCollecting) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFEAB308), modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Collecting Live Data", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Ensure the wearable is positioned correctly.", fontSize = 14.sp, color = TextGray)
            } else {
                Text("Hardware Connection", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Bluetooth disconnected. You can use manual/random values.", fontSize = 14.sp, color = TextGray, textAlign = TextAlign.Center)
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // Sensor Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SensorCard(title = "Gait Symmetry", value = "$gyro °/s", modifier = Modifier.weight(1f))
                SensorCard(title = "Crepitus", value = "$piezo V", modifier = Modifier.weight(1f))
                SensorCard(title = "Flexion ROM", value = "$rom°", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = { isCollecting = !isCollecting },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isCollecting) Color(0xFFDC2626) else PrimaryTeal)
            ) {
                Text(if (isCollecting) "Stop Collection" else "Start Live Feed", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            if (!isCollecting) {
                OutlinedButton(
                    onClick = {
                        val model = com.jointsense.app.ml.JointSenseModel(context)
                        // Generate dummy 10-timestep data for Manual Run
                        val dummyData = Array(10) { FloatArray(3) { Math.random().toFloat() * 10f } }
                        val score = model.analyzeRisk(dummyData)
                        model.close()
                        navController.navigate("results/${score}")
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Run AI Analysis (Manual Feed)", fontSize = 16.sp, color = DarkTeal)
                }
            } else {
                OutlinedButton(
                    onClick = {
                        val model = com.jointsense.app.ml.JointSenseModel(context)
                        // Use recent mock live data
                        val liveData = Array(10) { FloatArray(3) { Math.random().toFloat() * 10f } }
                        val score = model.analyzeRisk(liveData)
                        model.close()
                        navController.navigate("results/${score}")
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Analyze Captured Data", fontSize = 16.sp, color = DarkTeal)
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
