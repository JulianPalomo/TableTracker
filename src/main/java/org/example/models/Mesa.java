package org.example.models;

import java.util.Objects;

public class Mesa {

    public static void decrementarNumeroAuto() {
        Mesa.numeroAuto--;
    }

    public static int numeroAuto = 1;
    private int id;
    private EstadoMesa estado;
    private Pedido pedido;

    private int x;
    private int y;
    private int ancho;
    private int alto;

    public Mesa() {
        this.id = numeroAuto;
        this.estado = EstadoMesa.DISPONIBLE;
        this.pedido = null;
        this.x = 50;
        this.y = 50;
        this.alto = 50;
        this.ancho = 100;
        numeroAuto++;
    }

    public Mesa(int x,int y, int ancho, int alto) {
        this.id = numeroAuto;
        this.estado = EstadoMesa.DISPONIBLE;
        this.pedido = null;
        numeroAuto++;

        this.x = x;
        this.y = y;
        this.alto = alto;
        this.ancho = ancho;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getId() {
        return id;
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
        return id == mesa.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}