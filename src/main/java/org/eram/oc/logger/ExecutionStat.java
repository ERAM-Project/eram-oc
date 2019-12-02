package org.eram.oc.logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExecutionStat {

    private String appName;
    private Map<String, Double> times;
    private double totalTime = 0.0;
    public ExecutionStat(String appName){

        this.appName = appName;
        this.times = new LinkedHashMap<>();
    }

    public void addTime(String taskName, double time){
        this.times.put(taskName, time);

        totalTime = 0.0;
        for(String str: this.times.keySet()){
            totalTime += this.times.get(str);
        }
    }

    public double getTotalTime(){
        return this.totalTime;
    }

}
