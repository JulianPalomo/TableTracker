package org.example.models;

import java.util.Objects;

public class Mesa {

    private int numero;
    private EstadoMesa estado;

    public Mesa(int numero) {
        this.numero = numero;
        this.estado = EstadoMesa.DISPONIBLE;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isDisponible() {
        return this.estado == EstadoMesa.DISPONIBLE;
    }


    public EstadoMesa getEstado() {
        return this.estado;
    }

    public void ocupada() {
        this.estado = EstadoMesa.OCUPADA;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mesa mesa = (Mesa) o;
        return numero == mesa.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

}