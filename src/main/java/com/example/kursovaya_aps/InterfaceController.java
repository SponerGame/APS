package com.example.kursovaya_aps;

import java.net.URL;
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
    private Button btn_next_step;

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
    private TableColumn<Device, String> table_device_status;

    @FXML
    private TableColumn<Device, String> table_device_task;

    @FXML
    private TableColumn<Device, String> table_device_work_time;

    @FXML
    private TableView<Device> table_devices;

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

    @FXML
    void initialize() {
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


        //table_buffer_id.setCellValueFactory(new PropertyValueFactory<Task,Integer>(""));
        //table_buffer_id


    }

}
