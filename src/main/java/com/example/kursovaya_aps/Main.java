package com.example.kursovaya_aps;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static com.example.kursovaya_aps.Device.devices;
import static com.example.kursovaya_aps.Main.*;

public class Main {
    static ArrayList<Source> sources = new ArrayList<>();
    static final Random random = new Random();

    static final Object mutex = new Object();

    static ArrayDeque<Double> tasks_gen_times = new ArrayDeque<>();

    static double current_time = 0;

    static int tasksNum = 100;

    static int devicesNum = 5;

    static int sourcesNum = 7;

    static double  lambda = 10;

    static int bufferNum = 15;

    static double left_bord=2;
    static double right_bord=15;

    static double lastTimeEvent=0;

    static double get_current_event_time(){
        double min_time = 100000;
        for (var i : sources){
            if(i.generate_time<min_time && i.status==1){
                min_time=i.generate_time;
            }
        }

        for (var i : devices){
            if(!i.isFree() && i.event_time<min_time){
                min_time=i.event_time;
            }
        }

        return min_time;
    }

    public static ArrayList<Event> get_current_time_Events(double current_time){
        ArrayList<Event> current_time_events = new ArrayList<>();
        for (var i : sources){
            if(i.generate_time==current_time){
                current_time_events.add(new Event(Event.Event_type.newTask,i.source_id,i.task));
            }
        }
        for (var i : devices){
            if(i.isFree() && !Buffer.isEmpty()){
                current_time_events.add(new Event(Event.Event_type.setDevice)); //from buffer
            }
        }
        for (var i : devices){
            if(!i.isFree() && i.event_time==current_time){
                current_time_events.add(new Event(Event.Event_type.freeDevice));
            }
        }

        return current_time_events;
    }

    //@Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    static boolean isAllTasksWorked(){
        boolean check = true;
        Iterator iterator = sources.iterator();
        while(iterator.hasNext()){
            Source source = (Source) iterator.next();
            if (source.tasksRemain>0) {
                return false;
                //check=false;
            }
            if (source.tasksRemain==0) {
                System.out.println(source.source_id);

            }
        }

