package com.example.kursovaya_aps;

public class Controller{
    static int ptr_id;
    static public boolean giveJob(Task task,double current_time){
        synchronized (Device.devices) {
            boolean isSet = false;
            ptr_id = ptr_id % Device.devices.size();
            for (int i = 0; i<Device.devices.size(); ++ptr_id) {
                ptr_id = ptr_id % Device.devices.size();
                if (Device.devices.get(ptr_id).isFree()) {
                    Device.devices.get(ptr_id).takeJob(task, current_time);
                    isSet = true;
                    break;
                }
                i++;
            }
            return isSet;
        }
    }

    static public void setDeviceFree(double nextEventTime){
        for(var i : Device.devices){
            if(i.event_time==nextEventTime){
                i.setFree();
            }
        }
    }
}
