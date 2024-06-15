package org.example.interfaces;

import org.example.models.Categoria;

public abstract class Filtrable <T>{
    public boolean CumpleFiltro(T filtro) {
        return false;
    }

    public abstract boolean cumpleFiltro(Categoria categoria);
}
