package org.example.interfaces;

import org.example.models.Producto;
import java.util.List;

public interface PedidoListener {
    void onPedidoActualizado(List<Producto> nuevosProductos);
}

