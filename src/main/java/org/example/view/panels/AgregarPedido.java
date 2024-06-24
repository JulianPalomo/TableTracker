package org.example.view.panels;

import org.example.interfaces.PedidoListener;
import org.example.models.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgregarPedido extends JFrame {
    private ArrayList<Producto> pedido;

    public AgregarPedido(Map<String, List<Producto>> menu, PedidoListener listener, int nroMesa) {
        setTitle("Agregar productos a Mesa " + nroMesa);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        pedido = new ArrayList<>();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane. LEFT);

        for (Map.Entry<String, List<Producto>> entry : menu.entrySet()) {
            String categoria = entry.getKey();
            List<Producto> productos = entry.getValue();

            JPanel panel = new JPanel(new GridLayout(0, 5, 10, 10)); // 5 columnas: Nombre, Precio, Spinner, Observacion, Botón
            for (Producto producto : productos) {
                JLabel nameLabel = new JLabel(producto.getNombre());
                JLabel priceLabel = new JLabel(String.valueOf(producto.getPrecio()));
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Default 1, min 1, max 100, step 1
                JTextField observacionField = new JTextField(); // Campo para agregar observación
                JButton addButton = getAddButton(producto, spinner, observacionField);

                panel.add(nameLabel);
                panel.add(priceLabel);
                panel.add(spinner);
                panel.add(observacionField);
                panel.add(addButton);
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            tabbedPane.addTab(categoria, scrollPane);
        }

        add(tabbedPane, BorderLayout.CENTER);

        // Add a button to confirm the order
        JButton confirmButton = getConfirmButton(listener);
        add(confirmButton, BorderLayout.SOUTH);
    }

    private JButton getConfirmButton(PedidoListener listener) {
        JButton confirmButton = new JButton("Confirmar Pedido");

        confirmButton.addActionListener(e -> {
            ///Envia informacion a mesasPanel de que el pedido fue actualizado
            listener.onPedidoActualizado(pedido);
            StringBuilder resumen = new StringBuilder("Pedido confirmado:\n");
            for (Producto producto : pedido) {
                resumen.append(producto.getNombre()).append(" - ").append(producto.getPrecio()).append(" - Observación: ").append(producto.getObservacion()).append("\n");
            }
            JOptionPane.showMessageDialog(null, resumen.toString());
            dispose();
        });
        return confirmButton;
    }

    private  JButton getAddButton(Producto producto, JSpinner spinner, JTextField observacionField) {
        JButton addButton = new JButton("Añadir");

        addButton.addActionListener(e -> {
            int cantidad = (int) spinner.getValue();
            String observacion = observacionField.getText(); // Obtiene la observación

            for (int i = 0; i < cantidad; i++) {
                Producto productoClonado = producto.clone(); // Clona el producto
                productoClonado.setObservacion(observacion); // Setea observación en el clon
                pedido.add(productoClonado); // Añade el clon a la lista de pedidos
            }
        });
        return addButton;
    }
}