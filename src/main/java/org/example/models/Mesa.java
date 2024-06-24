package org.example.models;

import java.util.Objects;

public class Mesa extends Objeto {

    private EstadoMesa estado;
    private Pedido pedido;
    private int nroMesa;
    private static int nroMesaAuto = 1;

    public Mesa() {
        super();
        this.estado = EstadoMesa.DISPONIBLE;
        this.pedido = null;
        this.nroMesa = nroMesaAuto;
        nroMesaAuto++;
    }

    public Mesa(int x,int y, int ancho, int alto) {
        super(x,y,ancho,alto);
        this.estado = EstadoMesa.DISPONIBLE;
        this.pedido = null;
    }

    public int getNroMesa(){
        return this.nroMesa;
    }

    public static void decrementarNumeroAuto() {
        Mesa.nroMesaAuto--;
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
        return nroMesa == mesa.nroMesa;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nroMesa);
    }

}