package org.example.interfaces;

import org.example.models.Producto;

import java.util.ArrayList;
import java.util.List;

public interface PedidoListener {
    void onPedidoActualizado(ArrayList<Producto> nuevosProductos);
}

