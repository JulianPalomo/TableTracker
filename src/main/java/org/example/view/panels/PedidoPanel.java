package org.example.view.panels;

import org.example.interfaces.PedidoListener;
import org.example.models.*;
import org.example.service.ProductoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

///Esta clase que es un PANEL se instancia cuando se presiona el boton mesa

public class PedidoPanel extends JFrame implements PedidoListener {

    private int numero;
    private Pedido pedido;
    private DefaultListModel<String> listModel;
    private final ProductoService productoService;

    public PedidoPanel(Mesa mesa, ProductoService productoService) {
        this.numero = mesa.getId();
        if(mesa.getPedido() == null)
        {
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
/*
        billButton.addActionListener(new ActionListener() {
           /@Override
            public void actionPerformed(ActionEvent e) {
                facturar(mesa.getPedido());
            }
        });
        */


        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto(productoService.cargarMenu());
            }
        });

        buttonPanel.add(addProductButton);
        buttonPanel.add(billButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateOrderList();
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
/*
    private void facturar(Pedido pedido) {
        FacturaPanel facturaPanel = new FacturaPanel(pedido);
    }

 */

}

