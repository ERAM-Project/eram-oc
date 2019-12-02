package org.eram.oc.logger.db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class LoggerRoom_Impl extends LoggerRoom {
  private volatile LogLineDao _logLineDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `loggers` (`date` TEXT NOT NULL, `appName` TEXT, `taskName` TEXT, `execLocation` TEXT, `edgeIP` TEXT, `rtt` REAL NOT NULL, `uploadRate` REAL NOT NULL, `downloadRate` REAL NOT NULL, `networkTime` REAL NOT NULL, `execTime` REAL NOT NULL, `time` REAL NOT NULL, PRIMARY KEY(`date`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"664e0af6d1a88340060c6ae572bf1467\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `loggers`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsLoggers = new HashMap<String, TableInfo.Column>(11);
        _columnsLoggers.put("date", new TableInfo.Column("date", "TEXT", true, 1));
        _columnsLoggers.put("appName", new TableInfo.Column("appName", "TEXT", false, 0));
        _columnsLoggers.put("taskName", new TableInfo.Column("taskName", "TEXT", false, 0));
        _columnsLoggers.put("execLocation", new TableInfo.Column("execLocation", "TEXT", false, 0));
        _columnsLoggers.put("edgeIP", new TableInfo.Column("edgeIP", "TEXT", false, 0));
        _columnsLoggers.put("rtt", new TableInfo.Column("rtt", "REAL", true, 0));
        _columnsLoggers.put("uploadRate", new TableInfo.Column("uploadRate", "REAL", true, 0));
        _columnsLoggers.put("downloadRate", new TableInfo.Column("downloadRate", "REAL", true, 0));
        _columnsLoggers.put("networkTime", new TableInfo.Column("networkTime", "REAL", true, 0));
        _columnsLoggers.put("execTime", new TableInfo.Column("execTime", "REAL", true, 0));
        _columnsLoggers.put("time", new TableInfo.Column("time", "REAL", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLoggers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLoggers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLoggers = new TableInfo("loggers", _columnsLoggers, _foreignKeysLoggers, _indicesLoggers);
        final TableInfo _existingLoggers = TableInfo.read(_db, "loggers");
        if (! _infoLoggers.equals(_existingLoggers)) {
          throw new IllegalStateException("Migration didn't properly handle loggers(org.eram.oc.logger.db.LogLine).\n"
                  + " Expected:\n" + _infoLoggers + "\n"
                  + " Found:\n" + _existingLoggers);
        }
      }
    }, "664e0af6d1a88340060c6ae572bf1467", "0dc5b35f7e8a62713c95d686bd4f5661");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "loggers");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `loggers`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public LogLineDao dao() {
    if (_logLineDao != null) {
      return _logLineDao;
    } else {
      synchronized(this) {
        if(_logLineDao == null) {
          _logLineDao = new LogLineDao_Impl(this);
        }
        return _logLineDao;
      }
    }
  }
}
