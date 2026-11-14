package com.practica2.tasks;

import com.practica2.model.CintaTransportadora;

public class AGVTask implements Runnable {
    private int id;
    CintaTransportadora cintaTransportadora;
    private int bateriesPerCarregar;
    private volatile String estat = "Esperant";

    public AGVTask(int id, CintaTransportadora cintaTransportadora, int bateriesPerCarregar) {
        this.id = id;
        this.cintaTransportadora = cintaTransportadora;
        this.bateriesPerCarregar = bateriesPerCarregar;
    }

    public int getId() {
        return id;
    }

    public String getEstat() {
        return estat;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < bateriesPerCarregar; i++) {
                estat = "Carregant";
                Thread.sleep(1500);
                String bateria = "AGV" + id + "-B" + (i + 1);
                cintaTransportadora.carregarBateries(bateria);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            estat = "Finalitzat";
        }
    }
}
