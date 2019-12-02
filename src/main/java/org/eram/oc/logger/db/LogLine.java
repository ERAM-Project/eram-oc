package org.eram.oc.logger.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.List;

@Entity(tableName = "loggers")
public class LogLine {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "appName")
    private String appName;

    @ColumnInfo(name = "taskName")
    private String taskName;

    @ColumnInfo(name = "execLocation")
    private String execLocation;

    @ColumnInfo(name = "edgeIP")
    private String edgeIP;

    @ColumnInfo(name = "rtt")
    private double rtt;

    @ColumnInfo(name = "uploadRate")
    private double uploadRate;

    @ColumnInfo(name = "downloadRate")
    private double downloadRate;

    @ColumnInfo(name = "networkTime")
    private double networkTime;

    @ColumnInfo(name = "execTime")
    private double execTime;

    @ColumnInfo(name = "time")
    private double time;

    public LogLine(String appName, String taskName, String execLocation, String edgeIP, double rtt,
                   double uploadRate, double downloadRate, double networkTime, double execTime,
                   double time) {
        this.date = System.currentTimeMillis()+"";
        this.appName = appName;
        this.taskName = taskName;
        this.execLocation = execLocation;
        this.edgeIP = edgeIP;
        this.rtt = rtt;
        this.uploadRate = uploadRate;
        this.downloadRate = downloadRate;
        this.networkTime = networkTime;
        this.execTime = execTime;
        this.time = time;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getExecLocation() {
        return execLocation;
    }

    public void setExecLocation(String execLocation) {
        this.execLocation = execLocation;
    }

    public String getEdgeIP() {
        return edgeIP;
    }

    public void setEdgeIP(String edgeIP) {
        this.edgeIP = edgeIP;
    }

    public double getRtt() {
        return rtt;
    }

    public void setRtt(double rtt) {
        this.rtt = rtt;
    }

    public double getUploadRate() {
        return uploadRate;
    }

    public void setUploadRate(double uploadRate) {
        this.uploadRate = uploadRate;
    }

    public double getDownloadRate() {
        return downloadRate;
    }

    public void setDownloadRate(double downloadRate) {
        this.downloadRate = downloadRate;
    }

    public double getNetworkTime() {
        return networkTime;
    }

    public void setNetworkTime(double networkTime) {
        this.networkTime = networkTime;
    }

    public double getExecTime() {
        return execTime;
    }

    public void setExecTime(double execTime) {
        this.execTime = execTime;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }


    public List<List<Object>> asList()
    {
        return Arrays.asList(Arrays.asList((Object) getDate(), getAppName(), getAppName(), getTaskName(),
                getExecLocation(), getEdgeIP(), getRtt(), getUploadRate(), getDownloadRate(), getNetworkTime(),
                getExecTime(), getTime()));
    }

    @Override
    public String toString() {
        return "LogLine{" +
                "date='" + getDate() + '\'' +
                ", appName='" + getAppName() + '\'' +
                ", taskName='" + getTaskName()+ '\'' +
                ", execLocation='" + getExecLocation() + '\'' +
                ", edgeIP='" + getEdgeIP() + '\'' +
                ", rtt=" + getRtt() +
                ", uploadRate=" + getUploadRate() +
                ", downloadRate=" + getDownloadRate() +
                ", networkTime=" + getNetworkTime() +
                ", execTime=" + getExecTime() +
                ", time=" + getTime() +
                '}';
    }

    @Override
    public LogLine clone() {
        return new LogLine(this.appName, this.taskName, this.execLocation,
                this.edgeIP, this.rtt, this.uploadRate, this.downloadRate,
                this.networkTime, this.execTime, this.time);
    }
}