        return check;
    }

    public static void main(String[] args) {

        double a = left_bord;
        double b = right_bord;
        /*ArrayList<Double> list = new ArrayList<Double>();
        for (int i = 0; i < 20; i++) {
            list.add(a + (b - a) * (random.nextInt(100) % 100) / 100);
        }*/


        //double current_time = 0;
        ArrayList<Double> tasks_gen_time = new ArrayList<>();
        for (int i = 0; i < tasksNum*sourcesNum; i++) {
            tasks_gen_time.add((a+(b-a)*(random.nextInt(Integer.MAX_VALUE) %100)/100));
        }
        Collections.sort(tasks_gen_time);
        //ArrayDeque<Double> tasks_gen_times = new ArrayDeque<>();
        tasks_gen_times.addAll(tasks_gen_time);
        for (int i = 0; i < sourcesNum; i++) {
            sources.add(new Source(tasks_gen_times.pollFirst(), i));
        }
        for (int i = 0; i < devicesNum; i++) {
            devices.add(new Device(i, (-1/lambda)*Math.log(Math.random())));
        }

        Buffer.current_size = 0;
        Buffer.max_size = bufferNum;



        Thread t1 = new Thread() {
            public void run(){

                current_time = 0;
                boolean isFirstIteration = true;
                System.out.println(isAllTasksWorked());
                while (!Device.isAllFree() || !isAllTasksWorked()) {
                    System.out.println(isAllTasksWorked());
                    isFirstIteration=false;
                    synchronized (mutex){
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(current_time);
                        double nextEventTime = get_current_event_time();
                        ArrayList<Event> current_time_events = get_current_time_Events(nextEventTime);

                        for (var i : current_time_events) {
                            switch (i.event_type) {
                                case newTask:
                                    Task new_task = i.task;
                                    if(!Controller.giveJob(new_task,nextEventTime)){
                                        //current_time_events.add(new Event(Event.Event_type.setDevice)); //from buffer ???????? что-то сделать с циклом ивентов
                                        Buffer.add(i.task,nextEventTime);
                                    }
                                    Iterator iterator = sources.iterator();
                                    while(iterator.hasNext()){
                                        Source source = (Source) iterator.next();
                                        if (source.task==new_task && source.tasksRemain>0) {
                                            source.nextTask();
                                        } else if (source.task==new_task && source.tasksRemain==0) {
                                            source.setStatus(0);
                                        }
                                    }
                                    break;
                                case setDevice: //from buffer
                                    Task buffer_task = Buffer.buffer.getLast();
                                    if(Controller.giveJob(buffer_task,nextEventTime)) Buffer.removeLast(nextEventTime);
                                    break;
                                case freeDevice:
                                    Controller.setDeviceFree(nextEventTime);
                                    if(get_current_event_time()!=nextEventTime && !Buffer.isEmpty()){
                                        Task buffer_task_ = Buffer.buffer.getLast();
                                        if(Controller.giveJob(buffer_task_,nextEventTime)) Buffer.removeLast(nextEventTime);
                                    }
                                    break;
                            }
                        }
                        current_time = nextEventTime;
                    }
                }
                /*Iterator iterator = Statistics.allTasks.iterator();
                while(iterator.hasNext()){
                    Task task = (Task) iterator.next();
                    if (task.status==0) {
                        iterator.remove();
                    }
                }

                for (var i : Statistics.allTasks){
                    if(i.status==2)Statistics.cancelsNum++;
                    if(i.status==1)Statistics.tasksNum++;
                    i.buffTime=i.buffOutTime-i.buffInTime;
                    if(Statistics.ParentStatistic.putIfAbsent(i.taskParentID,new Stat(i.buffTime+i.deviceTime,i.buffTime,i.deviceTime))!=null){
                        Stat temp_stat = Statistics.ParentStatistic.get(i.taskParentID);
                        temp_stat.deviceTime+=i.deviceTime;
                        temp_stat.buffTime+=i.buffTime;
                        temp_stat.allTime+=i.buffTime+i.deviceTime;
                        if(i.status==1 || i.status==2) temp_stat.increaseTaskNum();
                        if(i.status==2) temp_stat.increaseCanceledTaskNum();
                        Statistics.ParentStatistic.put(i.taskParentID,temp_stat);
                    }
                }*/

                //Statistics.allTasks.re;
                //System.out.println(Statistics.cancelsNum);


            }
        };

        t1.start();
        //t2.start();



        Interface.InterfaceLaunch(new String[]{});

        t1.interrupt();
    }



    public static void auto(){
        current_time = 0;
        boolean isFirstIteration = true;
        while (!Device.isAllFree() || !isAllTasksWorked()) {
            isFirstIteration=false;


            //System.out.println(current_time);
            double nextEventTime = get_current_event_time();
            ArrayList<Event> current_time_events = get_current_time_Events(nextEventTime);

            for (var i : current_time_events) {
                switch (i.event_type) {
                    case newTask:
                        Task new_task = i.task;
                        if(!Controller.giveJob(new_task,nextEventTime)){
                            //current_time_events.add(new Event(Event.Event_type.setDevice)); //from buffer ???????? что-то сделать с циклом ивентов
                            Buffer.add(i.task,nextEventTime);
                        }
                        Iterator iterator = sources.iterator();
                        while(iterator.hasNext()){
                            Source source = (Source) iterator.next();
                            if (source.task==new_task && source.tasksRemain>0) {
                                source.nextTask();
                            } else if (source.task==new_task && source.tasksRemain==0) {
                                source.setStatus(0);
                            }
                        }
                        break;
                    case setDevice: //from buffer
                        Task buffer_task = Buffer.buffer.getLast();
                        if(Controller.giveJob(buffer_task,nextEventTime)) Buffer.removeLast(nextEventTime);
                        break;
                    case freeDevice:
                        Controller.setDeviceFree(nextEventTime);
                        if(get_current_event_time()!=nextEventTime && !Buffer.isEmpty()){
                            Task buffer_task_ = Buffer.buffer.getLast();
                            if(Controller.giveJob(buffer_task_,nextEventTime)) Buffer.removeLast(nextEventTime);
                        }
                        lastTimeEvent=nextEventTime;
                        break;
                }
            }
            current_time = nextEventTime;

        }

        //current_time = get_current_event_time();

        Iterator iterator = Statistics.allTasks.iterator();
        while(iterator.hasNext()){
            Task task = (Task) iterator.next();
            if (task.status==0) {
                iterator.remove();
            }
        }

        for (var i : Statistics.allTasks){
            if(i.status==2)Statistics.cancelsNum++;
            if(i.status==1)Statistics.tasksNum++;
            i.buffTime=i.buffOutTime-i.buffInTime;
            if(Statistics.ParentStatistic.putIfAbsent(i.taskParentID,new Stat(i.buffTime+i.deviceTime,i.buffTime,i.deviceTime))!=null){
                Stat temp_stat = Statistics.ParentStatistic.get(i.taskParentID);
                temp_stat.deviceTime+=i.deviceTime;
                temp_stat.buffTime+=i.buffTime;
                temp_stat.allTime+=i.buffTime+i.deviceTime;
                if(i.status==1 || i.status==2) temp_stat.increaseTaskNum();
                if(i.status==2) temp_stat.increaseCanceledTaskNum();
                Statistics.ParentStatistic.put(i.taskParentID,temp_stat);
            }
            /*if(Statistics.DeviceStatistic.putIfAbsent(i.taskDeviceID,i.deviceTime)==null){
                double prev_work_time = Statistics.DeviceStatistic.get(i.taskDeviceID);
                Statistics.DeviceStatistic.put(i.taskDeviceID, prev_work_time+ i.deviceTime);
            }*/
        }
        Iterator iterator_device = devices.iterator();
        while(iterator_device.hasNext()) {
            Device device = (Device) iterator_device.next();
            if (Statistics.DeviceStatistic.putIfAbsent(device.id, device.all_work_time) == null) {
                /*double prev_work_time = Statistics.DeviceStatistic.get(i.taskDeviceID);
                Statistics.DeviceStatistic.put(i.taskDeviceID, prev_work_time+ i.deviceTime);*/
            }
        }
    }
}