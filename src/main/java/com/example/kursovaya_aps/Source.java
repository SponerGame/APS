package com.example.kursovaya_aps;

import java.util.ArrayList;
import java.util.Scanner;

public class Source{
    int status;
    double generate_time;
    int source_id;

    int currentTaskID;

    int tasksRemain;

    public int getCurrentTaskID() {
        return currentTaskID;
    }

    public void setCurrentTaskID(int currentTaskID) {
        this.currentTaskID = currentTaskID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getGenerate_time() {
        return generate_time;
    }

    public void setGenerate_time(double generate_time) {
        this.generate_time = generate_time;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    Task task;



    Source(double generate_time, int source_id) {
        status=1;
        this.generate_time=generate_time;
        this.task = new Task(1,0,source_id);
        this.source_id=source_id;
        this.currentTaskID=task.taskID;
        //Main.tasksNum--;
        //Statistics.tasksNum++;
        Statistics.allTasks.add(this.task);
        this.tasksRemain=Main.tasksNum;
        this.tasksRemain--;
        // Создаём новый поток
        //super("Второй поток");
        //System.out.println("Thread1");
        //start(); // Запускаем поток
    }

    public Source() {

    }

    void start_next(){

    }

    void nextTask(){
        if((generate_time=Main.tasks_gen_times.pollFirst())==0){
            System.out.println(Main.current_time);
        }
        //generate_time=Main.tasks_gen_times.pollFirst();
        int taskID=this.task.taskID;
        this.task=new Task(taskID+1,0,source_id);
        this.currentTaskID=task.taskID;
        this.tasksRemain--;
        //Statistics.tasksNum++;
        Statistics.allTasks.add(this.task);
    }

    public void run() {
        long a_;
        long b_;
        a_ = System.currentTimeMillis();
        while (true){
            b_ = System.currentTimeMillis();
            System.out.println(b_-a_);
            /*try {
                sleep(1000);
            } catch (InterruptedException e) {
                return;
            }*/
        }
    }
}
