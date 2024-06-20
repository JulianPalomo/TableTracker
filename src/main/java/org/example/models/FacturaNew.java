package org.example.models;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FacturaNew extends JFrame {

    private JPanel panel1;
    private JList<Producto> listaProductos;
    private JComboBox<String> MediosDePago;
    private JLabel subtotalLabel;
    private JButton BotonFactura;
    private DefaultListModel<Producto> listaModel;
    private ArrayList<Producto> productos;

    public FacturaNew(Factura factura) {
        this.productos = factura.getPedido().getListaProductos();

        setTitle("Lista de Productos");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar componentes
        panel1 = new JPanel();
        listaModel = new DefaultListModel<>();
        listaProductos = new JList<>(listaModel);
        subtotalLabel = new JLabel("Subtotal: $0.0");
        
        JComboBox<MetodosdePAgo> comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(MetodosdePAgo.values()));
        
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

        // Calcular el subtotal inicial
        subtotalLabel.setText("Subtotal: $" + factura.getPedido().getTotal());

        // Añadir listener para el botón de generar factura
        BotonFactura.addActionListener(e -> {
            try {
                generarFactura(factura);
            } catch (DocumentException | FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    private void generarFactura(Factura factura) throws DocumentException, FileNotFoundException {
        String medioPago = MediosDePago.getSelectedItem().toString();
        String nombreArchivo = "Factura_" + factura.getId() + ".pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));

        document.open();
        document.add(new Paragraph("Factura:"));

        for (Producto producto : productos) {
            document.add(new Paragraph(producto.getNombre() + ": $" + producto.getPrecio()));
        }

        document.add(new Paragraph(subtotalLabel.getText()));
        document.add(new Paragraph("Medio de Pago: " + medioPago));
        document.close();

        JOptionPane.showMessageDialog(this, "Factura generada correctamente: " + nombreArchivo);
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
