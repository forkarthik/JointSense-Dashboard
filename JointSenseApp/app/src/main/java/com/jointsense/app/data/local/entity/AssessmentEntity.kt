package com.jointsense.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessments")
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val timestamp: Long,
    val isSensorBased: Boolean,
    val painScore: Int,
    val mobilityScore: Int,
    val rawSensorDataJson: String?,
    val aiRiskScore: Float,
    val riskCategory: String // "Low", "Moderate", "High"
)
