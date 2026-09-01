package com.jointsense.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jointsense.app.data.local.dao.AssessmentDao
import com.jointsense.app.data.local.dao.UserDao
import com.jointsense.app.data.local.entity.AssessmentEntity
import com.jointsense.app.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, AssessmentEntity::class], version = 1, exportSchema = false)
abstract class JointSenseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun assessmentDao(): AssessmentDao
}
