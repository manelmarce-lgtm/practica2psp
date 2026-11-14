package com.practica2.model;

import java.util.concurrent.Semaphore;

public class CentralSoldadura {
    int estacions = 3;
    Semaphore semafor = new Semaphore(estacions,true);
    Semaphore semaforFirma = new Semaphore(1,true);
    boolean ocupades[] = new boolean[estacions];
    private boolean[] estacionsAvariades = new boolean[estacions];


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

    public synchronized int getEstacionsOcupades(){
        int estacionsOcupades = 0;
        for(boolean ocupada : ocupades){
            if(ocupada){
                estacionsOcupades++;
            }
        }
        return estacionsOcupades;
    }

    public void signarCertificat(int idRobot){
        boolean estaOcupat = false;
        try {
            semaforFirma.acquire();
            estaOcupat = true;
            System.out.println("Robot "+ idRobot + " signant certificat");
            Thread.sleep(1400);
            System.out.println("Robot "+ idRobot + " ha signat el certificat");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if(estaOcupat){
                semaforFirma.release();
            }
        }
    }

    public synchronized void crearAvaria() {
        if(semafor.tryAcquire()){
            for(int i = 0; i < ocupades.length; i++){
                if(!ocupades[i] && !estacionsAvariades[i]){
                   estacionsAvariades[i] = true;
                   notifyAll();
                   break;
                }
            }
    }
}
}