package org.eram.oc.logger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.eram.oc.Execution;
import org.eram.oc.logger.db.LogLine;
import org.eram.oc.logger.db.LoggerViewModel;
import org.eram.oc.logger.sheets.ERAMSheets;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Logger {

    private LoggerViewModel logger;
    private ERAMSheets sheets;
    /// Add ERAMSheets;

    private Map<String, ExecutionStat> lastExecutions;

    public Logger(AppCompatActivity activity)
    {

        this.logger = ViewModelProviders.of(activity).get(LoggerViewModel.class);
        logger.getLogs().observe(activity, new Observer<List<LogLine>>() {
            @Override
            public void onChanged(List<LogLine> logLines) {
                // To add;
               /// Log.e("CHINO", logLines.size()+"!"+logLines.toString());
            }
        });

        this.sheets = new ERAMSheets(activity);
        this.lastExecutions = new LinkedHashMap<>();
    }

    public void addLogs(LogLine ... logs) {

        for(LogLine log: logs) {
            logger.insert(log);
            sheets.getResultsFromApi(log);
        }
    }

    public List<LogLine> appData(String appName){

        LiveData<List<LogLine>> result = logger.findApp(appName);
        if (result!=null)
            return result.getValue();
        return null;
    }

    public List<LogLine> getAll()
    {
        LiveData<List<LogLine>> result = logger.getLogs();
        if (result!=null)
            return result.getValue();
        return null;
    }


    public void addLastExec(String appName, String taskName, LogLine log)
    {
        if(!this.lastExecutions.containsKey(appName)){
            this.lastExecutions.put(appName, new ExecutionStat(appName));
        }

        this.lastExecutions.get(appName).addTime(taskName, log.getExecTime());
    }

    public ExecutionStat lasltExecInfos(String appName)
    {
        Log.e("LOGGER11", this.lastExecutions.keySet().toString());

        if(!this.lastExecutions.containsKey(appName))
            return null;
        return this.lastExecutions.remove(appName);
    }
}
