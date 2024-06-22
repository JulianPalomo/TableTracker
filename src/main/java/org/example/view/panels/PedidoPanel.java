package org.example.view.panels;

import org.example.interfaces.PedidoListener;
import org.example.models.*;
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

public class PedidoPanel extends JFrame implements PedidoListener {

    private int numero;
    private Pedido pedido;
    private DefaultTableModel tableModel;
    private final ProductoService productoService;

    public PedidoPanel(Mesa mesa, ProductoService productoService,MesasPanel mesasPanel) {
        this.numero = mesa.getId();
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

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto(productoService.cargarCarta());
                if(pedido != null){
                    mesa.ocuparMesa();
                    mesasPanel.actualizarColorMesas(); // Notificar al MesasPanel para actualizar el color
                }
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        buttonPanel.add(addProductButton);
        buttonPanel.add(removeProductButton);
        buttonPanel.add(billButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actualizar la lista de pedidos
        updateOrderList();
    }

    private void agregarProducto(Map<String, List<Producto>> menu) {
        AgregarPedido agregarPedido = new AgregarPedido(menu, this);
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
}
