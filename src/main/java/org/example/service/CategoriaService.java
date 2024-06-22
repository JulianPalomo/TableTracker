package org.example.service;

import java.util.HashSet;
import java.util.Set;

public class CategoriaService {
    private final Set<String> categorias = new HashSet<>();

    public CategoriaService() {
    }

    public Set<String> getCategorias() {
        return categorias;
    }

    public void agregarCategoria(String nuevaCategoria) {
        if (nuevaCategoria != null && !nuevaCategoria.trim().isEmpty()) {
            categorias.add(nuevaCategoria.toUpperCase());
        }
    }
}
