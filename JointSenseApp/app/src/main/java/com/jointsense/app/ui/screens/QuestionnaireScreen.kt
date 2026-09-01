package com.jointsense.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jointsense.app.PrimaryTeal
import com.jointsense.app.SurfaceWhite
import com.jointsense.app.TextDark
import com.jointsense.app.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireScreen(navController: NavController) {
    // State variables
    var stiffnessDuration by remember { mutableStateOf("") }
    var esr by remember { mutableStateOf("") }
    var crp by remember { mutableStateOf("") }

    var previousInjury by remember { mutableStateOf("None") }
    val injuryOptions = listOf("None", "Meniscus tear", "ACL / ligament injury", "Fracture near knee", "Other / Unknown")

    var quadWasting by remember { mutableStateOf("None") }
    val quadOptions = listOf("None", "Mild atrophy", "Moderate", "Severe")

    var klGrade by remember { mutableStateOf("Not available") }
    val klOptions = listOf("Not available", "KL 0", "KL 1", "KL 2", "KL 3", "KL 4")

    val painTriggers = remember { mutableStateListOf("Stairs", "Squatting", "Night Pain") } // Default checked
    val allTriggers = listOf("Stairs", "Squatting", "Walking", "At Rest", "Night Pain")

    var tenderness by remember { mutableStateOf("Mild") }
    val severityOptions = listOf("None", "Mild", "Moderate", "Severe")

    var effusion by remember { mutableStateOf("None") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clinical Assessment", fontWeight = FontWeight.Bold) },
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
                .padding(24.dp)
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Symptom Questionnaire", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Please enter clinical findings to augment sensor data.", fontSize = 14.sp, color = TextGray)
            
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Previous Knee Injury
                    DropdownField("Previous Knee Injury", previousInjury, injuryOptions) { previousInjury = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Morning Stiffness
                    OutlinedTextField(
                        value = stiffnessDuration,
                        onValueChange = { stiffnessDuration = it },
                        label = { Text("Morning Stiffness Duration (min)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Pain Triggers
                    Text("Pain Triggers", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                    allTriggers.forEach { trigger ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (painTriggers.contains(trigger)) painTriggers.remove(trigger)
                                    else painTriggers.add(trigger)
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = painTriggers.contains(trigger),
                                onCheckedChange = { checked ->
                                    if (checked) painTriggers.add(trigger)
                                    else painTriggers.remove(trigger)
                                }
                            )
                            Text(trigger)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Joint Line Tenderness
                    Text("Joint Line Tenderness (Doctor's Exam)", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                    SegmentedControl(severityOptions, tenderness) { tenderness = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Palpable Effusion
                    Text("Palpable Effusion", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                    SegmentedControl(severityOptions, effusion) { effusion = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Quadriceps Wasting
                    DropdownField("Quadriceps Wasting", quadWasting, quadOptions) { quadWasting = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Prior X-Ray KL Grade
                    DropdownField("Prior X-Ray KL Grade", klGrade, klOptions) { klGrade = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Labs
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = esr,
                            onValueChange = { esr = it },
                            label = { Text("ESR (mm/hr)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = crp,
                            onValueChange = { crp = it },
                            label = { Text("CRP (mg/L)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController.navigate("data_collection") },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
            ) {
                Text("Start Data Collection", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(label: String, selectedOption: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SegmentedControl(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            OutlinedButton(
                onClick = { onOptionSelected(option) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) PrimaryTeal else Color.Transparent,
                    contentColor = if (isSelected) Color.White else PrimaryTeal
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(option, fontSize = 12.sp, maxLines = 1)
            }
        }
    }
}
