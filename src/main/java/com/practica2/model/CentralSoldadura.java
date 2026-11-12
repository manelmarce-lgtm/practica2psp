package com.practica2.model;

import java.util.concurrent.Semaphore;

public class CentralSoldadura {
    int estacions = 3;
    Semaphore semafor = new Semaphore(estacions,true);
    boolean ocupades[] = new boolean[estacions];
    public void soldar(int idRobot){
        int estacioAssignada = -1;
        try {
            semafor.acquire();
            estacioAssignada = ocuparEstacio();
            System.out.println("Robot "+ idRobot + "Soldant a estació: " + estacioAssignada);
            Thread.sleep(1000);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } finally {

            if(estacioAssignada!= -1){
                alliberarEstacio(estacioAssignada, idRobot);
                semafor.release();
            }
            
        }
    }

    private synchronized int ocuparEstacio(){
        for(int i = 0; i < ocupades.length; i++){
            if(!ocupades[i]){
                ocupades[i] = true;
                return i;
            }
        }
        return -1;
    }

    private synchronized void alliberarEstacio(int estacio, int idRobot){

        ocupades[estacio] = false;
        System.out.println("Robot " + idRobot + " allibera estació " + estacio);

    }
}