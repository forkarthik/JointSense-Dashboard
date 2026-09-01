package com.jointsense.app.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/jointsense/app/data/local/JointSenseDatabase;", "Landroidx/room/RoomDatabase;", "()V", "assessmentDao", "Lcom/jointsense/app/data/local/dao/AssessmentDao;", "userDao", "Lcom/jointsense/app/data/local/dao/UserDao;", "app_debug"})
@androidx.room.Database(entities = {com.jointsense.app.data.local.entity.UserEntity.class, com.jointsense.app.data.local.entity.AssessmentEntity.class}, version = 1, exportSchema = false)
public abstract class JointSenseDatabase extends androidx.room.RoomDatabase {
    
    public JointSenseDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract com.jointsense.app.data.local.dao.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.jointsense.app.data.local.dao.AssessmentDao assessmentDao();
}