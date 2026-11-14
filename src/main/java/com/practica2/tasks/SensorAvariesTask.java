package com.practica2.tasks;

import com.practica2.model.CentralSoldadura;

public class SensorAvariesTask implements Runnable{
    CentralSoldadura centralSoldadura;
    private volatile String estat = "Esperant";


    public SensorAvariesTask(CentralSoldadura centralSoldadura) {
        this.centralSoldadura = centralSoldadura;
    }

    public String getEstat(){
        return estat;
    }


    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                estat = "Apagat";
                Thread.sleep((int) Math.floor(Math.random() * (4000 - 2000 +1)+2000));
                estat = "Generant avaria";
                centralSoldadura.crearAvaria();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
