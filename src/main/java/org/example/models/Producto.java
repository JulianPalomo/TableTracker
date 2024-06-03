package org.example.models;

public class Producto {
    private static int contador = 0;  // Variable estática para mantener el próximo ID disponible
    private int id;
    private String nombre;
    private org.example.models.Categoria Categoria;
    private double precio;

    public Producto( String nombre, Categoria Categoria, double precio) {
        this.id = contador++;
        this.nombre = nombre;
        this.Categoria = Categoria;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return Categoria;
    }

    public void setCategoria(Categoria Categoria) {
        this.Categoria = Categoria;
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
