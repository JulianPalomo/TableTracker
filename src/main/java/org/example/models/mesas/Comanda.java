package org.example.models.mesas;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.exceptions.ProductosYaComandadosException;
import org.example.models.Producto;
import org.example.service.ProductoService;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.time.format.DateTimeFormatter;

public class Comanda {
    private ArrayList<Producto> productos;
    private ProductoService productoService;

    public Comanda(ProductoService productoService) {
        this.productos = new ArrayList<>();
        this.productoService = productoService;
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
    }

    public void comandar(Mesa mesa) throws ProductosYaComandadosException {
        // Crear un mapa para almacenar la cantidad de cada producto en el pedido
        Map<Producto, Integer> cantidadesPedido = new HashMap<>();
        for (Producto producto : mesa.getPedido().getListaProductos()) {
            int cantidad = cantidadesPedido.getOrDefault(producto, 0) + 1;
            cantidadesPedido.put(producto, cantidad);
        }

        // Crear un mapa para almacenar la cantidad de cada producto en la comanda
        Map<Producto, Integer> cantidadesComanda = new HashMap<>();
        for (Producto producto : productos) {
            int cantidad = cantidadesComanda.getOrDefault(producto, 0) + 1;
            cantidadesComanda.put(producto, cantidad);
        }

        // Crear un mapa para los nuevos productos que necesitan ser comandados
        Map<Producto, Integer> nuevosProductosConCantidades = new HashMap<>();

        // Recorrer la lista de productos del pedido
        for (Map.Entry<Producto, Integer> entry : cantidadesPedido.entrySet()) {
            Producto productoPedido = entry.getKey();
            int cantidadPedido = entry.getValue();

            // Obtener la cantidad ya comandada de este producto
            int cantidadComanda = cantidadesComanda.getOrDefault(productoPedido, 0);

            // Calcular la cantidad de productos nuevos a comandar
            int cantidadNuevos = cantidadPedido - cantidadComanda;

            // Si hay nuevos productos para este producto, agregarlo al mapa
            if (cantidadNuevos > 0) {
                nuevosProductosConCantidades.put(productoPedido, cantidadNuevos);
            }
        }

        // Si no hay nuevos productos por comandar, lanzar excepción
        if (nuevosProductosConCantidades.isEmpty()) {
            throw new ProductosYaComandadosException("Todos los productos del pedido ya han sido comandados.");
        }

        // Si hay nuevos productos, generar el PDF y actualizar la comanda
        imprimirComandaConCantidades(nuevosProductosConCantidades, mesa.getNroMesa(), mesa.getMesero().getNombre());

        // Actualizar la comanda con los nuevos productos
        for (Map.Entry<Producto, Integer> entry : nuevosProductosConCantidades.entrySet()) {
            Producto productoNuevo = entry.getKey();
            int cantidadNueva = entry.getValue();
            for (int i = 0; i < cantidadNueva; i++) {
                productos.add(productoNuevo);
            }
        }
    }

    public void imprimirComandaConCantidades(Map<Producto, Integer> nuevosProductosConCantidades, int numeroMesa, String mesero) {
        // Configurar tamaño de página personalizado
        Rectangle pageSize = new Rectangle(216, 720); // Tamaño personalizado (por ejemplo, recibo pequeño)
        Document document = new Document(pageSize, 10, 10, 10, 10);

        try {
            // Formatear la fecha y hora actual para el nombre del archivo
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);

            // Crear la carpeta "Comandas" si no existe
            File comandaDir = new File("Comandas");
            if (!comandaDir.exists()) {
                comandaDir.mkdir();
            }

            // Crear un escritor de PDF
            String filePath = "Comandas/Mesa_" + numeroMesa + "-" + timestamp + "_" + ".pdf";
            // Crear un escritor de PDF
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Agregar el título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph title = new Paragraph("Comanda", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Agregar número de mesa y horario
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String fechaHora = dateFormatter.format(date);

            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Paragraph mesaInfo = new Paragraph("Mesa: " + numeroMesa + "\nMesero: " + mesero + "\nFecha y Hora: " + fechaHora, normalFont);
            mesaInfo.setAlignment(Element.ALIGN_LEFT);
            mesaInfo.setSpacingBefore(10);
            document.add(mesaInfo);

            // Crear una tabla para los productos
            PdfPTable table = new PdfPTable(3); // Añadimos una columna más para observaciones
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Añadir encabezados de la tabla
            PdfPCell header1 = new PdfPCell(new Phrase("Producto", normalFont));
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header1);

            PdfPCell header2 = new PdfPCell(new Phrase("Cantidad", normalFont));
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header2);

            PdfPCell header3 = new PdfPCell(new Phrase("Observaciones", normalFont));
            header3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header3);

            // Añadir productos, cantidades y observaciones a la tabla
            for (Map.Entry<Producto, Integer> entry : nuevosProductosConCantidades.entrySet()) {
                Producto producto = entry.getKey();
                Integer cantidad = entry.getValue();

                PdfPCell productCell = new PdfPCell(new Phrase(producto.getNombre(), normalFont));
                productCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(productCell);

                PdfPCell quantityCell = new PdfPCell(new Phrase(cantidad.toString(), normalFont));
                quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(quantityCell);

                PdfPCell observationCell = new PdfPCell(new Phrase(producto.getObservacion(), normalFont));
                observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(observationCell);
            }

            // Añadir la tabla al documento
            document.add(table);

            // Añadir total de artículos
            int totalArticulos = nuevosProductosConCantidades.values().stream().mapToInt(Integer::intValue).sum();
            Paragraph totalArticulosParagraph = new Paragraph("Total de artículos: " + totalArticulos, normalFont);
            totalArticulosParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalArticulosParagraph);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }
}


