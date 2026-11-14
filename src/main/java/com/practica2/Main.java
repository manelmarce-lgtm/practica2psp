package com.practica2;

import java.util.ArrayList;
import java.util.List;

import com.practica2.model.CentralSoldadura;
import com.practica2.model.CintaTransportadora;
import com.practica2.tasks.AGVTask;
import com.practica2.tasks.RobotTask;
import com.practica2.tasks.SensorAvariesTask;
import com.practica2.tasks.TecnicMantenimentTask;
import com.practica2.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        CintaTransportadora cinta = new CintaTransportadora();
        CentralSoldadura central = new CentralSoldadura();
        List<AGVTask> agvTasks = new ArrayList<>();
        List<RobotTask> robotTasks = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            AGVTask agvTask = new AGVTask(i,cinta,20 );
            agvTasks.add(agvTask);
            threads.add( new Thread(agvTask));
        }

        for (int i = 1; i <= 4; i++) {
            RobotTask robot = new RobotTask(i, cinta, central);
            robotTasks.add(robot);
            threads.add( new Thread(robot));
        }

        SensorAvariesTask filSensorAvariesTask = new SensorAvariesTask(central);
        TecnicMantenimentTask filTecnicMantenimentTask = new TecnicMantenimentTask(central);

        Thread filSensor = new Thread(filSensorAvariesTask);
        Thread filTecnic = new Thread(filTecnicMantenimentTask);

        filSensor.setDaemon(true);
        filTecnic.setDaemon(true);
        
        ConsoleView consoleView = new ConsoleView(cinta, central, agvTasks, robotTasks, filSensorAvariesTask, filTecnicMantenimentTask);
        Thread filView = new Thread(consoleView);
        filSensor.start();
        filTecnic.start();
        filView.start();
        for (Thread thread : threads) {
            thread.start();
        }
        
    }
}