package org.example.view.panels;

import org.example.exceptions.ProductosYaComandadosException;
import org.example.interfaces.PedidoListener;
import org.example.models.*;
import org.example.service.ProductoService;

import javax.swing.*;
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
    private DefaultListModel<String> listModel;
    private final ProductoService productoService;
    private ArrayList<Producto> comanda = new ArrayList<>();

    public PedidoPanel(Mesa mesa, ProductoService productoService, MesasPanel mesasPanel) {
        this.numero = mesa.getId();
        if(mesa.getPedido() == null) {
            mesa.setPedido(new Pedido());
        }
        this.pedido = mesa.getPedido();
        this.productoService = productoService;
        this.listModel = new DefaultListModel<>();

        setTitle("Mesa " + numero);
        setSize(700, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JList<String> productList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(productList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addProductButton = new JButton("Agregar Producto");
        JButton billButton = new JButton("Facturar");
        JButton botonComanda = new JButton("Comandar");

        billButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FacturaPanel(pedido);
                mesa.liberarMesa();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto(productoService.cargarCarta());
                if(pedido.getListaProductos() != null) {
                    mesa.ocuparMesa();
                    mesasPanel.actualizarColorMesas();
                }
            }
        });

        botonComanda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    comandar(comanda, mesa.getId());
                    JOptionPane.showMessageDialog(null, "Pedido comandado");
                } catch (ProductosYaComandadosException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        buttonPanel.add(addProductButton);
        buttonPanel.add(billButton);
        buttonPanel.add(botonComanda);
        add(buttonPanel, BorderLayout.SOUTH);

        updateOrderList();
    }
    public void comandar(ArrayList<Producto> comanda, int nroMesa) throws ProductosYaComandadosException {
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
        productoService.imprimirComandaConCantidades(nuevosProductosConCantidades, nroMesa);

        for (Map.Entry<Producto, Integer> entry : nuevosProductosConCantidades.entrySet()) {
            Producto productoNuevo = entry.getKey();
            int cantidadNueva = entry.getValue();
            for (int i = 0; i < cantidadNueva; i++) {
                comanda.add(productoNuevo);
            }
        }
    }

    private void agregarProducto(Map<String, List<Producto>> menu) {
        AgregarPedido agregarPedido = new AgregarPedido(menu, this);
        agregarPedido.setVisible(true);
    }

    private void updateOrderList() {
        listModel.clear();
        for (Producto producto : pedido.getListaProductos()) {
            listModel.addElement(producto.getNombre() + " - $" + producto.getPrecio());
        }
    }

    @Override
    public void onPedidoActualizado(ArrayList<Producto> nuevosProductos) {
        this.pedido.agregarProducto(nuevosProductos);
        updateOrderList();
    }

}