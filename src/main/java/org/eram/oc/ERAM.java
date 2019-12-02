/**
 * Copyright (c) 2019 Houssemeddine
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * https://opensource.org/licenses/MIT
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 **/

package org.eram.oc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.SparseArray;

import androidx.appcompat.app.AppCompatActivity;

import org.eram.common.Clone;
import org.eram.core.app.Application;
import org.eram.oc.communication.ERAMNetworkService;
import org.eram.oc.communication.NetworkHandler;
import org.eram.oc.logger.ExecutionStat;
import org.eram.oc.logger.Logger;
import org.eram.oc.logger.db.LogLine;
import org.eram.oc.offloading.TasksRunner;
import org.eram.oc.profiler.Profilers;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <br> ERAM </br> class define the entry point to the framework. It ensures to the developer to use
 * the <i> eRAM</i> Offloading framework.
 * @version 1.0
 * @author Houssemeddine MAZOUZI
 **/

public class ERAM {

    private static final String TAG = "eRAM";

    /**
     *  Singleton instance.
     */
    private static volatile ERAM instance = null;

    private String appName;
    private Context context;
    private PackageManager packageManager;

    private  static Set<Clone> clones ;

    private static List<TasksRunner> appRunners;
    private static AtomicInteger appID = new AtomicInteger();

    private static BlockingDeque<Execution> apps = new LinkedBlockingDeque<>();
    private static SparseArray<BlockingDeque<Object>> appResults = new SparseArray<>();

    private ProgressDialog pd = null;

    private Logger logger; // The logger.
    private Profilers profiler;


    public ERAM(String appName, Context context, PackageManager packageManager, AppCompatActivity app,
                Clone ... clones) {

        Log.d(TAG, "eRAM- instance created");

        this.appName = appName;
        this.context = context;
        this.packageManager = packageManager;
        this.logger = new Logger(app);
        ERAM.appRunners = new LinkedList<>();
        ERAM.clones = new LinkedHashSet<>();

        profiler = new Profilers(null, null, null);
        for(Clone e:clones){
            ERAM.clones.add(e);
        }

        this.start();
    }

    /**
     * Start to <i>eRAM</i> service.
     */
    private void start() {


        if (clones.size() == 0) {
            starteRAM();
        }

        this.pd = ProgressDialog.show(context, "Working...", "Initial network services...", true, false);
        (new NetworkHandler(appRunners, apps, appResults, appName, context, packageManager, logger, pd, profiler)).execute(clones);

    }

    private void starteRAM() {
        Log.i(TAG, "Starting the ERAM listening service...");
        Intent eramServiceIntent = new Intent(context, ERAMNetworkService.class);
        context.startService(eramServiceIntent);

    }

    public static ERAM getInstance(String appName, Context context, PackageManager packageManager, AppCompatActivity app,
                                   Clone ... clones){

        ERAM result = instance;

        synchronized (ERAM.class) { // As eRAM class in developed on Singleton, it must create only
            // one instance.
            if (result == null) {
                result = instance;
                if (result == null) {
                    instance = result = new ERAM(appName, context, packageManager, app, clones);
                }
            }
        }

        return result;
    }

    public Object execute(Application app){
        Object result = null;

        try {
            int id = appID.incrementAndGet();
            appResults.put(id, new LinkedBlockingDeque<Object>());
            Log.v(TAG, "Adding task on the tasks blocking queue...");
            Execution execution = new Execution(app, id);
            apps.put(execution);

            Log.v(TAG, "Task added .. ");

            Log.v(TAG, "Waiting for the result of this task to be inserted in the queue by the working thread...");

            result = appResults.get(id).take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }

    /**
     * Get the context of the application.
     * @return  the context of the application.
     **/
    public Context getContext() {
        return context;
    }


    public double getTime(String appName){

        ExecutionStat  stats = logger.lasltExecInfos(appName);
        if(stats == null)
            return -1;

        return stats.getTotalTime();
    }
}
