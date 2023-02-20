package com.example.kursovaya_aps;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class InterfaceController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_generate_auto;

    @FXML
    private Button btn_next_step;

    @FXML
    private Button btn_setUp;

    @FXML
    private TextField input_a;

    @FXML
    private TextField input_b;

    @FXML
    private TextField input_buffer_num;

    @FXML
    private TextField input_device_num;

    @FXML
    private TextField input_lamda_num;

    @FXML
    private TextField input_simulation_time;

    @FXML
    private TextField input_source_num;

    @FXML
    private Label field_current_time;

    @FXML
    private TableView<Task> table_buffer;

    @FXML
    private TableColumn<Task, Integer> table_buffer_id;

    @FXML
    private TableColumn<Task, String> table_buffer_status;

    @FXML
    private TableColumn<Task, String> table_buffer_task;

    @FXML
    private TableColumn<Device, Integer> table_device_id;

    @FXML
    private TableView<DeviceStat> table_device_stat;

    @FXML
    private TableColumn<DeviceStat, String> table_device_stat_coef;

    @FXML
    private TableColumn<DeviceStat, String> table_device_stat_id;

    @FXML
    private TableColumn<Device, String> table_device_status;

    @FXML
    private TableColumn<Device, String> table_device_task;

    @FXML
    private TableColumn<Device, String> table_device_work_time;

    @FXML
    private TableView<Device> table_devices;

    @FXML
    private TableView<SourceStat> table_source_stat;

    @FXML
    private TableColumn<SourceStat, String> table_source_stat_Disp_buff;

    @FXML
    private TableColumn<SourceStat, String> table_source_stat_Disp_work;

    @FXML
    private TableColumn<SourceStat, String> table_source_stat_Pcanceled;

    @FXML
    private TableColumn<SourceStat, String> table_source_stat_T_Buffer;

    @FXML
    private TableColumn<SourceStat, String> table_source_stat_T_avg;

    @FXML
    private TableColumn<SourceStat, String> table_source_stat_T_device;

    @FXML
    private TableColumn<SourceStat, String> table_source_stat_id;

    @FXML
    private TableColumn<SourceStat, String> table_source_stat_taskNum;

    @FXML
    private TableView<Source> table_sources;

    @FXML
    private TableColumn<Source, Integer> table_sources_id;

    @FXML
    private TableColumn<Source, String> table_sources_status;

    @FXML
    private TableColumn<Source, Integer> table_sources_task_id;

    @FXML
    private TableColumn<Source, String> table_sources_time;

    void source_table_init (){
        table_sources.getItems().clear();
        for (var i : Main.sources){
            table_sources.getItems().add(i);
        }
    }

    void device_table_init (){
        synchronized (Device.devices) {
            table_devices.getItems().clear();
        /*for (var i : Device.devices){
            table_devices.getItems().add(i);
        }*/
            Iterator iterator = Device.devices.iterator();
            while (iterator.hasNext()) {
                Device device = (Device) iterator.next();
                table_devices.getItems().add(device);
            }
        }
    }

    void buffer_table_init (){
        table_buffer.getItems().clear();
        /*for (var i : Buffer.buffer){
            table_buffer.getItems().add(i);
        }*/
        Iterator iterator = Buffer.buffer.iterator();
        while(iterator.hasNext()){
            Task task = (Task) iterator.next();
            table_buffer.getItems().add(task);
        }
    }

    void device_stat_table_init(){ ///доделать чтобы был id прибора
        table_device_stat.getItems().clear();

        //ArrayList<DeviceStat> deviceStats = new ArrayList<>();
        for (var i : Statistics.DeviceStatistic.keySet()){
            DeviceStat deviceStat = new DeviceStat();
            deviceStat.deviceID= i;
            deviceStat.usageCoef=Statistics.DeviceStatistic.get(i)/Main.lastTimeEvent;
            table_device_stat.getItems().add(deviceStat);
        }
    }

    void source_stat_table_init(){
        for (var i : Statistics.ParentStatistic.keySet()){
            SourceStat sourceStat = new SourceStat();
            sourceStat.sourceID=i;
            sourceStat.tasksNum = Statistics.ParentStatistic.get(i).taskNum;
            sourceStat.cancelP = (double) Statistics.ParentStatistic.get(i).taskCanceledNum/(Statistics.ParentStatistic.get(i).taskNum);
            System.out.println(Statistics.ParentStatistic.get(i).taskCanceledNum%Statistics.ParentStatistic.get(i).taskNum);
            sourceStat.avgAllTime = Statistics.ParentStatistic.get(i).allTime/Statistics.ParentStatistic.get(i).taskNum;
            sourceStat.avgTimeBuff = Statistics.ParentStatistic.get(i).buffTime/Statistics.ParentStatistic.get(i).taskNum;
            sourceStat.avgTimeDevice = Statistics.ParentStatistic.get(i).deviceTime/Statistics.ParentStatistic.get(i).taskNum;
            sourceStat.BuffDisp = Math.pow(Statistics.ParentStatistic.get(i).buffTime,2)/Statistics.ParentStatistic.get(i).taskNum;
            sourceStat.DeviceDisp = Math.pow(Statistics.ParentStatistic.get(i).deviceTime,2)/Statistics.ParentStatistic.get(i).taskNum;
            table_source_stat.getItems().add(sourceStat);
        }
    }

    @FXML
    void initialize() {

        btn_setUp.setOnAction(actionEvent -> {
            Main.sourcesNum=Integer.parseInt(input_source_num.getText());
            Main.bufferNum=Integer.parseInt(input_buffer_num.getText());
            Main.devicesNum=Integer.parseInt(input_device_num.getText());
            Main.lambda=Integer.parseInt(input_lamda_num.getText());
            Main.tasksNum=Integer.parseInt(input_simulation_time.getText());
            Main.left_bord=Integer.parseInt(input_a.getText());
            Main.right_bord=Integer.parseInt(input_b.getText());
        });



        source_table_init();
        device_table_init();
        buffer_table_init();
        btn_next_step.setOnAction(actionEvent -> {
            //Main.mutex.notify();
            synchronized (Main.mutex){
                Main.mutex.notify();
            }
            synchronized (this){
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            source_table_init();
            device_table_init();
            buffer_table_init();
            field_current_time.setText("Текущее время: "+Main.current_time);
        });


        table_sources_id.setCellValueFactory(new PropertyValueFactory<Source,Integer>("source_id"));
        table_sources_task_id.setCellValueFactory(new PropertyValueFactory<Source,Integer>("currentTaskID"));
        table_sources_status.setCellValueFactory(new PropertyValueFactory<Source,String>("status"));
        table_sources_time.setCellValueFactory(new PropertyValueFactory<Source,String>("generate_time"));

        table_device_id.setCellValueFactory(new PropertyValueFactory<Device,Integer>("id"));
        table_device_task.setCellValueFactory(new PropertyValueFactory<Device,String>("taskINFO")); // доделать
        table_device_status.setCellValueFactory(new PropertyValueFactory<Device,String>("status"));
        table_device_work_time.setCellValueFactory(new PropertyValueFactory<Device,String>("event_time"));

        //table_buffer_id.setCellValueFactory(new PropertyValueFactory<Task,Integer>("id"));
        table_buffer_task.setCellValueFactory(new PropertyValueFactory<Task,String>("taskINFO"));
        table_buffer_status.setCellValueFactory(new PropertyValueFactory<Task,String>("status"));

        table_device_stat_id.setCellValueFactory(new PropertyValueFactory<DeviceStat,String>("deviceID"));
        table_device_stat_coef.setCellValueFactory(new PropertyValueFactory<DeviceStat,String>("usageCoef"));

        table_source_stat_id.setCellValueFactory(new PropertyValueFactory<SourceStat,String>("sourceID"));
        table_source_stat_taskNum.setCellValueFactory(new PropertyValueFactory<SourceStat,String>("tasksNum"));
        table_source_stat_Pcanceled.setCellValueFactory(new PropertyValueFactory<SourceStat,String>("cancelP"));
        table_source_stat_T_avg.setCellValueFactory(new PropertyValueFactory<SourceStat,String>("avgAllTime"));
        table_source_stat_T_Buffer.setCellValueFactory(new PropertyValueFactory<SourceStat,String>("avgTimeBuff"));
        table_source_stat_T_device.setCellValueFactory(new PropertyValueFactory<SourceStat,String>("avgTimeDevice"));
        table_source_stat_Disp_buff.setCellValueFactory(new PropertyValueFactory<SourceStat,String>("BuffDisp"));
        table_source_stat_Disp_work.setCellValueFactory(new PropertyValueFactory<SourceStat,String>("DeviceDisp"));


        btn_generate_auto.setOnAction(actionEvent -> {
            Main.auto();
            synchronized (this){
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            device_stat_table_init();
            source_stat_table_init();
        });




        //table_buffer_id.setCellValueFactory(new PropertyValueFactory<Task,Integer>(""));
        //table_buffer_id


    }

}
