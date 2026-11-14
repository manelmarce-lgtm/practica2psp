package com.practica2.tasks;

import com.practica2.model.CentralSoldadura;

public class TecnicMantenimentTask implements Runnable {
    private volatile String estat = "Esperant";
    private CentralSoldadura centralSoldadura;

    public TecnicMantenimentTask(CentralSoldadura centralSoldadura) {
        this.centralSoldadura = centralSoldadura;
    }

    public String getEstat() {
        return estat;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                estat = "Pendent de manteniment";
                centralSoldadura.esperarAvaria();
                estat = "Fent el manteniment";
                Thread.sleep(2000);
                centralSoldadura.finalitzarManteniment();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}