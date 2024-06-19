package org.example.view.panels;

import org.example.models.Factura;
import org.example.models.MetodosDePago;
import org.example.models.Pedido;
import org.example.models.Producto;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import org.example.service.FacturaService;

public class FacturaPanel extends JFrame {

        private JPanel panel1;
        private JList<Producto> listaProductos;
        private JComboBox<String> MediosDePago;
        private JLabel subtotalLabel;
        private JButton BotonFactura;
        private DefaultListModel<Producto> listaModel;
        private ArrayList<Producto> productos;

        private FacturaService facturaService = new FacturaService();

        public FacturaPanel(Pedido pedido) {
            this.productos = pedido.getListaProductos();

            setTitle("Lista de Productos");
            setSize(400, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Inicializar componentes
            panel1 = new JPanel();
            listaModel = new DefaultListModel<>();
            listaProductos = new JList<>(listaModel);
            subtotalLabel = new JLabel("Subtotal: $0.0");

            JComboBox<MetodosDePago> comboBox = new JComboBox<>();
            comboBox.setModel(new DefaultComboBoxModel<>(MetodosDePago.values()));

            BotonFactura = new JButton("Imprimir Factura");

            // Llenar la lista con productos
            for (Producto producto : productos) {
                listaModel.addElement(producto);
            }

            // Configurar el panel
            panel1.setLayout(new BorderLayout());
            panel1.add(new JScrollPane(listaProductos), BorderLayout.CENTER);

            JPanel panelInferior = new JPanel();
            panelInferior.setLayout(new GridLayout(3, 1));
            panelInferior.add(subtotalLabel);
            panelInferior.add(MediosDePago);
            panelInferior.add(BotonFactura);

            panel1.add(panelInferior, BorderLayout.SOUTH);

            // Añadir el panel principal al frame
            add(panel1);

            //Calcular el subtotal inicial
            subtotalLabel.setText("Subtotal: $" + factura.getPedido().getTotal());

            // Añadir listener para el botón de generar factura
            BotonFactura.addActionListener(e -> {
                try {
                   generarFactura();
                } catch (DocumentException | FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            setVisible(true);
        }

        private void generarFactura(Pedido pedido) throws DocumentException, FileNotFoundException {
            Factura nuevaFactura = new Factura()
            JOptionPane.showMessageDialog(this, "Factura generada correctamente: " + nombreArchivo);
            facturaService.generarFactura(factura);
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
