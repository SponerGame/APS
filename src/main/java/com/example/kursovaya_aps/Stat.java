package com.example.kursovaya_aps;

public class Stat {
    //int sourceID;
    int taskNum = 1;
    int taskCanceledNum;
    double allTime;
    double buffTime;
    double deviceTime;


    Stat(double allTime,double buffTime, double deviceTime){
        this.allTime=allTime;
        this.buffTime=buffTime;
        this.deviceTime=deviceTime;
    }
    void increaseTaskNum(){
        this.taskNum++;
    };
    void increaseCanceledTaskNum(){
        this.taskCanceledNum++;
    };
}
