package org.example.models;

public class Mesa {
    private int numero;
    private boolean disponible;
    public Mesa(String number, int numero, EstadoMesa disponible) {
        this.numero = numero;
        this.disponible=true;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isDisponible() {
        return disponible;
    }
}