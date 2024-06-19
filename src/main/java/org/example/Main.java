package org.example;

import org.example.models.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class Main extends JFrame {
    public static void main(String[] args) {

        Pedido pedido = new Pedido();
        ArrayList<Producto> prods = new ArrayList<>();
        prods =
        pedido.setListaProductos(prods);
            SwingUtilities.invokeLater(() -> {
                Map<String, Double> productos = Map.of(
                        "Producto 1", 10.0,
                        "Producto 2", 20.0,
                        "Producto 3", 30.0
                );
                new FacturaNew(productos);
            });
    }
}