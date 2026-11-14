package com.practica2.model;

public class SupervisorCentral {
    boolean estatAlerta = false;
    
    public synchronized void VerificaEstat(int id) throws InterruptedException {
    while( estatAlerta){
            wait();
        } 
    }

    public synchronized  void PosarEstatAlerta() {
        estatAlerta = true;
    }

    public synchronized void LlevarEstatAlerta() {
        estatAlerta = false;
        notifyAll();
    }

    public synchronized boolean getEstatAlerta() {
        return estatAlerta;
    }
}