package com.example.kursovaya_aps;

public class Event {

    public enum Event_type{
        newTask,
        setDevice, //from buffer
        freeDevice
    }

    Event_type event_type;

    double event_time;

    int idSource;
    Task task;

    Event(Event_type type,int idSource,Task task){
        this.event_type=type;
        this.idSource=idSource;
        this.task=task;
    }
    Event(Event_type type){
        this.event_type=type;
        /*this.idSource=idSource;
        this.task=task;*/
    }

}
