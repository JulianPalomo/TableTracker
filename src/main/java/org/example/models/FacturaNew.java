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
import java.util.List;
import java.util.Map;

public class FacturaNew extends JFrame {
    private JPanel panel1;
    private JList<String> Productos;
    private JComboBox<String> MediosDPAgo;
    private JLabel subtotalLabel;
    private JButton BotonFactura;
    private DefaultListModel<String> listaModel;
    private Map<String, Double> productosMap;

    public FacturaNew(Map<String, Double> productos) {
        this.productosMap = productos;

        setTitle("Lista de Productos");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar componentes
        panel1 = new JPanel();
        listaModel = new DefaultListModel<>();
        Productos = new JList<>(listaModel);
        subtotalLabel = new JLabel("Subtotal: $0.0");
        MediosDPAgo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta de Crédito", "Tarjeta de Débito"});
        BotonFactura = new JButton("Imprimir Factura");

        // Llenar la lista con productos
        for (String producto : productosMap.keySet()) {
            listaModel.addElement(producto);
        }

        // Configurar el panel
        panel1.setLayout(new BorderLayout());
        panel1.add(new JScrollPane(Productos), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayout(3, 1));
        panelInferior.add(subtotalLabel);
        panelInferior.add(MediosDPAgo);
        panelInferior.add(BotonFactura);

        panel1.add(panelInferior, BorderLayout.SOUTH);

        // Añadir el panel principal al frame
        add(panel1);

        // Calcular el subtotal inicial
        calcularSubtotal();

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

    private void calcularSubtotal() {
        double subtotal = 0.0;
        for (Double precio : productosMap.values()) {
            subtotal += precio;
        }
        subtotalLabel.setText("Subtotal: $" + subtotal);
    }

    private void generarFactura() throws DocumentException, FileNotFoundException {
        String medioPago = MediosDPAgo.getSelectedItem().toString();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Factura.pdf"));

        document.open();
        document.add(new Paragraph("Factura:"));
        for (Map.Entry<String, Double> entry : productosMap.entrySet()) {
            document.add(new Paragraph(entry.getKey() + ": $" + entry.getValue()));
        }
        document.add(new Paragraph(subtotalLabel.getText()));
        document.add(new Paragraph("Medio de Pago: " + medioPago));
        document.close();

        JOptionPane.showMessageDialog(this, "Factura generada correctamente");
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
