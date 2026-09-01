package com.jointsense.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jointsense.app.data.local.entity.AssessmentEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AssessmentDao_Impl implements AssessmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AssessmentEntity> __insertionAdapterOfAssessmentEntity;

  public AssessmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAssessmentEntity = new EntityInsertionAdapter<AssessmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `assessments` (`id`,`userId`,`timestamp`,`isSensorBased`,`painScore`,`mobilityScore`,`rawSensorDataJson`,`aiRiskScore`,`riskCategory`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AssessmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getTimestamp());
        final int _tmp = entity.isSensorBased() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getPainScore());
        statement.bindLong(6, entity.getMobilityScore());
        if (entity.getRawSensorDataJson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getRawSensorDataJson());
        }
        statement.bindDouble(8, entity.getAiRiskScore());
        if (entity.getRiskCategory() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getRiskCategory());
        }
      }
    };
  }

  @Override
  public Object insertAssessment(final AssessmentEntity assessment,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAssessmentEntity.insertAndReturnId(assessment);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AssessmentEntity>> getAssessmentsForUser(final int userId) {
    final String _sql = "SELECT * FROM assessments WHERE userId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assessments"}, new Callable<List<AssessmentEntity>>() {
      @Override
      @NonNull
      public List<AssessmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsSensorBased = CursorUtil.getColumnIndexOrThrow(_cursor, "isSensorBased");
          final int _cursorIndexOfPainScore = CursorUtil.getColumnIndexOrThrow(_cursor, "painScore");
          final int _cursorIndexOfMobilityScore = CursorUtil.getColumnIndexOrThrow(_cursor, "mobilityScore");
          final int _cursorIndexOfRawSensorDataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "rawSensorDataJson");
          final int _cursorIndexOfAiRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "aiRiskScore");
          final int _cursorIndexOfRiskCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "riskCategory");
          final List<AssessmentEntity> _result = new ArrayList<AssessmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AssessmentEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsSensorBased;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSensorBased);
            _tmpIsSensorBased = _tmp != 0;
            final int _tmpPainScore;
            _tmpPainScore = _cursor.getInt(_cursorIndexOfPainScore);
            final int _tmpMobilityScore;
            _tmpMobilityScore = _cursor.getInt(_cursorIndexOfMobilityScore);
            final String _tmpRawSensorDataJson;
            if (_cursor.isNull(_cursorIndexOfRawSensorDataJson)) {
              _tmpRawSensorDataJson = null;
            } else {
              _tmpRawSensorDataJson = _cursor.getString(_cursorIndexOfRawSensorDataJson);
            }
            final float _tmpAiRiskScore;
            _tmpAiRiskScore = _cursor.getFloat(_cursorIndexOfAiRiskScore);
            final String _tmpRiskCategory;
            if (_cursor.isNull(_cursorIndexOfRiskCategory)) {
              _tmpRiskCategory = null;
            } else {
              _tmpRiskCategory = _cursor.getString(_cursorIndexOfRiskCategory);
            }
            _item = new AssessmentEntity(_tmpId,_tmpUserId,_tmpTimestamp,_tmpIsSensorBased,_tmpPainScore,_tmpMobilityScore,_tmpRawSensorDataJson,_tmpAiRiskScore,_tmpRiskCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
