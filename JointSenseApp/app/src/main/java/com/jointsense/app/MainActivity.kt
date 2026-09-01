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
import androidx.navigation.compose.rememberNavController
import com.jointsense.app.ui.JointSenseNavGraph

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

@Composable
fun JointSenseApp() {
    val navController = rememberNavController()
    JointSenseNavGraph(navController = navController)
}
