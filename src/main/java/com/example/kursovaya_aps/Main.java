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

    static double current_time = 0;

    static int tasksNum = 12;

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

    public static void main(String[] args) {
        //final Object mutex = new Object();
        //launch();
        double a = 5;
        double b = 10;
        ArrayList<Double> list = new ArrayList<Double>();
        for (int i = 0; i < 20; i++) {
            list.add(a + (b - a) * (random.nextInt(100) % 100) / 100);
        }
        //ArrayList<Thread> threads = new ArrayList<>();
        /*for (int i=0;i<5;i++){
            threads.add(new Source());
        }*/

        // Thread manager = new Controller();

        long a_ = 0;
        long b_ = 0;
        a_ = System.currentTimeMillis();
        /*while (b_-a_<10*1000){
            b_=System.currentTimeMillis();
        }*/

        /*for (var i : threads) {
            i.interrupt();
        }*/


        //double current_time = 0;

        /*for (int i = 0; i < 5; i++) {
            sources.add(new Source((a+(b-a)*(random.nextInt(100) %100)/100),i));
        }*/
        for (int i = 0; i < 5; i++) {
            sources.add(new Source(i + 0.5, i));
        }
        for (int i = 0; i < 5; i++) {
            devices.add(new Device(i + 1, i+0.6));
        }

        Buffer.current_size = 0;
        Buffer.max_size = 3;

        //Buffer.buffer.addFirst(new Task(0,0,0));
        //Buffer.buffer.addFirst(new Task(0,0,0));

        /*int sum=0;
        for(int i = 0; i<100000;i++){
            sum+=i;
        }*/


        /*while (simulation_time > current_time) {
            double nextEventTime = get_current_event_time();
            ArrayList<Event> current_time_events = get_current_time_Events(nextEventTime);

            for (var i : current_time_events) {
                switch (i.event_type) {
                    case newTask:
                        Task new_task = i.task;
                        Controller.giveJob(new_task);
                        break;
                    case setDevice: //from buffer
                        Task buffer_task = Buffer.buffer.pollLast();
                        Controller.giveJob(buffer_task);
                        break;
                    case freeDevice:
                        Controller.setDeviceFree(nextEventTime);
                        break;
                }
            }

            current_time = nextEventTime;
        }*/

        //double simulation_time = 10;
        Thread t1 = new Thread() {
            public void run(){
                double simulation_time = 4.5;

                current_time = 0;
                boolean isFirstIteration = true;
                while (!Device.isAllFree() || isFirstIteration ) {
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
                                        Buffer.add(i.task);
                                    }
                                    if(tasksNum>=0){
                                        Iterator iterator = sources.iterator();
                                        while(iterator.hasNext()){
                                            Source source = (Source) iterator.next();
                                            if (source.task==new_task) {
                                                source.nextTask();
                                            }
                                        }
                                    }else {
                                        Iterator iterator = sources.iterator();
                                        while (iterator.hasNext()) {
                                            Source source = (Source) iterator.next();
                                            source.setStatus(0);
                                        }
                                    }


                                    break;
                                case setDevice: //from buffer
                                    Task buffer_task = Buffer.buffer.getLast();
                                    if(Controller.giveJob(buffer_task,nextEventTime)) Buffer.removeLast();
                                    break;
                                case freeDevice:
                                    Controller.setDeviceFree(nextEventTime);
                                    if(get_current_event_time()!=nextEventTime && !Buffer.isEmpty()){
                                        Task buffer_task_ = Buffer.buffer.getLast();
                                        if(Controller.giveJob(buffer_task_,nextEventTime)) Buffer.removeLast();
                                    }
                                    break;
                            }
                        }
                        current_time = nextEventTime;
                    }
                }

            }
        };

        t1.start();
        //t2.start();


        Interface.InterfaceLaunch(new String[]{});
        t1.interrupt();
    }
}