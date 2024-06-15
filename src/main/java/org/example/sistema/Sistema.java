package org.example.sistema;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controllers.ProductoController;
import org.example.models.Categoria;
import org.example.models.Producto;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Sistema {
    private final ProductoController productoController = new ProductoController();


    public void agregarProducto(Producto nuevoProducto) {

        productoController.agregarProducto(nuevoProducto);
        //corroborar la excepcion de que el prodcuto ya exista (dos prodcutos son iguales si tienen el mismo nombre)
    }

    public List<Producto> productosPorCategoria(Categoria categoria) {
        return productoController.filtrarProductosPorCategoria(categoria);
    }

    public void buscarProductoPorNombre(String nombre) {
        Producto producto = productoController.buscarProductoPorNombre(nombre);
        if (producto != null) {
            System.out.println("Producto encontrado:");
            System.out.println(producto);
        } else {
            System.out.println("Producto no encontrado con nombre: " + nombre);
        }
    }

    public void mostrarTodosLosProductos() {
        List<Producto> productos = productoController.obtenerTodosLosProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            System.out.println("Todos los productos:");
            for (Producto producto : productos) {
                System.out.println(producto);
            }
        }
}

