package org.example;

import org.example.models.Factura;
import org.example.models.FacturaNew;
import org.example.models.MetodosdePAgo;

import javax.swing.*;
import java.util.Map;

public class Main extends JFrame {
    public static void main(String[] args) {

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