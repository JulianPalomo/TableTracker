package org.example.service;

import org.example.models.Factura;
import org.example.models.Mesa;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import org.example.models.Producto;

public class FacturaService {
    private ArrayList<Factura> facturasGeneradas = new ArrayList<>();

    public void generarFactura(Factura factura) throws DocumentException, FileNotFoundException {
        String nombreArchivo = factura.getNombreArchivo();

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

        document.add(new Paragraph("Subtotal: $" + factura.getPedido().getTotal()));
        document.add(new Paragraph("Medio de Pago: " + factura.getMetodoDePago()));
        document.close();
    }

    public ArrayList<Factura> getFacturasGeneradas() {
        return facturasGeneradas;
    }
}