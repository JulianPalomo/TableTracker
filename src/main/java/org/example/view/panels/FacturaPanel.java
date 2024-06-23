package org.example.view.panels;

import org.example.models.Factura;
import org.example.models.MetodosDePago;
import org.example.models.Pedido;
import org.example.models.Producto;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

import org.example.service.FacturaService;

import javax.swing.table.DefaultTableModel;


public class FacturaPanel extends JFrame {

    private JPanel panel1;
    private JTable tablaProductos;
    private JComboBox<MetodosDePago> mediosDePago;
    private JLabel subtotalLabel;
    private JButton botonFactura;
    private DefaultTableModel tablaModel;
    private ArrayList<Producto> productos;

    private FacturaService facturaService = new FacturaService();

    public FacturaPanel(Pedido pedido) {
        this.productos = pedido.getListaProductos();

        setTitle("Lista de Productos");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel1 = new JPanel();
        tablaModel = new DefaultTableModel();
        tablaModel.addColumn("ID");
        tablaModel.addColumn("Nombre");
        tablaModel.addColumn("Categoría");
        tablaModel.addColumn("Precio");

        tablaProductos = new JTable(tablaModel);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        subtotalLabel = new JLabel("Subtotal: $0.0");
        mediosDePago = new JComboBox<>(MetodosDePago.values());
        botonFactura = new JButton("Imprimir Factura");

        for (Producto producto : productos) {
            Object[] row = {producto.getId(), producto.getNombre(), producto.getCategoria(), producto.getPrecio()};
            tablaModel.addRow(row);
        }

        panel1.setLayout(new BorderLayout());
        panel1.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayout(3, 1));
        panelInferior.add(subtotalLabel);
        panelInferior.add(mediosDePago);
        panelInferior.add(botonFactura);

        panel1.add(panelInferior, BorderLayout.SOUTH);

        add(panel1);

        subtotalLabel.setText("Subtotal: $" + pedido.getTotal());

        botonFactura.addActionListener(e -> {
            try {
                generarFactura(pedido);
            } catch (DocumentException | FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    private void generarFactura(Pedido pedido) throws DocumentException, FileNotFoundException {
        Factura factura = new Factura(pedido, (MetodosDePago) mediosDePago.getSelectedItem());
        facturaService.generarFactura(factura);
        JOptionPane.showMessageDialog(this, "Factura generada correctamente.");
        abrirArchivoPDF(factura.getNombreArchivo());
    }

    private void abrirArchivoPDF(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(this, "Apertura automática no soportada en este sistema.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no se encuentra: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + e.getMessage());
        }
    }
}
