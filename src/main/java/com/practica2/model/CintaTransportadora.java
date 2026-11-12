package com.practica2.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CintaTransportadora {
 List<String> coa = new LinkedList<>();
 int capacitatMax = 5;

 private final ReentrantLock lock = new ReentrantLock();
 private final Condition cintaNoPlena = lock.newCondition();
 private final Condition cintaNoBuida = lock.newCondition();

 public void carregarBateries(String bateria) throws InterruptedException {
    lock.lock();
    try {
        while (coa.size() == capacitatMax){
            cintaNoPlena.await();
        }
            coa.add(bateria);
            System.out.println("Bateria " + bateria + " carregada");
            cintaNoBuida.signal();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
 

 public void agafarBateria(){
    lock.lock();
    try {
        while(coa.isEmpty()){
       
            cintaNoBuida.await();
        }
            String bateria = coa.remove(0);
            System.out.println("Bateria " + bateria + " agafada");
            cintaNoPlena.signal();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    } finally {
        lock.unlock();
    }
 }
}
