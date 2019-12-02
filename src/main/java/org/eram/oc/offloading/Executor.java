package org.eram.oc.offloading;

import android.util.Log;

import org.eram.core.app.Task;
import org.eram.oc.Execution;
import org.eram.oc.offloading.scheduler.TaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executor {

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static Future<Object> execute(TaskExecutor taskExecutor){

        return Executor.executor.submit(taskExecutor);
    }
}
