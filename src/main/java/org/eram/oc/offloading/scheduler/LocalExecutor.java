package org.eram.oc.offloading.scheduler;

import android.util.Log;

import org.eram.core.app.Task;
import org.eram.oc.logger.Logger;
import org.eram.oc.logger.db.LogLine;
import org.eram.oc.profiler.Profilers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocalExecutor extends ERAMExecutors {

    public LocalExecutor(String appName, Profilers profiler, Logger logger, Task task, ObjectInputStream input, ObjectOutputStream output) {
        super(appName, profiler, logger, task, input, output);
    }

    @Override
    public Object execute() {
        Object result = null;
        try {

            Long startTime = System.nanoTime();

            result = task.run();

            long mPureLocalDuration = System.nanoTime() - startTime;
            Log.e(TAG, "LOCAL " + task.toString() + ": Actual Invocation duration - "
                    + mPureLocalDuration / 1000000 + "ms");

            LogLine log = new LogLine(this.appName, task.toString(), "LOCAL",
                    "127.0.0.1", 0.0, 0.0, 0.0, 0.0,
                    mPureLocalDuration/1000000, mPureLocalDuration/1000000);

            this.logger.addLogs(log);
            this.logger.addLastExec(this.appName, task.toString(), log);

        } catch (Exception e) {
            Log.w(TAG, "Exception while running the method locally: " + e);
            profiler.stopProfilers();//stopAndDiscardExecutionInfoTracking();
            e.printStackTrace();
        }
        return result;
    }
}
