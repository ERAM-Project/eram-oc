package org.eram.oc.profiler;

import android.util.Log;

import org.eram.common.monitor.app.AppProfiler;
import org.eram.common.monitor.net.NetworkProfiler;
import org.eram.common.monitor.sys.SystemProfiler;
import org.eram.common.settings.ConnectionSettings;

import java.io.FileWriter;

public class Profilers {
    private static final String TAG = "Profilers";
    private AppProfiler appProfiler;
    private NetworkProfiler netProfiler;
    private SystemProfiler sysProfiler;

    private static FileWriter logFileWriter;
    private ConnectionSettings.ExecutionLocation execLocation;



    public Profilers(AppProfiler appProfiler, NetworkProfiler netProfiler,
                    SystemProfiler sysProfiler) {
        this.appProfiler = appProfiler;
        this.netProfiler = netProfiler;
        this.sysProfiler = sysProfiler;
    }


    public void startTracking() {

        try {
            this.appProfiler.startProfiler();
            this.sysProfiler.startProfiler();
            this.netProfiler.startProfiler();

            Log.d(TAG, execLocation + " " + appProfiler.getAppName());
        }catch (NullPointerException e){
            Log.e(TAG, "Plaise Create profiler Instances");
        }
    }

    public void stopProfilers() {
        try {
            this.appProfiler.stopProfiler();
            this.sysProfiler.stopProfiler();
            this.netProfiler.stopProfiler();
        }catch (NullPointerException e){
            Log.e(TAG, "Plaise Create profiler Instances");
        }
    }


    /**
     * Stop running profilers and log current information
     */

    public void stopAndLogExecutionInfoTracking(long prepareDataDuration, Long pureExecTime) {
        /*
        stopProfilers();

        lastLogRecord = new LogRecord(progProfiler, netProfiler, devProfiler);


        lastLogRecord.prepareDataDuration = prepareDataDuration;
        lastLogRecord.pureDuration = pureExecTime;
        lastLogRecord.execLocation = mLocation;

        if (mRegime == REGIME_CLIENT) {
            //Phone phone = PhoneFactory.getPhone(devProfiler, netProfiler, progProfiler);
            //phone.estimateEnergyConsumption();
            lastLogRecord.energyConsumption = 0;
            lastLogRecord.cpuEnergy = 0;
            lastLogRecord.screenEnergy = 0;
            lastLogRecord.wifiEnergy = 0;
            lastLogRecord.threeGEnergy = 0;

            Log.i(TAG, "Log record - " + lastLogRecord.toString());

            try {
                synchronized (this) {
                    if (logFileWriter == null) {
                        File logFile = new File(ERADConstants.LOG_FILE_NAME +
                                lastLogRecord.appName + ".csv");
                        // Try creating new, if doesn't exist
                        boolean logFileCreated = logFile.createNewFile();
                        logFileWriter = new FileWriter(logFile, true);
                        if (logFileCreated) {
                            logFileWriter.append(LogRecord.LOG_HEADERS + "\n");
                        }
                    }

                    logFileWriter.append(lastLogRecord.toString()).append("\n");
                    logFileWriter.flush();
                }
            } catch (IOException e) {
                Log.w(TAG, "Not able to create the logFile " +
                        ERADConstants.LOG_FILE_NAME + lastLogRecord.appName + ".csv" + ": " + e);
            }

            updateDbCache();
        }

       */
    }

    private void updateDbCache() {

        /*

        DBCache dbCache = DBCache.getDbCache(lastLogRecord.appName);
        // public DBEntry(String appName, String methodName, String execLocation, String networkType,
        // String networkSubType, int ulRate, int dlRate, long execDuration, long execEnergy)
        DBEntry dbEntry =
                new DBEntry(lastLogRecord.appName, lastLogRecord.methodName, lastLogRecord.execLocation,
                        lastLogRecord.networkType, lastLogRecord.networkSubtype, lastLogRecord.ulRate,
                        lastLogRecord.dlRate, lastLogRecord.execDuration, lastLogRecord.energyConsumption);
        dbCache.insertEntry(dbEntry);
       */
    }
}
