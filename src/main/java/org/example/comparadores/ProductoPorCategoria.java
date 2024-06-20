package org.example.comparadores;

import org.example.models.Producto;

import java.util.Comparator;

public class ProductoPorCategoria implements Comparator<Producto> {
    @Override
    public int compare(Producto p1, Producto p2) {
        return p1.getCategoria().compareTo(p2.getCategoria());
    }
}