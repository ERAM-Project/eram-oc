package org.eram.oc.offloading.scheduler;

import org.eram.core.app.Task;
import org.eram.oc.logger.Logger;
import org.eram.oc.profiler.Profilers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class ERAMExecutors  implements TaskExecutor  {
    protected final String TAG = "executor";
    protected String appName;
    protected Profilers profiler ;
    protected Logger logger;

    protected Task task;
    protected  ObjectInputStream input;
    protected  ObjectOutputStream output;

    public ERAMExecutors(String appName, Profilers profiler, Logger logger, Task task, ObjectInputStream input, ObjectOutputStream output) {
        this.appName = appName;
        this.profiler = profiler;
        this.logger = logger;

        this.task = task;
        this.input = input;
        this.output = output;
    }

    @Override
    public Object call() {
        return this.execute();
    }
}
