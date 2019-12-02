package org.eram.oc.offloading.scheduler;

import org.eram.core.app.Task;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Future;

public interface Protocol {

    void sendApk(String apkName, String mAppName, ObjectInputStream input, ObjectOutputStream output);
    Future<Object> execute(TaskExecutor taskExecutor);
}
