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

///Esta clase proporciona todos los metodos necesarios para la gestion la lista de carta. Metodos que seran llamados en los botones del sistema

public class ProductoService {

    private static final String RUTA_JSON = "src/main/java/org/example/resource/carta.json";
    private Map<Categoria, List<Producto>> carta = new LinkedHashMap<>();


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
        List<Producto> productosCategoria = carta.get(producto.getCategoria());
        if (productosCategoria != null) {
            productosCategoria.remove(producto);
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

    public List<Producto> filtrarProductosPorCategoria(Categoria categoria) {
        return carta.getOrDefault(categoria, List.of());
    }

    public <K, V> List<V> searchInLinkedHashMap(@org.jetbrains.annotations.NotNull LinkedHashMap<K, V> map, K key) {
        List<V> resultList = new ArrayList<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                resultList.add(entry.getValue());
            }
        }
        return resultList;
    }

    public List<Producto> obtenerTodosLosProductos() {
        return carta.values().stream()
                .flatMap(List::stream)
                .toList();
    }


    public Map<Categoria, List<Producto>> cargarCarta() {
        Map<Categoria, List<Producto>> cartaCargada = new LinkedHashMap<>();
        try (FileReader reader = new FileReader(RUTA_JSON)) {
            Gson gson = new Gson();
            Type cartaType = new TypeToken<Map<Categoria, List<Producto>>>() {}.getType();
            cartaCargada = gson.fromJson(reader, cartaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cartaCargada;
    }


    public Map<Categoria, List<Producto>> cargarMenu() {
        try (FileReader reader = new FileReader(RUTA_JSON)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            Type productoListType = new TypeToken<List<Producto>>() {}.getType();
            List<Producto> carta = gson.fromJson(jsonObject.get("carta"), productoListType);
            return organizarPorCategoria(carta);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<Categoria, List<Producto>> organizarPorCategoria(List<Producto> carta) {
        Map<Categoria, List<Producto>> menu = new LinkedHashMap<>();
        for (Producto producto : carta) {
            menu.computeIfAbsent(Categoria.valueOf(String.valueOf(producto.getCategoria())), k -> new ArrayList<>()).add(producto);
        }
        return menu;
    }


    /*
    public void agregarProducto(Producto producto) {
        Categoria categoria = String.valueOf(producto.getCategoria());
        List<Producto> productosCategoria = carta.getOrDefault(categoria, List.of());
        productosCategoria.add(producto);
        carta.put(categoria, productosCategoria);
        guardarProductosJson(carta);
    }




    public void eliminarProducto(Producto producto) {
        String categoria = String.valueOf(producto.getCategoria());
        List<Producto> productosCategoria = carta.getOrDefault(categoria, List.of());
        productosCategoria.remove(producto);
        carta.put(categoria, productosCategoria);
        guardarProductosJson(carta);
    }

 */

}