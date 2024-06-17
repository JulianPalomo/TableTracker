package org.example.view.panels;

import org.example.models.Mesa;
import org.example.models.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

///Esta clase que es un PANEL se instancia cuando se presiona el boton mesa

public class PedidoPanel extends JPanel{
    private int numero;
    private ArrayList<Producto> productos = new ArrayList<>();
    private DefaultListModel<String> listModel;

    public PedidoPanel(Mesa mesa) {
        this.numero = mesa.getNumero();
        this.productos = mesa.getPedido().getListaProductos();
        this.listModel = new DefaultListModel<>();

        setTitle("Mesa " + numero);
        setSize(400, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JList<String> productList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(productList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addProductButton = new JButton("Agregar Producto");
        JButton billButton = new JButton("Facturar");

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });

        billButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                facturar();
            }
        });

        buttonPanel.add(addProductButton);
        buttonPanel.add(billButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void agregarProducto() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del Producto:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            String precioStr = JOptionPane.showInputDialog(this, "Precio del Producto:");
            try {
                double precio = Double.parseDouble(precioStr.trim());
                Producto producto = new Producto(nombre, precio);
                productos.add(producto);
                listModel.addElement(nombre + " - $" + precio);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Precio inválido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void facturar() {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.getPrecio();
        }
        JOptionPane.showMessageDialog(this, "Total a facturar: $" + total, "Factura", JOptionPane.INFORMATION_MESSAGE);
    }
}
