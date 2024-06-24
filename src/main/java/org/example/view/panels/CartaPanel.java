package org.example.view.panels;

import org.example.models.Producto;
import org.example.service.ProductoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class CartaPanel extends JFrame {
    private final ProductoService productoService = new ProductoService();
    private JTabbedPane tabbedPane;

    public CartaPanel() {
        setTitle("Carta de Productos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar componentes
        tabbedPane = new JTabbedPane();
//        tabbedPane.setTabPlacement(JTabbedPane. LEFT);
        cargarProductosEnTabs();

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Agregar Producto");
        JButton deleteCategoryButton = new JButton("Eliminar Categoría");
        JButton editButton = new JButton("Editar Producto");
        JButton deleteButton = new JButton("Eliminar Producto");
        JButton increaseButton = new JButton("Aumentar Precios");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteCategoryButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(increaseButton);

        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> agregarProducto());
        deleteCategoryButton.addActionListener(e -> eliminarCategoria());
        editButton.addActionListener(e -> editarProducto());
        deleteButton.addActionListener(e -> eliminarProducto());
        increaseButton.addActionListener(e -> aumentarPrecios());

        setVisible(true);
    }


    private void eliminarCategoria() {
        String categoriaAEliminar = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione la categoría a eliminar:",
                "Eliminar Categoría",
                JOptionPane.PLAIN_MESSAGE,
                null,
                productoService.obtenerCategorias().toArray(),
                null
        );

        if (categoriaAEliminar != null && !categoriaAEliminar.isEmpty()) {
            // Remove the tab from the JTabbedPane
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (tabbedPane.getTitleAt(i).equals(categoriaAEliminar)) {
                    tabbedPane.removeTabAt(i);
                    break;
                }
            }

            // Remove the category from the product service
            productoService.eliminarCategoria(categoriaAEliminar);
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
            String categoria = categoriaField.getText().toUpperCase();
            double precio = Double.parseDouble(precioField.getText());

            Producto nuevoProducto = new Producto(nombre, categoria, precio);
            productoService.agregarProducto(nuevoProducto);

            repaint();
//            // Check if the category tab exists, if not, add it
            List<String> categorias = productoService.obtenerCategorias();
            if(!categorias.contains(categoria)){
                agregarNuevaCategoriaAlTabbedPane(categoria);
            }
            agregarProductoATab(categoria,nuevoProducto);
        }
    }

    private void agregarNuevaCategoriaAlTabbedPane(String categoria) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Nombre", "Precio"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        tabbedPane.addTab(categoria, scrollPane);
    }

    private void agregarProductoATab(String categoria, Producto nuevoProducto) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(categoria)) {
                JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(i);
                JTable table = (JTable) scrollPane.getViewport().getView();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new Object[]{nuevoProducto.getNombre(), nuevoProducto.getPrecio()});
                break;
            }
        }
    }

    private void cargarProductosEnTabs() {
        tabbedPane.removeAll();
        List<String> categorias = productoService.obtenerCategorias();

        for (String categoria : categorias) {
            DefaultTableModel model = new DefaultTableModel(new Object[]{"Nombre", "Precio"}, 0);
            JTable table = new JTable(model);
            List<Producto> productos = productoService.filtrarProductosPorCategoria(categoria);
            for (Producto producto : productos) {
                model.addRow(new Object[]{producto.getNombre(), producto.getPrecio()});
            }
            JScrollPane scrollPane = new JScrollPane(table);
            tabbedPane.addTab(categoria, scrollPane);
        }
    }

    private void editarProducto() {
        int selectedTab = tabbedPane.getSelectedIndex();
        if (selectedTab != -1) {
            String categoriaSeleccionada = tabbedPane.getTitleAt(selectedTab);
            JTable table = (JTable) ((JScrollPane) tabbedPane.getComponentAt(selectedTab)).getViewport().getView();
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Producto productoSeleccionado = productoService.filtrarProductosPorCategoria(categoriaSeleccionada).get(selectedRow);

                JTextField nombreField = new JTextField(productoSeleccionado.getNombre(), 10);
                JTextField categoriaField = new JTextField(productoSeleccionado.getCategoria(), 10);
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
                    productoSeleccionado.setCategoria(categoriaField.getText().toUpperCase());
                    productoSeleccionado.setPrecio(Double.parseDouble(precioField.getText()));
                    productoService.actualizarProducto(productoSeleccionado);
                    cargarProductosEnTabs();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto para editar.", "Editar Producto", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void eliminarProducto() {
        int selectedTab = tabbedPane.getSelectedIndex();
        if (selectedTab != -1) {
            String categoriaSeleccionada = tabbedPane.getTitleAt(selectedTab);
            JTable table = (JTable) ((JScrollPane) tabbedPane.getComponentAt(selectedTab)).getViewport().getView();
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Producto productoSeleccionado = productoService.filtrarProductosPorCategoria(categoriaSeleccionada).get(selectedRow);
                productoService.eliminarProducto(productoSeleccionado);
                cargarProductosEnTabs();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto para eliminar.", "Eliminar Producto", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void aumentarPrecios() {
        String porcentajeStr = JOptionPane.showInputDialog(this,
                "Ingrese el porcentaje de aumento:",
                "Aumentar Precios", JOptionPane.PLAIN_MESSAGE);
        if (porcentajeStr != null && !porcentajeStr.isEmpty()) {
            double porcentaje = Double.parseDouble(porcentajeStr);
            productoService.aplicarAumento(porcentaje);
            cargarProductosEnTabs();
        }
    }

}

