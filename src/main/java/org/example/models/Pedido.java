package org.example.models;


import org.example.interfaces.Filtrable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pedido implements Filtrable<LocalDate> {
    private ArrayList<Producto> listaProductos;
    private LocalDate fecha;

    public Pedido() {
        this.listaProductos = new ArrayList<>();
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
    }

    public void agregarProducto(ArrayList<Producto> productos)
    {
        listaProductos.addAll(productos);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public boolean cumpleFiltro(LocalDate criterio) {
        return this.fecha.equals(fecha);
    }
}
