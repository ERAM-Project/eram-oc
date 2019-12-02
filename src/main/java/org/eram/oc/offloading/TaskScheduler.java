package org.eram.oc.offloading;

import android.util.Log;

import org.eram.core.app.Application;
import org.eram.core.app.Task;
import org.eram.oc.communication.Connection;
import org.eram.oc.logger.Logger;
import org.eram.oc.offloading.scheduler.LocalExecutor;
import org.eram.oc.offloading.scheduler.Protocol;
import org.eram.oc.offloading.scheduler.RemoteEexecutor;
import org.eram.oc.offloading.scheduler.TaskExecutor;
import org.eram.oc.profiler.Profilers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public class TaskScheduler<T>{

    private static  final  String TAG ="eRAM-scheduler";
    private String appName;
    private Map<String, T> executionResults;
    private Protocol protocol;
    private Application application;
    private TaskExecutor executor;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Logger logger;
    private Profilers profiler;


    public TaskScheduler(String appName, Protocol protocol, Application application, Logger logger, Profilers profiler) {

        this.appName = appName;
        this.protocol = protocol;
        this.application = application;

        this.logger = logger;
        this.profiler = profiler;

        this.executionResults = new LinkedHashMap<>();
    }

    public T execute(Map<String, Connection> connections) throws Exception{

        List<Set<Task>> notes = application.taskNote();

        for(int i=0; i<notes.size();i++){

            List<T> results = perform(notes.get(i), connections);

            Iterator<T> interator = results.iterator();

            for(Task task: notes.get(i)){
                executionResults.put(task.toString(), interator.next());
            }
        }

        String lastTask ="";
        for(String str: executionResults.keySet())
            lastTask = str;

        return executionResults.get(lastTask);
    }

    private List<T> perform(Set<Task> tasks, Map<String, Connection> connections) throws Exception{

        List<T> results = new LinkedList<>();
        Set<Future> futures = new LinkedHashSet<>();

        Log.e(TAG,tasks.toString());

        for(Task task:tasks) {

            Set<Task> preds = application.predecessors(task);
            ArrayList<T> inputs = new ArrayList<>();

            for(Task pred: preds){
                inputs.add(executionResults.get(pred.toString()));
            }

            if(inputs.size()!=0 && this.application.canChangeInput(task))
                task.setInputs(inputs);


            if(connections.get(task.toString()) == null){

                this.input = null;
                this.output = null;

                this.executor = new LocalExecutor(this.appName, this.profiler, this.logger, task, input, output);
            }
            else{
                this.input = connections.get(task.toString()).getInput();
                this.output = connections.get(task.toString()).getOutput();

                this.executor = new RemoteEexecutor(this.appName, this.profiler, this.logger,task, input, output);

            }

            futures.add(this.protocol.execute(this.executor));
        }


        for(Future future:futures){
            results.add((T) future.get());
        }

        return results;
    }

}
