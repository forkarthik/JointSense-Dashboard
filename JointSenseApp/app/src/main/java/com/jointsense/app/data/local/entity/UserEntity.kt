package com.jointsense.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: String,
    val heightCm: Float,
    val weightKg: Float,
    val occupation: String
)
