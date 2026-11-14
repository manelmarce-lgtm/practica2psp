package com.practica2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.practica2.model.CentralSoldadura;
import com.practica2.model.CintaTransportadora;
import com.practica2.model.SupervisorCentral;
import com.practica2.tasks.AGVTask;
import com.practica2.tasks.RobotTask;
import com.practica2.tasks.SensorAvariesTask;
import com.practica2.tasks.TecnicMantenimentTask;
import com.practica2.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        CintaTransportadora cinta = new CintaTransportadora();
        CentralSoldadura central = new CentralSoldadura();
        SupervisorCentral supervisor = new SupervisorCentral();

        List<AGVTask> agvTasks = new ArrayList<>();
        List<RobotTask> robotTasks = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Introdueix el nombre de robots d'ensamblatge: ");
        int numRobots = scanner.nextInt();

        System.out.println("Introdueix el nombre de AGVs: ");
        int numAGVs = scanner.nextInt();
        
        int totalBateries = numRobots *2;
        int bateriesBase = totalBateries / numAGVs;
        int numBateriesExtra = totalBateries % numAGVs;

        for (int i = 0; i < numAGVs; i++) {
            int bateriesPerAVG = bateriesBase + (i < numBateriesExtra ? 1 : 0);
            AGVTask agvTask = new AGVTask(i+1, cinta, bateriesPerAVG);
            agvTasks.add(agvTask);
            threads.add(new Thread(agvTask));
        }

        for (int i = 1; i <= numRobots; i++) {
            RobotTask robot = new RobotTask(i, cinta, central, supervisor);
            robotTasks.add(robot);
            threads.add(new Thread(robot));
        }
        
        SensorAvariesTask filSensorAvariesTask = new SensorAvariesTask(central);
        TecnicMantenimentTask filTecnicMantenimentTask = new TecnicMantenimentTask(central);
        Thread filSensor = new Thread(filSensorAvariesTask);
        Thread filTecnic = new Thread(filTecnicMantenimentTask);
        Thread filAlertes = new Thread(() ->  {
            try{
                while(!Thread.currentThread().isInterrupted()){
                    Thread.sleep(4000);
                    supervisor.posarEstatAlerta();
                    Thread.sleep(2000);
                    supervisor.llevarEstatAlerta();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });
    filSensor.setDaemon(true);
    filTecnic.setDaemon(true);
    filAlertes.setDaemon(true);

    ConsoleView consoleView = new ConsoleView(cinta, central, supervisor, agvTasks, robotTasks, filSensorAvariesTask, filTecnicMantenimentTask);
    Thread filView = new Thread(consoleView);
    
    filSensor.start();
    filTecnic.start();
    filAlertes.start();
    filView.start();

    for(Thread thread : threads){
        thread.start();
    }

    try {
        for(Thread thread : threads){
            thread.join();
        }
        filView.join();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("Error en la simulació");
    }
    
    scanner.close();
}
}