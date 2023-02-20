package com.example.kursovaya_aps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Device{

    static final ArrayList<Device> devices = new ArrayList<>();
    int id;
    int status; //1-free //0-working
    Task task=null;
    double event_time=-1;
    double work_time;

    double all_work_time;

    String taskINFO;

    Device(int id,double work_time) {
        this.status=1;
        this.id=id;
        this.work_time=work_time;
        //start(); // Запускаем поток
    }

    public static boolean isAllFree(){
        Iterator iterator = devices.iterator();
        while(iterator.hasNext()) {
            Device device = (Device) iterator.next();
            if (device.status == 0) return false;
        }
        return true;
    }

    public boolean isFree(){
        if(status==1){
            return true;
        }
        return false;
    }

    public void takeJob(Task task,double nextEventTime){
        status=0;
        this.task=task;
        event_time=nextEventTime;
        event_time+=work_time;
        taskINFO=task.taskINFO;
        this.task.deviceTime=work_time;
        this.task.status=1;
        this.task.taskDeviceID=this.id;
        this.all_work_time+=work_time;
    }

    public void setFree(){
        status=1;
        this.task=null;
        event_time=-1;
        taskINFO="Свободно";
        this.work_time=(-1/Main.lambda)*Math.log(Math.random());
    }

    public void run() {

        /*long a_;
        long b_;
        a_ = System.currentTimeMillis();
        while (true){
            b_ = System.currentTimeMillis();
            System.out.println(b_-a_);

        }*/
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getTaskINFO() {
        return taskINFO;
    }

    public void setTaskINFO(String taskINFO) {
        this.taskINFO = taskINFO;
    }

    public double getEvent_time() {
        return event_time;
    }

    public void setEvent_time(double event_time) {
        this.event_time = event_time;
    }

    public double getWork_time() {
        return work_time;
    }

    public void setWork_time(double work_time) {
        this.work_time = work_time;
    }
}
