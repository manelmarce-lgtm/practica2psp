package com.practica2.model;

public class SupervisorCentral {
    boolean estatAlerta = false;
    
    public synchronized void VerificaEstat(int id) {
    while( estatAlerta){
        try {
            wait();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    }

    public synchronized  void PosarEstatAlerta() {
        estatAlerta = true;
    }

    public synchronized void LlevarEstatAlerta() {
        estatAlerta = false;
        notifyAll();
    }
}