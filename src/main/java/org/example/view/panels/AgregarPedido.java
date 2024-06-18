package org.example.view.panels;

import org.example.models.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarPedido extends JFrame {
        private ArrayList<Producto> pedido;

        public AgregarPedido(Map<String, List<Producto>> menu) {
            setTitle("Menú del Restaurante");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            pedido = new ArrayList<Producto>();
            JTabbedPane tabbedPane = new JTabbedPane();

            for (Map.Entry<String, List<Producto>> entry : menu.entrySet()) {
                String categoria = entry.getKey();
                List<Producto> productos = entry.getValue();

                JPanel panel = new JPanel(new GridLayout(0, 4, 10, 10)); // 4 columns: Nombre, Precio, Spinner, Botón
                for (Producto producto : productos) {
                    JLabel nameLabel = new JLabel(producto.getNombre());
                    JLabel priceLabel = new JLabel(String.valueOf(producto.getPrecio()));
                    JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Default 1, min 1, max 100, step 1
                    JButton addButton = new JButton("Añadir");

                    addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int cantidad = (int) spinner.getValue();
                            for (int i = 0; i < cantidad; i++) {
                                pedido.add(producto);
                            }
                            JOptionPane.showMessageDialog(null, cantidad + " " + producto.getNombre() + "(s) añadido(s) al pedido.");
                        }
                    });

                    panel.add(nameLabel);
                    panel.add(priceLabel);
                    panel.add(spinner);
                    panel.add(addButton);
                }

                JScrollPane scrollPane = new JScrollPane(panel);
                tabbedPane.addTab(categoria, scrollPane);
            }

            add(tabbedPane, BorderLayout.CENTER);

            // Add a button to confirm the order
            JButton confirmButton = new JButton("Confirmar Pedido");
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Aquí puedes manejar el pedido, por ejemplo, enviarlo a otra clase o imprimirlo.
                    // Por ahora, solo vamos a mostrar un mensaje de los productos pedidos.
                    StringBuilder resumen = new StringBuilder("Pedido confirmado:\n");
                    for (Producto producto : pedido) {
                        resumen.append(producto.getNombre()).append(" - ").append(producto.getPrecio()).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, resumen.toString());
                }
            });

            add(confirmButton, BorderLayout.SOUTH);
        }
}
