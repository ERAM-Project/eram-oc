package org.eram.oc.offloading;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.SparseArray;

import org.eram.common.Clone;
import org.eram.core.app.Application;
import org.eram.oc.Execution;
import org.eram.oc.communication.ClearConnection;
import org.eram.oc.communication.Connection;
import org.eram.oc.communication.discovery.RemoteServerDiscovery;
import org.eram.oc.logger.Logger;
import org.eram.oc.offloading.decision.ERAMode;
import org.eram.oc.offloading.decision.ODE;
import org.eram.oc.offloading.scheduler.ERAMProtocol;
import org.eram.oc.offloading.scheduler.Protocol;
import org.eram.oc.offloading.scheduler.TaskExecutor;
import org.eram.oc.profiler.Profilers;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TasksRunner implements Runnable, RemoteServerDiscovery.DiscoveryCallBack {

    private  String TAG="";
    private Set<Connection> connections;
    private Set<Clone> clones;
    private List<Boolean> status;

    private Protocol protocol;
    private ODE ode;
    private RemoteServerDiscovery serverDiscovery;

    private BlockingDeque<Execution> apps;
    private SparseArray<BlockingDeque<Object>> appResults;
    private CountDownLatch waitForScheduler;

    private boolean isDFEActive;
    private Context context;
    private PackageManager pManager;

    private String appName;
    private Logger logger;
    private Profilers profiler;


    private ScheduledThreadPoolExecutor remoteServerConnector = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(1);
    // Every two minutes check if we need to reconnect to the remote server
    private static final int FREQUENCY_MEC_CONNECTION = 2 * 60 * 1000;


    public TasksRunner(int id, Set<Clone> clones, CountDownLatch waitForScheduler, Context context,
                       PackageManager pManager, String appName, Logger logger, Profilers profiler,
                       BlockingDeque<Execution> apps, SparseArray<BlockingDeque<Object>> appResults) {

        this.TAG = "eRAM-task-Runner-"+id;

        this.waitForScheduler = waitForScheduler;

        this.context = context;
        this.pManager = pManager;

        this.appName = appName;
        this.logger = logger;
        this.profiler = profiler;

        this.serverDiscovery = new RemoteServerDiscovery(this);
        this.isDFEActive = true;

        this.apps = apps;
        this.appResults = appResults;

        this.clones = clones;
        this.connections = new LinkedHashSet<>();
        this.status = new LinkedList<>();

        for(int i=0; i< clones.size();i++){
            this.status.add(false);
        }

    }

    @Override
    public void run() {

        this.registerToRemoteServer();
        remoteServerConnector.scheduleWithFixedDelay( this.serverDiscovery, FREQUENCY_MEC_CONNECTION,
                FREQUENCY_MEC_CONNECTION, TimeUnit.MILLISECONDS);

        waitForScheduler.countDown();

        while (true) {
            try {
                Log.v(TAG, "Waiting for application...");

                Execution execution = this.apps.take();

                Log.v(TAG, "Got a, app, executing...");

                Object result = runApp(execution.app);
                Log.v(TAG, "App finished execution, putting result on the resultMap...");

                this.appResults.get(execution.iD).put(result != null ? result : new Object());

                Log.v(TAG, "Result inserted on the resultMap.");

            } catch (InterruptedException e) {

                Log.e(TAG,"Interruption "+e);
                /*
                if (!isDFEActive) {
                    Log.v(TAG, "The ERAD is destroyed, exiting");
                    closeConnection();
                    remoteServerConnector.shutdownNow();
                    break;
                } else {
                    Thread.currentThread().interrupt();
                }
                 */
            } catch (Exception e) {
                Log.e(TAG, "Exception on TaskRunner: " + e);
                e.printStackTrace();
            }
        }

    }

    @Override
    public void registerToRemoteServer() {

        Log.d(TAG, "Registering with the Remote Servers...");

        if (this.clones.size() == 0) {
            Log.v(TAG, "There is no remote server, aborting Remote Server registration.");
            return;
        }

        if (areAllConnected()) {
            Log.v(TAG, "The device is already connected to all available remote server, no need for reconnection.");
            return;
        }

        establishConnection();

        Log.i(TAG, "Send the source code to the available edge servers.");

        sendSourceCode();

        sendUpdateConnectionInfo();

    }

    /**
     * Send the source code of the application to the Remote Server.
     */
    private void sendSourceCode()
    {
        try {
            String apkName = pManager.getApplicationInfo(appName, 0).sourceDir;
            int i = 0;
            for(Connection connection: this.connections) {
                if(this.status.get(i))
                    this.protocol.sendApk(apkName, appName, connection.getInput(), connection.getOutput());

                i++;
            }
        }catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Application not found: " + e.getMessage());
        }
    }

    /**
     * Set up streams for the socket connection, perform initial communication with the clone.
     */
    private void establishConnection() {

        try {

            for(Clone clone:this.clones){
                ClearConnection connection = new ClearConnection();
                connection.connect(clone.getIp(), clone.getOffloadingPort(), 5 * 1000);
                this.connections.add(connection);
            }

            this.protocol = new ERAMProtocol();
            this.ode = new ERAMode();

        } catch (Exception e) {
            Log.e(TAG,"Connection setup with the Remote Server failed - " + e);
        }
    }

    /**
     * Send update on the connection status to the client using the framework.
     */
    private void sendUpdateConnectionInfo() {

    }

    private Object runApp(Application app)
    {
        try {
            TaskScheduler scheduler = new TaskScheduler(this.appName, protocol, app, logger, profiler);

            return scheduler.execute(this.getOffloadingDecision(app));
        }catch (Exception e){
            Log.e(TAG, "Exception: "+e.getMessage());
            return  null;
        }
    }


    private Map<String, Connection> getOffloadingDecision(Application app) {

        Log.v(TAG, "Finding the offloading decisions of tasks of the application, online: " +
                this.status);

        Map<String, Connection> decisions = new LinkedHashMap<>();
        Map<String, Clone>  cDecisions = this.ode.decide(app, this.clones);

        for(String str: cDecisions.keySet()){

            decisions.put(str, this.mach(cDecisions.get(str)));
        }

        return decisions;
    }

    private void closeConnection() {

        int i = 0;

        for(Connection connection:this.connections) {
           boolean bool = connection.close();
           this.status.remove(i);
           this.status.add(i, bool);

           sendUpdateConnectionInfo();
           i++;
        }
    }


    private boolean areAllConnected(){
        boolean isOk = true;

        for(boolean b: this.status)
            isOk= isOk&&b;

        return isOk;
    }

    private Connection mach(Clone clone){

        if(clone != null) {

            for (Connection connection : this.connections) {
                if (clone.getIp().equals(connection.getIp()) && connection.getStatus()) {
                    return connection;
                }
            }
        }
        return null;
    }

}
