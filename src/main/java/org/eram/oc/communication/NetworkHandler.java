package org.eram.oc.communication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import org.eram.common.Clone;
import org.eram.oc.Execution;
import org.eram.oc.logger.Logger;
import org.eram.oc.offloading.TasksRunner;
import org.eram.oc.profiler.Profilers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkHandler extends AsyncTask<Set<Clone>, String, Void> {

    private static final String TAG = "eRAM-Network-Handler";

    private ExecutorService threadPool;
    private BlockingDeque<Execution> apps;
    private SparseArray<BlockingDeque<Object>> appResults;
    private CountDownLatch waitForScheduler;

    private static final int numberTaskRunners = 1;
    private List<TasksRunner> tasksRunnerList;

    private String appName;
    private Context context;
    private PackageManager pManager;

    private Logger logger;
    private Profilers profiler;

    private ProgressDialog pd = null;


    public NetworkHandler(List<TasksRunner> tasksRunnerList, BlockingDeque<Execution> apps, SparseArray<BlockingDeque<Object>> appResults,
                          String appName, Context context, PackageManager pManager, Logger logger,
                          ProgressDialog pd, Profilers profiler) {

        threadPool = Executors.newFixedThreadPool(numberTaskRunners);
        waitForScheduler = new CountDownLatch(numberTaskRunners);

        this.apps = apps;
        this.appResults = appResults;

        this.tasksRunnerList = tasksRunnerList;

        this.appName = appName;
        this.context = context;
        this.pManager = pManager;
        this.logger = logger;
        this.pd = pd;

        this.profiler = profiler;
    }

    @Override
    protected Void doInBackground(Set<Clone>... sets) {

        if(sets[0] != null){

            publishProgress("Using the edge server given by the user: " + sets[0]);
            // NetworkProfiler1.measureUlRate(eClone.getIp(), eClone.getClonePortBandwidthTest());
            // NetworkProfiler1.measureDlRate(eClone.getIp(), eClone.getClonePortBandwidthTest());
            // NetworkProfiler1.measureRtt(eClone.getIp(), eClone.getClonePortBandwidthTest());


            publishProgress("Starting the TaskRunner threads...");
            // Start TaskRunner threads that will handle the task dispatching process.
            for (int i = 0; i < numberTaskRunners; i++) {
                TasksRunner t = new TasksRunner(i, sets[0], waitForScheduler, context, pManager, appName,
                        logger, profiler, apps, appResults);

                tasksRunnerList.add(t);
                //Log.e(TAG,apps+"");
                threadPool.submit(t);
            }
            try {
                waitForScheduler.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "Started initial network tasks");
    }

    @Override
    protected void onPostExecute(Void aVoid) {


        Log.i(TAG, "Finished initial network tasks");
        if (pd != null) {
            pd.dismiss();
        }

        this.onDestroy();
    }


    @Override
    protected void onProgressUpdate(String... values) {
        Log.i(TAG, values[0]);
        if (pd != null) {
            pd.setMessage(values[0]);
        }
    }

    private void onDestroy()
    {
        threadPool.shutdownNow();
    }
}
