package org.example.models;


import org.example.interfaces.Filtrable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Pedido implements Filtrable<LocalDate> {
    private ArrayList<Producto> listaProductos;
    private double total;
    private LocalDate fecha;

    //private EstadoPedido estadoPedido;

    private Factura factura;

    public Pedido() {
        this.listaProductos = new ArrayList<>();
        this.total = 0;
        this.fecha = LocalDate.now();

    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public void agregarProducto(Producto Producto) {
        listaProductos.add(Producto);
        total += Producto.getPrecio();
    }
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "Pedido [listaProductos=" + listaProductos + ", total=" + total + ", fecha=" + fecha + "]";
    }


    @Override
    public boolean cumpleFiltro(LocalDate criterio) {
        return this.fecha.equals(fecha);
    }
}
