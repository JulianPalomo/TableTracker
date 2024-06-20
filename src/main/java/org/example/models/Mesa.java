package org.example.models;

import java.util.Objects;

public class Mesa {

    public static int numeroAuto = 1;
    private int numero;
    private EstadoMesa estado;
    private Pedido pedido;

    public Mesa() {
        this.numero = numeroAuto;
        this.estado = EstadoMesa.DISPONIBLE;
        this.pedido = null;
        numeroAuto++;
    }

    public int getNumero() {
        return numero;
    }

    //metodos para DETERMINAR COLOR EN EL BOTTON
    public boolean isDisponible() {
        return this.estado == EstadoMesa.DISPONIBLE;
    }
    public EstadoMesa getEstado() {
        return this.estado;
    }

    public void ocuparMesa() {
        this.estado = EstadoMesa.OCUPADA;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido)
    {
        this.pedido = pedido;
    }

    public void liberarMesa() {
        this.pedido = null;
        this.estado = EstadoMesa.DISPONIBLE;
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