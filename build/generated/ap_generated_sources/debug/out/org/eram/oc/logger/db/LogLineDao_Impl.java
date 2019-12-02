package org.eram.oc.logger.db;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unchecked")
public final class LogLineDao_Impl implements LogLineDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfLogLine;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfLogLine;

  private final SharedSQLiteStatement __preparedStmtOfReinit;

  public LogLineDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLogLine = new EntityInsertionAdapter<LogLine>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `loggers`(`date`,`appName`,`taskName`,`execLocation`,`edgeIP`,`rtt`,`uploadRate`,`downloadRate`,`networkTime`,`execTime`,`time`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LogLine value) {
        if (value.getDate() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getDate());
        }
        if (value.getAppName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getAppName());
        }
        if (value.getTaskName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTaskName());
        }
        if (value.getExecLocation() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getExecLocation());
        }
        if (value.getEdgeIP() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEdgeIP());
        }
        stmt.bindDouble(6, value.getRtt());
        stmt.bindDouble(7, value.getUploadRate());
        stmt.bindDouble(8, value.getDownloadRate());
        stmt.bindDouble(9, value.getNetworkTime());
        stmt.bindDouble(10, value.getExecTime());
        stmt.bindDouble(11, value.getTime());
      }
    };
    this.__deletionAdapterOfLogLine = new EntityDeletionOrUpdateAdapter<LogLine>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `loggers` WHERE `date` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LogLine value) {
        if (value.getDate() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getDate());
        }
      }
    };
    this.__preparedStmtOfReinit = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from loggers";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final LogLine... logs) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfLogLine.insert(logs);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll(final LogLine... logs) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfLogLine.handleMultiple(logs);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void reinit() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfReinit.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfReinit.release(_stmt);
    }
  }

  @Override
  public LiveData<List<LogLine>> getALl() {
    final String _sql = "SELECT * FROM loggers";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"loggers"}, new Callable<List<LogLine>>() {
      @Override
      public List<LogLine> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
          final int _cursorIndexOfExecLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "execLocation");
          final int _cursorIndexOfEdgeIP = CursorUtil.getColumnIndexOrThrow(_cursor, "edgeIP");
          final int _cursorIndexOfRtt = CursorUtil.getColumnIndexOrThrow(_cursor, "rtt");
          final int _cursorIndexOfUploadRate = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadRate");
          final int _cursorIndexOfDownloadRate = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadRate");
          final int _cursorIndexOfNetworkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "networkTime");
          final int _cursorIndexOfExecTime = CursorUtil.getColumnIndexOrThrow(_cursor, "execTime");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final List<LogLine> _result = new ArrayList<LogLine>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final LogLine _item;
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final String _tmpTaskName;
            _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
            final String _tmpExecLocation;
            _tmpExecLocation = _cursor.getString(_cursorIndexOfExecLocation);
            final String _tmpEdgeIP;
            _tmpEdgeIP = _cursor.getString(_cursorIndexOfEdgeIP);
            final double _tmpRtt;
            _tmpRtt = _cursor.getDouble(_cursorIndexOfRtt);
            final double _tmpUploadRate;
            _tmpUploadRate = _cursor.getDouble(_cursorIndexOfUploadRate);
            final double _tmpDownloadRate;
            _tmpDownloadRate = _cursor.getDouble(_cursorIndexOfDownloadRate);
            final double _tmpNetworkTime;
            _tmpNetworkTime = _cursor.getDouble(_cursorIndexOfNetworkTime);
            final double _tmpExecTime;
            _tmpExecTime = _cursor.getDouble(_cursorIndexOfExecTime);
            final double _tmpTime;
            _tmpTime = _cursor.getDouble(_cursorIndexOfTime);
            _item = new LogLine(_tmpAppName,_tmpTaskName,_tmpExecLocation,_tmpEdgeIP,_tmpRtt,_tmpUploadRate,_tmpDownloadRate,_tmpNetworkTime,_tmpExecTime,_tmpTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            _item.setDate(_tmpDate);
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

  @Override
  public LiveData<List<LogLine>> findApp(final String appName) {
    final String _sql = "SELECT * FROM loggers where appName LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (appName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, appName);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"loggers"}, new Callable<List<LogLine>>() {
      @Override
      public List<LogLine> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
          final int _cursorIndexOfExecLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "execLocation");
          final int _cursorIndexOfEdgeIP = CursorUtil.getColumnIndexOrThrow(_cursor, "edgeIP");
          final int _cursorIndexOfRtt = CursorUtil.getColumnIndexOrThrow(_cursor, "rtt");
          final int _cursorIndexOfUploadRate = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadRate");
          final int _cursorIndexOfDownloadRate = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadRate");
          final int _cursorIndexOfNetworkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "networkTime");
          final int _cursorIndexOfExecTime = CursorUtil.getColumnIndexOrThrow(_cursor, "execTime");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final List<LogLine> _result = new ArrayList<LogLine>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final LogLine _item;
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final String _tmpTaskName;
            _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
            final String _tmpExecLocation;
            _tmpExecLocation = _cursor.getString(_cursorIndexOfExecLocation);
            final String _tmpEdgeIP;
            _tmpEdgeIP = _cursor.getString(_cursorIndexOfEdgeIP);
            final double _tmpRtt;
            _tmpRtt = _cursor.getDouble(_cursorIndexOfRtt);
            final double _tmpUploadRate;
            _tmpUploadRate = _cursor.getDouble(_cursorIndexOfUploadRate);
            final double _tmpDownloadRate;
            _tmpDownloadRate = _cursor.getDouble(_cursorIndexOfDownloadRate);
            final double _tmpNetworkTime;
            _tmpNetworkTime = _cursor.getDouble(_cursorIndexOfNetworkTime);
            final double _tmpExecTime;
            _tmpExecTime = _cursor.getDouble(_cursorIndexOfExecTime);
            final double _tmpTime;
            _tmpTime = _cursor.getDouble(_cursorIndexOfTime);
            _item = new LogLine(_tmpAppName,_tmpTaskName,_tmpExecLocation,_tmpEdgeIP,_tmpRtt,_tmpUploadRate,_tmpDownloadRate,_tmpNetworkTime,_tmpExecTime,_tmpTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            _item.setDate(_tmpDate);
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

  @Override
  public LiveData<List<LogLine>> findExecPlace(final String execPlace) {
    final String _sql = "SELECT * FROM loggers where execLocation LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (execPlace == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, execPlace);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"loggers"}, new Callable<List<LogLine>>() {
      @Override
      public List<LogLine> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
          final int _cursorIndexOfExecLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "execLocation");
          final int _cursorIndexOfEdgeIP = CursorUtil.getColumnIndexOrThrow(_cursor, "edgeIP");
          final int _cursorIndexOfRtt = CursorUtil.getColumnIndexOrThrow(_cursor, "rtt");
          final int _cursorIndexOfUploadRate = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadRate");
          final int _cursorIndexOfDownloadRate = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadRate");
          final int _cursorIndexOfNetworkTime = CursorUtil.getColumnIndexOrThrow(_cursor, "networkTime");
          final int _cursorIndexOfExecTime = CursorUtil.getColumnIndexOrThrow(_cursor, "execTime");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final List<LogLine> _result = new ArrayList<LogLine>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final LogLine _item;
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final String _tmpTaskName;
            _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
            final String _tmpExecLocation;
            _tmpExecLocation = _cursor.getString(_cursorIndexOfExecLocation);
            final String _tmpEdgeIP;
            _tmpEdgeIP = _cursor.getString(_cursorIndexOfEdgeIP);
            final double _tmpRtt;
            _tmpRtt = _cursor.getDouble(_cursorIndexOfRtt);
            final double _tmpUploadRate;
            _tmpUploadRate = _cursor.getDouble(_cursorIndexOfUploadRate);
            final double _tmpDownloadRate;
            _tmpDownloadRate = _cursor.getDouble(_cursorIndexOfDownloadRate);
            final double _tmpNetworkTime;
            _tmpNetworkTime = _cursor.getDouble(_cursorIndexOfNetworkTime);
            final double _tmpExecTime;
            _tmpExecTime = _cursor.getDouble(_cursorIndexOfExecTime);
            final double _tmpTime;
            _tmpTime = _cursor.getDouble(_cursorIndexOfTime);
            _item = new LogLine(_tmpAppName,_tmpTaskName,_tmpExecLocation,_tmpEdgeIP,_tmpRtt,_tmpUploadRate,_tmpDownloadRate,_tmpNetworkTime,_tmpExecTime,_tmpTime);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            _item.setDate(_tmpDate);
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
}
