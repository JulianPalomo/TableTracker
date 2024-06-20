package org.example.view.panels;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.example.models.Producto;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

///esto se supone que es la vista de productos y pedido
public class MenuLoader {


    public Map<String, List<Producto>> cargarMenu(String rutaArchivo) {
        try (FileReader reader = new FileReader(rutaArchivo)) {
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
}