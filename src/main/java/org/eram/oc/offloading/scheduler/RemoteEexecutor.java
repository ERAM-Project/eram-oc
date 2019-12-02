package org.eram.oc.offloading.scheduler;

import android.util.Log;

import org.eram.common.Messages;
import org.eram.common.ResultContainer;
import org.eram.core.app.Task;
import org.eram.oc.logger.Logger;
import org.eram.oc.logger.db.LogLine;
import org.eram.oc.profiler.Profilers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Future;

public class RemoteEexecutor extends ERAMExecutors {

    public RemoteEexecutor(String appName,Profilers profiler, Logger logger, Task task, ObjectInputStream input, ObjectOutputStream output) {
        super(appName, profiler, logger, task, input, output);
    }

    @Override
    public Object execute() {
        Object result = null;
        try{

            Long startTime = System.nanoTime();
            output.write(Messages.I_OFFLOAD_A_TASK);
            output.flush();
            result = sendAndExecute(task, input, output);

            Long duration = System.nanoTime() - startTime;
            Log.d(TAG, "REMOTE " + task.toString() + ": Actual Send-Receive duration - "
                    + duration / 1000000 + "ms");

            LogLine log = new LogLine(this.appName, task.toString(), "REMOTE",
                    "127.0.0.1", 0.0, 0.0, 0.0, 0.0,
                    duration/1000000, duration/1000000);

            this.logger.addLogs(log);
            this.logger.addLastExec(this.appName, task.toString(), log);

        } catch (Exception e) {
            // No such host exists, execute locally
            Log.e(TAG,"REMOTE ERROR: " + task.toString() + ": " + e);
            e.printStackTrace();
            // Close the connection;

        }

        return result;
    }



    ///
    private Object sendAndExecute(Task task, ObjectInputStream input, ObjectOutputStream output)
            throws Exception {

        // Send the object itself
        sendObject(task, output);

        // Read the results from the server
        Log.d(TAG, "Read Result");
        Object response = input.readObject();

        ResultContainer container = (ResultContainer) response;

        Log.d(TAG, "Finished remote execution");

        return container.functionResult;
    }


    private void sendObject(Task task, ObjectOutputStream output) throws IOException {

        output.reset();
        Log.d(TAG, "Write Object and data");

        output.writeObject(task);

        output.flush();
    }
}
