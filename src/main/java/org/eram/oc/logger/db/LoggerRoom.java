package org.eram.oc.logger.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {LogLine.class}, version = 1)
public abstract class LoggerRoom extends RoomDatabase {

    public abstract LogLineDao dao();
    private static volatile  LoggerRoom instance;

    static LoggerRoom getDatabase(final Context context) {
        if (instance == null) {
            synchronized (LoggerRoom.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            LoggerRoom.class, "eram_logger_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(loggerRoomDatabase)
                            .build();
                }
            }
        }
        return instance;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback loggerRoomDatabase= new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(instance).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final LogLineDao lDao;

        PopulateDbAsync(LoggerRoom db) {
            lDao = db.dao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            //lDao.deleteAll();
            return null;
        }
    }

}
