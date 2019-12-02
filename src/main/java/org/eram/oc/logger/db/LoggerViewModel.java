package org.eram.oc.logger.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LoggerViewModel extends AndroidViewModel {

    private LoggerRepository loggerRepository;

    private LiveData<List<LogLine>> logs;

    public LoggerViewModel (Application application) {
        super(application);
        loggerRepository = new LoggerRepository(application);
        logs = loggerRepository.getData();
    }


    public void insert(LogLine log) { loggerRepository.insert(log); }

    public LiveData<List<LogLine>> getLogs() {
        return logs;
    }

    public LiveData<List<LogLine>> findApp(String appName)
    {
        return loggerRepository.findApp(appName);
    }
}
