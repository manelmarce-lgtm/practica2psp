package com.practica2.model;

public class SupervisorCentral {
    boolean estatAlerta = false;

    public synchronized void VerificaEstat(int id) throws InterruptedException {
        while (estatAlerta) {
            wait();
        }
    }

    public synchronized void posarEstatAlerta() {
        estatAlerta = true;
    }

    public synchronized void llevarEstatAlerta() {
        estatAlerta = false;
        notifyAll();
    }

    public synchronized boolean getEstatAlerta() {
        return estatAlerta;
    }
}