package org.example.view;

import org.example.exceptions.ProductosYaComandadosException;
import org.example.interfaces.PedidoListener;
import org.example.models.*;
import org.example.models.mesas.Comanda;
import org.example.models.mesas.Mesa;
import org.example.models.mesas.Pedido;
import org.example.models.personas.Credenciales;
import org.example.service.MesaService;
import org.example.service.ProductoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

///Esta clase que es un PANEL se instancia cuando se presiona el boton mesa

public class PedidoView extends JFrame implements PedidoListener {

    private int numero;
    private Pedido pedido;
    private DefaultTableModel tableModel;
    private final ProductoService productoService;
    private Comanda comanda;
    private JLabel mesero;
    private Timer comandaTimer;
    private Timer facturaTimer;
    private boolean comandado;
    private boolean facturado;
    private JButton botonComanda;
    private JButton billButton;

    public PedidoView(Mesa mesa, ProductoService productoService, MesasView mesasView, Credenciales credenciales) {
        this.numero = mesa.getNroMesa();
        if (mesa.getPedido() == null) {
            mesa.setPedido(new Pedido());
        }
        this.pedido = mesa.getPedido();
        this.productoService = productoService;
        comanda = new Comanda(productoService);

        comandado = true;

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
        billButton = new JButton("Facturar");
        botonComanda = new JButton("Comandar");
        JButton botonLiberarMesa = new JButton("Liberar Mesa");

        botonLiberarMesa.addActionListener(e -> {
            mesa.liberarMesa();
            mesasView.actualizarColorMesas();
            MesaService.getInstance().guardarMesasYParedesJSON();
            dispose();
        });

        billButton.addActionListener(e -> {
            new FacturaView(mesa, mesasView);
            mesasView.actualizarColorMesas();
            facturado = true;
            facturaTimer.stop(); // Detener el temporizador cuando se factura con éxito
            billButton.setBackground(null); // Restaurar color del botón
        });

        addProductButton.addActionListener(e -> {
            agregarProducto(productoService.cargarCarta(), mesa.getNroMesa());
            if (pedido != null) {
                mesa.ocuparMesa();
                mesasView.actualizarColorMesas(); // Notificar al MesasPanel para actualizar el color
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
                    mesasView.actualizarColorMesas();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un producto para eliminar.");
            }

        });

        botonComanda.addActionListener(e -> {
            try {
                comanda.comandar(mesa);
                JOptionPane.showMessageDialog(null, "Pedido comandado");
                comandado = true;
                comandaTimer.stop(); // Detener el temporizador cuando se comanda con éxito
                botonComanda.setBackground(null); // Restaurar color del botón
            } catch (ProductosYaComandadosException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        buttonPanel.add(addProductButton);
        if (credenciales == Credenciales.ADMINISTRADOR || credenciales == Credenciales.CAJERO) {
            buttonPanel.add(removeProductButton);
        }


        buttonPanel.add(billButton);
        buttonPanel.add(botonComanda);
        buttonPanel.add(botonLiberarMesa);

        // Añadir la etiqueta del mesero al panel de botones
        mesero = new JLabel("Mesero: " + (mesa.getMesero() != null ? mesa.getMesero().toString() : "No asignado"));
        buttonPanel.add(mesero, BorderLayout.WEST);

        add(buttonPanel, BorderLayout.SOUTH);

        // Inicializar el temporizador para verificar si hay productos sin comandar
        comandado = false;
        facturado = false;
        comandaTimer = new Timer(500, new ActionListener() {
            private boolean colorFlag = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!comandado && !pedido.getListaProductos().isEmpty()) {
                    botonComanda.setBackground(colorFlag ? new Color(255, 200, 200) : null);
                    colorFlag = !colorFlag;
                }
            }
        });

        facturaTimer = new Timer(500, new ActionListener() {
            private boolean colorFlag = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!facturado && !pedido.getListaProductos().isEmpty()) {
                    billButton.setBackground(colorFlag ? new Color(200, 255, 200) : null);
                    colorFlag = !colorFlag;
                }
            }
        });

        // No iniciar los temporizadores aquí, se iniciarán al agregar un producto

        // Actualizar la lista de pedidos
        updateOrderList();
    }

    private void agregarProducto(Map<String, List<Producto>> menu, int nroMesa) {
        AgregarPedidoView agregarPedidoView = new AgregarPedidoView(menu, this, nroMesa);
        agregarPedidoView.setVisible(true);
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
        comandado = false; // Resetear el estado cuando se actualiza el pedido
        facturado = false; // Resetear el estado cuando se actualiza el pedido

        // Iniciar los temporizadores si hay productos en el pedido
        if (!pedido.getListaProductos().isEmpty()) {
            comandaTimer.start();
            facturaTimer.start();
        }
    }
}
