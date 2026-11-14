package com.practica2.tasks;

import com.practica2.model.CentralSoldadura;
import com.practica2.model.CintaTransportadora;
import com.practica2.model.SupervisorCentral;

public class RobotTask implements Runnable {
    int id;
    CintaTransportadora cintaTransportadora;
    CentralSoldadura centralSoldadura;
    SupervisorCentral supervisorCentral;
    private volatile int bateria = 0;
    private volatile String estat = "Esperant";

    public RobotTask(int id, CintaTransportadora cintaTransportadora, CentralSoldadura centralSoldadura,
            SupervisorCentral supervisorCentral) {
        this.id = id;
        this.cintaTransportadora = cintaTransportadora;
        this.centralSoldadura = centralSoldadura;
        this.supervisorCentral = supervisorCentral;
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

    private void verificarEmergencia() throws InterruptedException {
        if (supervisorCentral.getEstatAlerta()) {
            estat = "Pausat per sobreescalfament";
        }
        supervisorCentral.VerificaEstat(id);
    }

    private void assegurarBateria(int costEnergetic) {
        while (bateria < costEnergetic) {
            estat = "Agafant bateria";
            cintaTransportadora.agafarBateria();
            bateria += 50;
        }
    }

    @Override
    public void run() {
        try {
            assegurarBateria(50);
            Thread.sleep(2000);

            verificarEmergencia();
            estat = "Soldant";
            centralSoldadura.soldar(id);
            bateria -= 50;
            Thread.sleep(2000);

            assegurarBateria(50);
            verificarEmergencia();
            estat = "Signant certificat";
            centralSoldadura.signarCertificat(id);
            bateria -= 50;
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            estat = "Finalitzat";
        }
    }
}
