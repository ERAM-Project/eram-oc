package org.eram.oc.logger.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LogLineDao {

    @Insert
    void insertAll(LogLine ... logs);

    @Delete
    void deleteAll(LogLine ... logs);

    @Query("SELECT * FROM loggers")
    LiveData<List<LogLine>> getALl();

    @Query("DELETE from loggers")
    void reinit();

    @Query("SELECT * FROM loggers where appName LIKE :appName")
    LiveData<List<LogLine>> findApp(String appName);

    @Query("SELECT * FROM loggers where execLocation LIKE :execPlace")
    LiveData<List<LogLine>> findExecPlace(String execPlace);
}
