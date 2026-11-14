package com.practica2.view;

import java.util.List;

import com.practica2.model.CentralSoldadura;
import com.practica2.model.CintaTransportadora;
import com.practica2.model.SupervisorCentral;
import com.practica2.tasks.AGVTask;
import com.practica2.tasks.RobotTask;
import com.practica2.tasks.SensorAvariesTask;
import com.practica2.tasks.TecnicMantenimentTask;

public class ConsoleView implements Runnable {
    private final CintaTransportadora cintaTransportadora;
    private final CentralSoldadura centralSoldadura;
    private final SupervisorCentral supervisorCentral;
    private final List<AGVTask> agvTasks;
    private final List<RobotTask> robotTasks;
    private final SensorAvariesTask sensorAvariesTask;
    private final TecnicMantenimentTask tecnicMantenimentTask;

    public ConsoleView(CintaTransportadora cintaTransportadora, CentralSoldadura centralSoldadura,
            SupervisorCentral supervisorCentral, List<AGVTask> agvTasks, List<RobotTask> robotTasks,
            SensorAvariesTask sensorAvariesTask, TecnicMantenimentTask tecnicMantenimentTask) {
        this.cintaTransportadora = cintaTransportadora;
        this.centralSoldadura = centralSoldadura;
        this.supervisorCentral = supervisorCentral;
        this.agvTasks = agvTasks;
        this.robotTasks = robotTasks;
        this.sensorAvariesTask = sensorAvariesTask;
        this.tecnicMantenimentTask = tecnicMantenimentTask;
    }

    private boolean totsFinalitzats() {
        for (RobotTask r : robotTasks) {
            if (!"Finalitzat".equals(r.getEstat()))
                return false;
        }
        for (AGVTask a : agvTasks) {
            if (!"Finalitzat".equals(a.getEstat()))
                return false;
        }
        return true;
    }

    private void imprimirFuncionament() {
        System.out.println("ESTAT DE LA FÀBRICA");
        System.out.println(
                "Supervisor central: " + (supervisorCentral.getEstatAlerta() ? "ALERTA SOBREESCALFAMENT!" : "Normal"));
        System.out.println("Cinta de transportadora: " + cintaTransportadora.getQuantitatBateries() + " bateries");
        System.out.println("Central de soldadura: " + centralSoldadura.getEstacionsOcupades() + " ocupades "
                + centralSoldadura.getEstacionsAvariades() + " avariades");
        System.out.println("--------------------------------------------");
        System.out.println("Sensor d'avaries: " + sensorAvariesTask.getEstat());
        System.out.println("Tècnic de manteniment: " + tecnicMantenimentTask.getEstat());
        System.out.println("--------------------------------------------");

        for (AGVTask a : agvTasks) {
            System.out.println("AGV " + a.getId() + ": " + a.getEstat());
        }
        for (RobotTask r : robotTasks) {
            System.out.println("Robot " + r.getId() + ": " + r.getEstat()
                    + " Bateria: " + r.getBateria() + " %");
        }
        System.out.println("--------------------------------------------");
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !totsFinalitzats()) {
                Thread.sleep(2000);
                imprimirFuncionament();
            }
            imprimirFuncionament();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
