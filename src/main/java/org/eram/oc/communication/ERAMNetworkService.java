package org.eram.oc.communication;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.eram.common.Clone;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ERAMNetworkService extends IntentService {

    private static final String TAG = "ERAM-Network-Service";

    private Clone eClone;
    static final int AC_RM_PORT = 23456;
    static final int AC_GET_VM = 1;
    static final int AC_GET_NETWORK_MEASUREMENTS = 2;
    static final int AC_QOS_PARAMS = 3;

    ScheduledThreadPoolExecutor netScheduledPool =
            (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
    // Every 30 minutes measure rtt, ulRate, and dlRate
    private static final int FREQUENCY_NET_MEASUREMENT = 30 * 60 * 1000;

    ScheduledThreadPoolExecutor registrationScheduledPool =
            (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
    // Every 2 minutes check if we have a VM. If not, try to re-register with the DS and SLAM.
    private static final int FREQUENCY_REGISTRATION = 2 * 60 * 1000;
    private static boolean registeringWithDs = false;
    private static boolean registeringWithSlam = false;

    public static boolean usePrevVm = true;

    // Intent for sending broadcast messages
    public static final String RAPID_VM_CHANGED = "eu.project.rapid.vmChanged";
    public static final String RAPID_VM_IP = "eu.project.rapid.vmIP";
    public static final String RAPID_NETWORK_CHANGED = "eu.project.rapid.networkChanged";
    public static final String RAPID_NETWORK_RTT = "eu.project.rapid.rtt";
    public static final String RAPID_NETWORK_UL_RATE = "eu.project.rapid.ulRate";
    public static final String RAPID_NETWORK_DL_RATE = "eu.project.rapid.dlRate";
    private String jsonQosParams;

    private ExecutorService clientsThreadPool = Executors.newCachedThreadPool();

    public ERAMNetworkService(Clone eClone) {
        super(ERAMNetworkService.class.getName());
        this.eClone = eClone;

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Started the service");

        try {
            ServerSocket acRmServerSocket = new ServerSocket(AC_RM_PORT);
            Log.i(TAG, "************* Started the AC_RM listening server ****************");

           // String qosParams = readQosParams().replace(" ", "").replace("\n", "");

           // jsonQosParams = parseQosParams(qosParams);
           // Log.v(TAG, "QoS params in JSON: " + jsonQosParams);

            // The prev id is useful to the DS so that it can release already allocated VMs.
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            //String vmmIp = prefs.getString(ERAMConstants.PREV_VMM_IP, "");


            // netScheduledPool.scheduleWithFixedDelay(new NetMeasurement(this), 10,
            // FREQUENCY_NET_MEASUREMENT, TimeUnit.MILLISECONDS);

            registrationScheduledPool.scheduleWithFixedDelay(
                    new Runnable() {
                        @Override
                        public void run() {
                            Log.v(TAG, "We do not have a VM, registering with the DS and SLAM...");
                            if (registeringWithDs || registeringWithSlam) {
                                Log.v(TAG, "Registration already in progress...");
                            } else {


                            }
                        }
                    }, FREQUENCY_REGISTRATION, FREQUENCY_REGISTRATION, TimeUnit.MILLISECONDS
            );


            while (true) {
                try {
                    Socket client = acRmServerSocket.accept();
                    clientsThreadPool.execute(new ClientHandler(client));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            Log.w(TAG, "Couldn't start the listening server, " +
                    "maybe another app has already started this service: " + e);
        } finally {
            if (clientsThreadPool != null) {
                clientsThreadPool.shutdown();
                Log.v(TAG, "The clientThreadPool is now shut down.");
            }

            if (netScheduledPool != null) {
                netScheduledPool.shutdown();
                Log.v(TAG, "The netScheduledPool is now shut down.");
            }

            if (registrationScheduledPool != null) {
                registrationScheduledPool.shutdown();
                Log.v(TAG, "The registrationScheduledPool is now shut down.");
            }
        }
    }



    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {

                InputStream is = clientSocket.getInputStream();
                OutputStream os = clientSocket.getOutputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                ObjectOutputStream oos = new ObjectOutputStream(os);

                int command = is.read();
                switch (command) {
                    case AC_GET_VM:
                        oos.writeObject(eClone);
                        oos.flush();
                        break;

                    case AC_GET_NETWORK_MEASUREMENTS:
                       /*
                        oos.writeInt(NetworkProfiler1.lastUlRate.getBw());
                        oos.writeInt(NetworkProfiler1.lastDlRate.getBw());
                        oos.writeInt(NetworkProfiler1.rtt);
                        oos.flush();
                        */
                        break;


                    case AC_QOS_PARAMS:
                        break;

                    default:
                        Log.w(TAG, "Did not recognize command: " + command);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void doMeasurement() {
        /*
        NetworkProfiler1.measureRtt(eClone.getIp(), eClone.getClonePortBandwidthTest());
        NetworkProfiler1.measureUlRate(eClone.getIp(), eClone.getClonePortBandwidthTest());
        NetworkProfiler1.measureDlRate(eClone.getIp(), eClone.getClonePortBandwidthTest());

        Intent intent = new Intent(ERADNetworkService.RAPID_NETWORK_CHANGED);
        intent.putExtra(ERADNetworkService.RAPID_NETWORK_RTT, NetworkProfiler1.rtt);
        intent.putExtra(ERADNetworkService.RAPID_NETWORK_DL_RATE, NetworkProfiler1.lastDlRate.getBw());
        intent.putExtra(ERADNetworkService.RAPID_NETWORK_UL_RATE, NetworkProfiler1.lastUlRate.getBw());
        sendBroadcast(intent);
        */
    }
}
