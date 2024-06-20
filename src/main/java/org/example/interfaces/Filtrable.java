package org.example.interfaces;

import org.example.models.Categoria;

public interface Filtrable <U>{
    boolean cumpleFiltro(U criterio);
}
