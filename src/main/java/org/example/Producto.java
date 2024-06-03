package org.example.models;

public class Producto {
    private static int contador = 0;  // Variable estática para mantener el próximo ID disponible
    private int id;
    private String nombre;
    private TipoDeProducto TipoDeProducto;
    private double precio;

    public Producto( String nombre, TipoDeProducto TipoDeProducto, double precio) {
        this.id =contador++;
        this.nombre = nombre;
        this.TipoDeProducto = TipoDeProducto;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDeProducto getTipoDeProducto() {
        return TipoDeProducto;
    }

    public void setTipoDeProducto(TipoDeProducto TipoDeProducto) {
        this.TipoDeProducto = TipoDeProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public int getId() {
        return id;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}
