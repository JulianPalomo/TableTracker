package org.example.service;

import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.models.Producto;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
}