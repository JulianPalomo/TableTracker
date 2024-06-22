package org.example.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.example.models.Factura;
import org.example.models.Mesa;

import java.awt.*;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.pdf.PdfWriter;
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
        Document document = new Document(); // Tamaño de página personalizado
        PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));

        document.open();

        // Información del restaurante
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph title = new Paragraph("RESTAURANTE EL CAPITAN", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Paragraph info = new Paragraph("Caños de Meca Gestion SL\nCIF: B-70227519\n", infoFont);
        info.setAlignment(Element.ALIGN_CENTER);
        document.add(info);

        // Información de la factura
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String fechaHora = formatter.format(date);

        Paragraph facturaInfo = new Paragraph(new StringBuilder().append("Factura: ").append(factura.getId()).append("\nFecha: ").append(fechaHora).toString());
        facturaInfo.setAlignment(Element.ALIGN_LEFT);
        facturaInfo.setSpacingBefore(10);
        document.add(facturaInfo);

        // Crear una tabla para los productos
        PdfPTable table = new PdfPTable(2); // 3 columnas: Cantidad, Descripción, Total
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        // Añadir encabezados de la tabla


        PdfPCell header1 = new PdfPCell(new Phrase("Descripción", infoFont));
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header1);

        PdfPCell header2 = new PdfPCell(new Phrase("Total", infoFont));
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header2);

        // Añadir productos y sus precios a la tabla
        for (Producto producto : factura.getPedido().getListaProductos()) {
            table.addCell(new PdfPCell(new Phrase(producto.getNombre(), infoFont)));
            table.addCell(new PdfPCell(new Phrase("$" + producto.getPrecio(), infoFont)));
        }

        // Añadir la tabla al documento
        document.add(table);

        // Calcular totales
        double baseIVA = factura.getPedido().getTotal() / 1.10; // Suponiendo que el IVA es del 10%
        double iva = factura.getPedido().getTotal() - baseIVA;

        // Añadir total
        Paragraph totalParagraph = new Paragraph("Base IVA 10%: $" + String.format("%.2f", baseIVA) +
                "\nIVA 10%: $" + String.format("%.2f", iva) +
                "\nTotal: $" + String.format("%.2f", factura.getPedido().getTotal()), infoFont);
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        // Añadir método de pago
        Paragraph metodoPago = new Paragraph("Medio de Pago: " + factura.getMetodoDePago(), infoFont);
        metodoPago.setAlignment(Element.ALIGN_RIGHT);
        metodoPago.setSpacingBefore(10);
        document.add(metodoPago);

        document.close();
    }

    public ArrayList<Factura> getFacturasGeneradas() {
        return facturasGeneradas;
    }
}