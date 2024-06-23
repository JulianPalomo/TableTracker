package org.example.models;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pedido{
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

    public double getTotal() {
        double total = 0.0;
        for (Producto producto : listaProductos) {
            total += producto.getPrecio();
        }
        return total;
    }
}
