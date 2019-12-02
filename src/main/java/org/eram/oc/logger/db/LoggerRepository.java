package org.eram.oc.logger.db;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LoggerRepository {

    private LogLineDao dao;
    private LiveData<List<LogLine>> data;

    public LoggerRepository(Application application) {

        LoggerRoom db = LoggerRoom.getDatabase(application);
        dao = db.dao();
        data = dao.getALl();
    }

    public LiveData<List<LogLine>> getData() {
        return data;
    }

    public void insert (LogLine log) {

        dao.insertAll(log);
        /*
        try {
             new insertAsyncTask(dao).execute(log);
        }catch (Exception e){
            Log.e(this.toString(), e.toString());
        }*/
    }

    public LiveData<List<LogLine>> findApp(String appName){
        try {
            return (new findAsyncTask(dao).execute(appName).get());
        }catch (Exception e){
            Log.e("POLOOL", e.toString());
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<LogLine, Void, Void> {

        private LogLineDao iDao;

        insertAsyncTask(LogLineDao dao) {
            iDao = dao;
        }

        @Override
        protected Void doInBackground(final LogLine ... params) {
            iDao.insertAll(params);
            return null;
        }
    }

    private static class findAsyncTask extends AsyncTask<String, Void, LiveData<List<LogLine>>> {

        private LogLineDao iDao;

        findAsyncTask(LogLineDao dao) {
            iDao = dao;
        }

        @Override
        protected LiveData<List<LogLine>> doInBackground(final String ... params) {
            Log.e("DB", params[0]);
            return iDao.findApp(params[0]);
        }
    }
}
