package org.example.models;

import org.example.interfaces.Buscable;
import org.example.interfaces.Filtrable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLongArray;

public class Producto implements Cloneable {

    private static int contador = 0;  // Variable estática para mantener el próximo ID disponible
    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private String observacion; // nuevo campo


    public Producto( String nombre, String Categoria, double precio) {
        this.id = contador++;
        this.nombre = nombre;
        this.categoria = Categoria;
        this.precio = precio;
        this.observacion = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String Categoria) {
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
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;

    }

    @Override
    public Producto clone() {
        try {
            return (Producto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar el producto", e);
        }
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

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", categoria=" + categoria +
                ", precio=" + precio +
                '}';
    }




}
