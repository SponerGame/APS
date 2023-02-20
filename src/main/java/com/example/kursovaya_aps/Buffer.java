package com.example.kursovaya_aps;

import java.util.ArrayDeque;
import java.util.Queue;

public class Buffer {
    static ArrayDeque<Task> buffer = new ArrayDeque<Task>();
    static int current_size;
    static int max_size;

    public static boolean isFull(){
        if(current_size==max_size) return true;
        return false;
    }
    public static boolean isEmpty(){
        if(current_size==0) return true;
        return false;
    }

    static void add(Task task,double time){
        if(current_size<max_size){
            current_size++;
            task.buffInTime=time;
            buffer.addFirst(task);
        }else {
            Task task1 = buffer.pollFirst();
            task1.buffOutTime=time;
            task1.status=2;
            buffer.addFirst(task);
        }
    }
    static void removeLast(double time){
        if(current_size>0){
            current_size--;
            Task task = buffer.pollLast();
            task.buffOutTime=time;
        }
    }
    static Task peek(Task task){
        return buffer.getLast();
        //if(current_size<max_size){
            //current_size--;
            //buffer.add(task);
        //}
    }
}
