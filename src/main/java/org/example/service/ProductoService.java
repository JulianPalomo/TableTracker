package org.example.service;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.example.models.Categoria;
import org.example.models.Producto;
import java.util.*;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

///Esta clase proporciona todos los metodos necesarios para la gestion la lista de productos. Metodos que seran llamados en los botones del sistema

public class ProductoService {

    private static final String RUTA_JSON = "src/main/resources/json/productos.json";
    private final Map<String, List<Producto>> productos = new HashMap<>();


    public ProductoService() {
        cargarMenu();
    }


    public void agregarProducto(Producto producto) {
        String categoria = String.valueOf(producto.getCategoria());
        List<Producto> productosCategoria = productos.getOrDefault(categoria, List.of());
        productosCategoria.add(producto);
        productos.put(categoria, productosCategoria);
        guardarProductosJson(productos);
    }

    public void eliminarProducto(Producto producto) {
        String categoria = String.valueOf(producto.getCategoria());
        List<Producto> productosCategoria = productos.getOrDefault(categoria, List.of());
        productosCategoria.remove(producto);
        productos.put(categoria, productosCategoria);
        guardarProductosJson(productos);
    }

    public Producto buscarProductoPorNombre(String nombre) {
        return productos.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }


    public List<Producto> buscarProductosPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();
        for (List<Producto> productos : productos.values()) {
            for (Producto producto : productos) {
                if (producto.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                    productosEncontrados.add(producto);
                }
            }
        }
        return productosEncontrados;
    }

    public List<Producto> filtrarProductosPorCategoria(Categoria categoria) {
        return productos.getOrDefault(categoria, List.of());
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productos.values().stream()
                .flatMap(List::stream)
                .toList();
    }
    

    /*
    public static Map<String, List<Producto>> cargarProductosDesdeJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(RUTA_JSON)) {
            Type tipo = new TypeToken<Map<String, List<Producto>>>() {}.getType();
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    */

    public Map<String, List<Producto>> cargarMenu() {
        try (FileReader reader = new FileReader(RUTA_JSON)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            Type productoListType = new TypeToken<List<Producto>>() {}.getType();
            List<Producto> productos = gson.fromJson(jsonObject.get("productos"), productoListType);
            return organizarPorCategoria(productos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, List<Producto>> organizarPorCategoria(List<Producto> productos) {
        Map<String, List<Producto>> menu = new LinkedHashMap<>();
        for (Producto producto : productos) {
            menu.computeIfAbsent(String.valueOf(producto.getCategoria()), k -> new ArrayList<>()).add(producto);
        }
        return menu;
    }

    public void guardarProductosJson(Map<String, List<Producto>> productos) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(productos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}