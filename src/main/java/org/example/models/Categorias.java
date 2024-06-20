package org.example.models;

import java.util.HashSet;
import java.util.Set;

public class Categorias{
    private final Set<String> categorias = new HashSet<>();

    public Categorias() {
        // Agregar categorías predefinidas
        categorias.add("BEBIDA");
        categorias.add("BEBIDA_ALCOHOLICA");
        categorias.add("ENTRADA");
        categorias.add("PLATO_PRINCIPAL");
        categorias.add("POSTRE");
    }

    public Set<String> getCategorias() {
        return categorias;
    }

    public void agregarCategoria(String nuevaCategoria) {
        categorias.add(nuevaCategoria);
    }
}
