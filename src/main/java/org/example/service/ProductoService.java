package org.example.service;

import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.models.Producto;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import org.example.models.Producto;

///Esta clase proporciona todos los metodos necesarios para la gestion la lista de carta. Metodos que seran llamados en los botones del sistema

public class ProductoService {

    private static final String RUTA_JSON = "src/main/java/org/example/resource/carta.json";
    private Map<String, List<Producto>> carta = new LinkedHashMap<>();
    private final CategoriaService categoriaService = new CategoriaService();
    private Document documentoSwing;

    public ProductoService() {
        carta = cargarCarta();
    }

    ///NUEVAS FUNCIONES PARA CARTA PANEL
    public void agregarProducto(Producto producto) {
        List<Producto> productosCategoria = carta.computeIfAbsent(producto.getCategoria(), k -> new ArrayList<>());
        productosCategoria.add(producto);
        guardarCartaJson();
    }

    public void eliminarProducto(Producto producto) {
        List<Producto> productos = carta.get(producto.getCategoria());
        if (productos != null) {
            productos.remove(producto);
            guardarCartaJson();
        }
    }

    public void guardarCartaJson() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(carta, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void aplicarAumento(double porcentaje) {
        for (List<Producto> productosCategoria : carta.values()) {
            for (Producto producto : productosCategoria) {
                double nuevoPrecio = producto.getPrecio() * (1 + porcentaje / 100);
                producto.setPrecio(nuevoPrecio);
            }
        }
        guardarCartaJson();
    }

    public void actualizarProducto(Producto producto) {
        List<Producto> productosCategoria = carta.get(producto.getCategoria());
        if (productosCategoria != null) {
            for (int i = 0; i < productosCategoria.size(); i++) {
                if (productosCategoria.get(i).getId() == producto.getId()) {
                    productosCategoria.set(i, producto);
                    guardarCartaJson();
                    break;
                }
            }
        }
    }

    public List<Producto> obtenerTodosLosProductos() {
        return carta.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    public Map<String, List<Producto>> cargarCarta() {
        Map<String, List<Producto>> cartaCargada = new LinkedHashMap<>();
        try (FileReader reader = new FileReader(RUTA_JSON)) {
            Gson gson = new Gson();
            Type cartaType = new TypeToken<Map<String, List<Producto>>>() {
            }.getType();
            cartaCargada = gson.fromJson(reader, cartaType);
            for (String categoria : cartaCargada.keySet()) {
                categoriaService.agregarCategoria(categoria);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cartaCargada;
    }

    public void agregarCategoria(String categoria) {
        categoriaService.agregarCategoria(categoria);
        carta.put(categoria, new ArrayList<>());
        guardarCartaJson();
    }

    public List<String> obtenerCategorias() {
        return categoriaService.getCategorias().stream().toList();
    }

    public void eliminarCategoria(String categoria) {
        carta.remove(categoria);
        guardarCartaJson();
    }


    /////
    public Producto buscarProductoPorNombre(String nombre) {
        return carta.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public List<Producto> buscarProductosPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();
        for (List<Producto> carta : carta.values()) {
            for (Producto producto : carta) {
                if (producto.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                    productosEncontrados.add(producto);
                }
            }
        }
        return productosEncontrados;
    }

    public List<Producto> filtrarProductosPorCategoria(String categoria) {
        return new ArrayList<>(carta.getOrDefault(categoria, new ArrayList<>()));
    }

    public void imprimirComandaConCantidades(Map<Producto, Integer> nuevosProductosConCantidades, int numeroMesa) {
        // Configurar tamaño de página personalizado
        Rectangle pageSize = new Rectangle(216, 720); // Tamaño personalizado (por ejemplo, recibo pequeño)
        Document document = new Document(pageSize, 10, 10, 10, 10);

        try {
            // Crear un escritor de PDF
            PdfWriter.getInstance(document, new FileOutputStream("comanda" + numeroMesa + ".pdf"));
            document.open();

            // Agregar el título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph title = new Paragraph("Comanda", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Agregar número de mesa y horario
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String fechaHora = formatter.format(date);

            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Paragraph mesaInfo = new Paragraph("Mesa: " + numeroMesa + "\nFecha y Hora: " + fechaHora, normalFont);
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
            System.out.println("Comanda generada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}