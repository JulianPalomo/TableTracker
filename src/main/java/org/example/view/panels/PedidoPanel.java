package org.example.view.panels;

import org.example.exceptions.ProductosYaComandadosException;
import org.example.interfaces.PedidoListener;
import org.example.models.*;
import org.example.service.ProductoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

///Esta clase que es un PANEL se instancia cuando se presiona el boton mesa

public class PedidoPanel extends JFrame implements PedidoListener {

    private int numero;
    private Pedido pedido;
    private DefaultTableModel tableModel;
    private final ProductoService productoService;
    private ArrayList<Producto> comanda = new ArrayList<>();
    private JLabel mesero;

    public PedidoPanel(Mesa mesa, ProductoService productoService, MesasPanel mesasPanel) {
        this.numero = mesa.getNroMesa();
        if (mesa.getPedido() == null) {
            mesa.setPedido(new Pedido());
        }
        this.pedido = mesa.getPedido();
        this.productoService = productoService;

        setTitle("Mesa " + numero);
        setSize(700, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel(new Object[]{"Nombre", "Precio", "Observación"}, 0);
        JTable productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addProductButton = new JButton("Agregar Producto");
        JButton removeProductButton = new JButton("Eliminar Producto");
        JButton billButton = new JButton("Facturar");
        JButton botonComanda = new JButton("Comandar");

        billButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FacturaPanel(pedido);
                mesa.liberarMesa();
                mesasPanel.actualizarColorMesas();
            }
        });

        addProductButton.addActionListener(e -> {
            agregarProducto(productoService.cargarCarta(),mesa.getNroMesa());
            if(pedido != null){
                mesa.ocuparMesa();
                mesasPanel.actualizarColorMesas(); // Notificar al MesasPanel para actualizar el color
            }
        });

        removeProductButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                String productName = (String) tableModel.getValueAt(selectedRow, 0);
                double productPrice = (double) tableModel.getValueAt(selectedRow, 1);
                String productObservation = (String) tableModel.getValueAt(selectedRow, 2);

                // Buscar el producto en el pedido y eliminarlo
                for (Producto producto : pedido.getListaProductos()) {
                    if (producto.getNombre().equals(productName) &&
                            producto.getPrecio() == productPrice &&
                            producto.getObservacion().equals(productObservation)) {
                        pedido.getListaProductos().remove(producto);
                        break;
                    }
                }
                tableModel.removeRow(selectedRow);

                // Verificar si el pedido está vacío y liberar la mesa si es necesario
                if (pedido.getListaProductos().isEmpty()) {
                    mesa.liberarMesa();
                    mesasPanel.actualizarColorMesas();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un producto para eliminar.");
            }

        });

        botonComanda.addActionListener(e -> {
            try {
                comandar(comanda, mesa.getNroMesa());
                JOptionPane.showMessageDialog(null, "Pedido comandado");
            } catch (ProductosYaComandadosException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        buttonPanel.add(addProductButton);
        buttonPanel.add(removeProductButton);
        buttonPanel.add(billButton);
        buttonPanel.add(botonComanda);

        // Añadir la etiqueta del mesero al panel de botones
        mesero = new JLabel("Mesero: " + (mesa.getMesero() != null ? mesa.getMesero().toString() : "No asignado"));
        buttonPanel.add(mesero, BorderLayout.WEST);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actualizar la lista de pedidos
        updateOrderList();
    }

    public void comandar(ArrayList<Producto> comanda, Mesa mesa) throws ProductosYaComandadosException {
        // Crear un mapa para almacenar la cantidad de cada producto en el pedido
        Map<Producto, Integer> cantidadesPedido = new HashMap<>();
        for (Producto producto : pedido.getListaProductos()) {
            int cantidad = cantidadesPedido.getOrDefault(producto, 0) + 1;
            cantidadesPedido.put(producto, cantidad);
        }

        // Crear una lista para almacenar los nuevos productos y sus cantidades
        Map<Producto, Integer> nuevosProductosConCantidades = new HashMap<>();

        // Recorrer la lista de productos del pedido
        for (Map.Entry<Producto, Integer> entry : cantidadesPedido.entrySet()) {
            Producto productoPedido = entry.getKey();
            int cantidadPedido = entry.getValue();

            // Verificar la cantidad del producto en la comanda
            int cantidadComanda = 0;
            for (Producto productoComanda : comanda) {
                if (productoPedido.equals(productoComanda)) {
                    cantidadComanda++;
                }
            }

            // Calcular la cantidad de productos nuevos
            int cantidadNuevos = cantidadPedido - cantidadComanda;

            // Si hay nuevos productos para este producto, agregarlo al mapa
            if (cantidadNuevos > 0) {
                nuevosProductosConCantidades.put(productoPedido, cantidadNuevos);
            }
        }

        // Si no hay nuevos productos por comandar, lanzar excepción
        if (nuevosProductosConCantidades.isEmpty()) {
            throw new ProductosYaComandadosException("Todos los productos del pedido ya han sido comandados.");
        }

        // Si hay nuevos productos, generar el PDF y actualizar la comanda
        productoService.imprimirComandaConCantidades(nuevosProductosConCantidades, mesa.getNroMesa());

        for (Map.Entry<Producto, Integer> entry : nuevosProductosConCantidades.entrySet()) {
            Producto productoNuevo = entry.getKey();
            int cantidadNueva = entry.getValue();
            for (int i = 0; i < cantidadNueva; i++) {
                comanda.add(productoNuevo);
            }
        }

        // Actualizar el nombre del mesero en la etiqueta correspondiente
        actualizarNombreMesero(mesa);
    }

    private void agregarProducto(Map<String, List<Producto>> menu, int nroMesa) {
        AgregarPedido agregarPedido = new AgregarPedido(menu, this, nroMesa);
        agregarPedido.setVisible(true);
    }

    private void updateOrderList() {
        tableModel.setRowCount(0); // Limpiar la tabla
        for (Producto producto : pedido.getListaProductos()) {
            tableModel.addRow(new Object[]{producto.getNombre(), producto.getPrecio(), producto.getObservacion()});
        }
    }

    @Override
    public void onPedidoActualizado(ArrayList<Producto> nuevosProductos) {
        this.pedido.agregarProducto(nuevosProductos);
        updateOrderList();
    }
/*
    private void facturar(Pedido pedido) {
        FacturaPanel facturaPanel = new FacturaPanel(pedido);
    }

 */

}

