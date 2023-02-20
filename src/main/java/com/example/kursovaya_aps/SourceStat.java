package com.example.kursovaya_aps;

public class SourceStat {
    int sourceID;
    int tasksNum;
    double cancelP;
    double avgAllTime;
    double avgTimeBuff;
    double avgTimeDevice;
    double BuffDisp;
    double DeviceDisp;

    public int getSourceID() {
        return sourceID;
    }

    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }

    public int getTasksNum() {
        return tasksNum;
    }

    public void setTasksNum(int tasksNum) {
        this.tasksNum = tasksNum;
    }

    public double getCancelP() {
        return cancelP;
    }

    public void setCancelP(double cancelP) {
        this.cancelP = cancelP;
    }

    public double getAvgAllTime() {
        return avgAllTime;
    }

    public void setAvgAllTime(double avgAllTime) {
        this.avgAllTime = avgAllTime;
    }

    public double getAvgTimeBuff() {
        return avgTimeBuff;
    }

    public void setAvgTimeBuff(double avgTimeBuff) {
        this.avgTimeBuff = avgTimeBuff;
    }

    public double getAvgTimeDevice() {
        return avgTimeDevice;
    }

    public void setAvgTimeDevice(double avgTimeDevice) {
        this.avgTimeDevice = avgTimeDevice;
    }

    public double getBuffDisp() {
        return BuffDisp;
    }

    public void setBuffDisp(double buffDisp) {
        BuffDisp = buffDisp;
    }

    public double getDeviceDisp() {
        return DeviceDisp;
    }

    public void setDeviceDisp(double deviceDisp) {
        DeviceDisp = deviceDisp;
    }
}
