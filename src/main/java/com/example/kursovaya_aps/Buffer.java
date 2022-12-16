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

    static void add(Task task){
        if(current_size<max_size){
            current_size++;
            buffer.addFirst(task);
        }else {
            buffer.pollFirst();
            buffer.addFirst(task);
        }
    }
    static void removeLast(){
        if(current_size>0){
            current_size--;
            buffer.pollLast();
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
