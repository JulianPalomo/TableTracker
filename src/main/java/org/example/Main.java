package org.example;

import org.example.models.Categoria;
import org.example.models.Producto;
import org.example.sistema.Gestor;
import org.example.sistema.Sistema;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        List<Producto> productos = sistema.cargarDatosDesdeJson("C:\\Users\\Lucrecia\\Desktop\\TableTracker\\src\\main\\java\\org\\example\\resource\\productos.json", Producto.class);

        // Ahora puedes instanciar el Gestor de productos y cargar los productos
        Gestor<Categoria, Producto> gestorProductos = new Gestor<>(productos);

        // Ejemplo de cómo usar el gestor de productos
        Producto cocaCola = gestorProductos.buscar(Categoria.BEBIDA);
        if (cocaCola != null) {
            System.out.println("Producto encontrado: " + cocaCola);
        } else {
            System.out.println("Producto no encontrado.");
        }
    }
}