package org.example.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.Utils.LocalDateAdapter;
import org.example.models.Mesa;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MesaService {

    private static final String RUTA_JSON = "src/main/java/org/example/resource/mesas.json";
    private ArrayList<Mesa> mesas = new ArrayList<>();

    public ArrayList<Mesa> getMesas() {
        return this.mesas;
    }

    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void guardarMesasEnJson() {
        Gson gson = createGson();
        try (FileWriter writer = new FileWriter(RUTA_JSON)) {
            gson.toJson(mesas, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void cargarMesasJson() {
        Gson gson = createGson();
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_JSON))) {
            Type tipoLista = new TypeToken<List<Mesa>>() {}.getType();
            List<Mesa> listaMesas = gson.fromJson(reader, tipoLista);
            mesas.clear();
            mesas.addAll(listaMesas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Mesa agregarMesa() {
        Mesa nueva = new Mesa();
        this.mesas.add(nueva);
        return nueva;
    }

    public Mesa buscarMesa(int numeroMesa) {
        if (this.mesas != null && numeroMesa <= this.mesas.size()) {
            return mesas.get(numeroMesa - 1);
        }
        return null;
    }

    public void eliminarUltimaMesa() {
        if (!mesas.isEmpty()) {
            mesas.remove(mesas.size() - 1);
            Mesa.decrementarNumeroAuto();
        } else {
            throw new IllegalStateException("No hay mesas para eliminar");
        }
    }
}
