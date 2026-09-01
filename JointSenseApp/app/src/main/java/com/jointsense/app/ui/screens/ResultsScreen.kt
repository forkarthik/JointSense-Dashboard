package com.jointsense.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navController: NavController, riskScore: Float) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analysis Results", fontWeight = FontWeight.Bold) },
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
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("FUSION AI RISK SCORE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "%.1f".format(riskScore),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (riskScore >= 80) Color(0xFFDC2626) else PrimaryTeal
                    )
                    Text("/ 100", color = TextGray, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val statusText = if (riskScore >= 85) "Severe OA Risk"
                                    else if (riskScore >= 70) "Moderate OA Risk"
                                    else "Low OA Risk"
                    
                    Text(statusText, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (riskScore >= 80) Color(0xFFDC2626) else DarkTeal, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Clinical Recommendations", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (riskScore >= 85) {
                        Text("• Immediate specialist referral recommended.", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• High probability of advanced cartilage degradation.", color = TextGray)
                    } else if (riskScore >= 70) {
                        Text("• Refer for MRI or X-Ray imaging.", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Recommend physical therapy and weight management.", color = TextGray)
                    } else {
                        Text("• Routine monitoring every 6-12 months.", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Encourage active lifestyle and low-impact exercises.", color = TextGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController.navigate("history") },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
            ) {
                Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Patient History", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
