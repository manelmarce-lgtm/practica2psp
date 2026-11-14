package com.practica2.tasks;

import com.practica2.model.CentralSoldadura;
import com.practica2.model.CintaTransportadora;

public class RobotTask implements Runnable{
    int id;
    CintaTransportadora cintaTransportadora;
    CentralSoldadura centralSoldadura;
    private volatile int bateria = 0;
    private volatile String estat = "Esperant";

    public RobotTask(int id, CintaTransportadora cintaTransportadora, CentralSoldadura centralSoldadura) {
        this.id = id;
        this.cintaTransportadora = cintaTransportadora;
        this.centralSoldadura = centralSoldadura;
    }
    
    public int getBateria() {
        return bateria;
    }
    public String getEstat() {
        return estat;
    }

    public int getId() {
        return id;
    }

    private void assegurarBateria(int costEnergetic) {
        while(bateria < costEnergetic) {
            estat = "Agafant bateria";
            cintaTransportadora.agafarBateria();
            bateria += 50;
        }
    }
    @Override
    public void run() {
      try {
          assegurarBateria(10);  
          Thread.sleep(2000);
          estat = "Soldant";
          centralSoldadura.soldar(id);
          bateria -= 10;
          Thread.sleep(2000);
          
          assegurarBateria(20);
          estat= "Signant certificat";
          centralSoldadura.signarCertificat(id);
          bateria -= 20;
          Thread.sleep(2000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        estat = "Finalitzat";
      } 
    }
}
