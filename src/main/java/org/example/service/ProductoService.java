package org.example.service;

import com.google.gson.reflect.TypeToken;
import jdk.internal.foreign.SystemLookup;
import org.example.models.Producto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoService {

    private static final String RUTA_JSON = "src/main/resources/json/productos.json";


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

    public void guardarProductosJson(Map<String, List<Producto>> productos) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(productos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}