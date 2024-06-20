package org.example.service;
import org.example.models.Factura;
import org.example.models.MetodosDePago;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.example.models.Producto;

public class FacturaService {

     public double calcularSubtotal(List<Producto> productosList) {
            double subtotal = 0.0;
            for (Producto producto : productosList) {
                subtotal += producto.getPrecio();
            }
            return subtotal;
        }

        public void generarFactura(int nroMesa, MetodosDePago medioPago, List<Producto> productosList) throws DocumentException, FileNotFoundException {
            String fileName = fileNameFecha();
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();
            document.add(new Paragraph("Factura: " + fileName));
            document.add(new Paragraph("Mesa N°: " + nroMesa));
            for (Producto producto : productosList) {
                document.add(new Paragraph(producto.getNombre() + ": $" + producto.getPrecio()));
            }
            document.add(new Paragraph("Subtotal: $" + calcularSubtotal(productosList)));
            document.add(new Paragraph("Medio de Pago: " + medioPago));
            document.close();

            abrirArchivoPDF(fileName);
        }

        private String fileNameFecha() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            return "Factura" + LocalDateTime.now().format(formatter) + ".pdf";
        }

        private void abrirArchivoPDF(String filePath) {
            try {
                File file = new File(filePath);
                if (file.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        System.out.println("Apertura automática no soportada en este sistema.");
                    }
                } else {
                    System.out.println("El archivo no se encuentra: " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al abrir el archivo: " + e.getMessage());
            }
        }
    }
   /* public void generarFactura(Factura factura) throws DocumentException, FileNotFoundException {
        //String medioDePago = String.valueOf(factura.getMetodoDePago());
        String nombreArchivo = "F" + factura.getId() + ".pdf";

        // Create PDF file
        crearPDF(factura, nombreArchivo);

        // Add invoice to ArrayList
        facturasGeneradas.add(factura);
    }

    private void crearPDF(Factura factura, String nombreArchivo) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));

        document.open();
        document.add(new Paragraph("Factura:"));

        for (Producto producto : factura.getPedido().getListaProductos()) {
            document.add(new Paragraph(producto.getNombre() + ": $" + producto.getPrecio()));
        }

        Label subtotalLabel = null; //me lo pide el IDE
        document.add(new Paragraph(subtotalLabel.getText()));
        document.add(new Paragraph("Medio de Pago: " + factura.getMetodoDePago()));
        document.close();
    }

    public ArrayList<Factura> getFacturasGeneradas() {
        return facturasGeneradas;
    }
    */

