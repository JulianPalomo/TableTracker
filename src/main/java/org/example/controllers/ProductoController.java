package org.example.controllers;
import org.example.models.Categoria;
import org.example.models.Producto;
import org.example.service.ProductoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductoController {
    private final Map<String, List<Producto>> productos;
    private final ProductoService productoService;

    public ProductoController() {
        this.productos = ProductoService.cargarProductosDesdeJson(); // Inicializar el mapa de productos según el tipo de Map
        this.productoService = new ProductoService();
        cargarProductosDesdeJson(); // Cargar productos desde el archivo JSON al inicializar el controller
    }


    public void agregarProducto(Producto producto) {
        String categoria = String.valueOf(producto.getCategoria());
        List<Producto> productosCategoria = productos.getOrDefault(categoria, List.of());
        productosCategoria.add(producto);
        productos.put(categoria, productosCategoria);
        productoService.guardarProductosJson(productos);
    }

    public void eliminarProducto(Producto producto) {
        String categoria = String.valueOf(producto.getCategoria());
        List<Producto> productosCategoria = productos.getOrDefault(categoria, List.of());
        productosCategoria.remove(producto);
        productos.put(categoria, productosCategoria);
        productoService.guardarProductosJson(productos);
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

    private void cargarProductosDesdeJson() {
        Map<String, List<Producto>> productosDesdeJson = ProductoService.cargarProductosDesdeJson();
        if (productosDesdeJson != null) {
            productos.putAll(productosDesdeJson);
        }
    }
}