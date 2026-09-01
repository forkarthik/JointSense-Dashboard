package com.jointsense.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jointsense.app.PrimaryTeal
import com.jointsense.app.SurfaceWhite
import com.jointsense.app.TextDark
import com.jointsense.app.TextGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    // Dummy Data for History (Would typically come from Room DB AssessmentDao)
    val pastAssessments = listOf(
        Pair(Date(System.currentTimeMillis()), 88.2f),
        Pair(Date(System.currentTimeMillis() - 86400000L * 30), 75.4f),
        Pair(Date(System.currentTimeMillis() - 86400000L * 90), 62.1f)
    )

    val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient History", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
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
                .padding(16.dp)
        ) {
            
            Text("Past Assessments", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pastAssessments.size) { index ->
                    val (date, score) = pastAssessments[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(dateFormat.format(date), fontWeight = FontWeight.Medium)
                                Text(if (score >= 80) "Severe Risk" else if (score >= 70) "Moderate Risk" else "Low Risk", color = TextGray, fontSize = 12.sp)
                            }
                            Text(
                                text = "%.1f".format(score),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = if (score >= 80) Color(0xFFDC2626) else PrimaryTeal
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout Session", fontSize = 16.sp, color = Color(0xFFDC2626))
            }
        }
    }
}
