package org.eram.oc.offloading.scheduler;

import org.eram.core.app.Task;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface TaskExecutor<Object> extends Callable<Object> {

     Object execute();//(Task task, ObjectInputStream input, ObjectOutputStream output);
}
