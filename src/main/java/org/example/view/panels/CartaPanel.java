package org.example.view.panels;

import org.example.models.Categoria;
import org.example.models.Producto;
import org.example.service.ProductoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartaPanel extends JFrame {
    private final ProductoService productoService = new ProductoService();
    private DefaultTableModel tableModel;
    private JTable productTable;

    public CartaPanel() {
        setTitle("Carta de Productos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar componentes
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Categoría");
        tableModel.addColumn("Precio");
        cargarProductos();
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Agregar Producto");
        JButton addCategoryButton = new JButton("Agregar Categoría");
        JButton editButton = new JButton("Editar Producto");
        JButton deleteButton = new JButton("Eliminar Producto");
        JButton increaseButton = new JButton("Aumentar Precios");

        buttonPanel.add(addButton);
        buttonPanel.add(addCategoryButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(increaseButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> agregarProducto());
       // addCategoryButton.addActionListener(e -> agregarCategoria());
        editButton.addActionListener(e -> editarProducto());
        deleteButton.addActionListener(e -> eliminarProducto());
        increaseButton.addActionListener(e -> aumentarPrecios());

        setVisible(true);
    }
/*
    private void agregarCategoria() {
        String nuevaCategoria = JOptionPane.showInputDialog(this,
                "Ingrese el nombre de la nueva categoría:",
                "Agregar Categoría", JOptionPane.PLAIN_MESSAGE);
        if (nuevaCategoria != null && !nuevaCategoria.isEmpty()) {
            productoService.agregarCategoria(nuevaCategoria.toUpperCase());
            // Actualizar la lista de categorías en la tabla
            productTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox<>(Categoria.values())));
            productTable.repaint();
        }
    }*/

    private void cargarProductos() {
        tableModel.setRowCount(0);
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        for (Producto producto : productos) {
            Object[] row = {producto.getNombre(), producto.getCategoria(), producto.getPrecio()};
            tableModel.addRow(row);
        }
    }

    private void agregarProducto() {
        JTextField nombreField = new JTextField(10);
        JTextField categoriaField = new JTextField(10);
        JTextField precioField = new JTextField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(Box.createHorizontalStrut(15)); // a spacer
        panel.add(new JLabel("Categoría:"));
        panel.add(categoriaField);
        panel.add(Box.createHorizontalStrut(15)); // a spacer
        panel.add(new JLabel("Precio:"));
        panel.add(precioField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            Categoria categoria = Categoria.valueOf(categoriaField.getText().toUpperCase());
            double precio = Double.parseDouble(precioField.getText());
            Producto nuevoProducto = new Producto(nombre, categoria, precio);
            productoService.agregarProducto(nuevoProducto);
            Object[] row = {nuevoProducto.getNombre(), nuevoProducto.getCategoria(), nuevoProducto.getPrecio()};
            tableModel.addRow(row);
        }
    }

    private void editarProducto() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            Producto productoSeleccionado = productoService.obtenerTodosLosProductos().get(selectedRow);

            JTextField nombreField = new JTextField(productoSeleccionado.getNombre(), 10);
            JTextField categoriaField = new JTextField(productoSeleccionado.getCategoria().toString(), 10);
            JTextField precioField = new JTextField(String.valueOf(productoSeleccionado.getPrecio()), 10);

            JPanel panel = new JPanel();
            panel.add(new JLabel("Nombre:"));
            panel.add(nombreField);
            panel.add(Box.createHorizontalStrut(15)); // a spacer
            panel.add(new JLabel("Categoría:"));
            panel.add(categoriaField);
            panel.add(Box.createHorizontalStrut(15)); // a spacer
            panel.add(new JLabel("Precio:"));
            panel.add(precioField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Editar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                productoSeleccionado.setNombre(nombreField.getText());
                productoSeleccionado.setCategoria(Categoria.valueOf(categoriaField.getText().toUpperCase()));
                productoSeleccionado.setPrecio(Double.parseDouble(precioField.getText()));
                productoService.actualizarProducto(productoSeleccionado);
                tableModel.setValueAt(productoSeleccionado.getNombre(), selectedRow, 0);
                tableModel.setValueAt(productoSeleccionado.getCategoria(), selectedRow, 1);
                tableModel.setValueAt(productoSeleccionado.getPrecio(), selectedRow, 2);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto para editar.", "Editar Producto", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarProducto() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que quieres eliminar este producto?",
                    "Eliminar Producto", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                Producto productoSeleccionado = productoService.obtenerTodosLosProductos().get(selectedRow);
                productoService.eliminarProducto(productoSeleccionado);
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto para eliminar.", "Eliminar Producto", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void aumentarPrecios() {
        String input = JOptionPane.showInputDialog(this,
                "Ingrese el porcentaje de aumento:",
                "Aumentar Precios", JOptionPane.PLAIN_MESSAGE);
        if (input != null) {
            try {
                double porcentaje = Double.parseDouble(input);
                productoService.aplicarAumento(porcentaje);
                cargarProductos();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Porcentaje inválido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
