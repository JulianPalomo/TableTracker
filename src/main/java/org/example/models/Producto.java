package org.example.models;

import org.example.interfaces.Filtrable;

import java.util.Objects;

public class Producto implements Filtrable<Categoria> {

    private static int contador = 0;  // Variable estática para mantener el próximo ID disponible
    private int id;
    private String nombre;
    private Categoria categoria;
    private double precio;

    public Producto( String nombre, Categoria Categoria, double precio) {
        this.id = contador++;
        this.nombre = nombre;
        this.categoria = Categoria;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria Categoria) {
        this.categoria = Categoria;
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
    public boolean cumpleFiltro(Categoria categoria) {
        return this.categoria == categoria;
    }

    @Override
    public int compareTo(Producto otroProducto) {
        return this.nombre.compareTo(otroProducto.getNombre());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id == producto.id && Objects.equals(nombre, producto.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }



}
