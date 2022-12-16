package com.example.kursovaya_aps;

public class Task {
    int taskID;
    int taskParentID;
    int status; //1-ready to go //0-finished

    String taskINFO;



    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getTaskParentID() {
        return taskParentID;
    }

    public void setTaskParentID(int taskParentID) {
        this.taskParentID = taskParentID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskINFO() {
        return taskINFO;
    }

    public void setTaskINFO(String taskINFO) {
        this.taskINFO = taskINFO;
    }

    Task(int taskID, int status, int taskParentID){
        this.taskID=taskID;
        this.status=status;
        this.taskParentID=taskParentID;
        this.taskINFO="#"+taskParentID+" "+taskID;
    }
}

