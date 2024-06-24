package org.example.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.Utils.LocalDateAdapter;
import org.example.models.Mesa;
import org.example.models.Pared;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MesaService {

    private static final String RUTA_MESAS = "src/main/java/org/example/resource/mesas.json";
    private static final String RUTA_PAREDES = "src/main/java/org/example/resource/paredes.json";

    private ArrayList<Mesa> mesas = new ArrayList<>();
    private ArrayList<Pared> paredes  = new ArrayList<Pared>();

    public ArrayList<Mesa> getMesas() {
        return this.mesas;
    }
    public ArrayList<Pared> getParedes() {
        return this.paredes;
    }

    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void guardarMesasYParedesJSON() {
        Gson gson = createGson();
        try (FileWriter mesasWriter = new FileWriter(RUTA_MESAS);
             FileWriter paredesWriter = new FileWriter(RUTA_PAREDES)) {

            gson.toJson(mesas, mesasWriter);
            gson.toJson(paredes, paredesWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarMesasYParedesJSON() {
        Gson gson = createGson();
        try (BufferedReader mesasReader = new BufferedReader(new FileReader(RUTA_MESAS));
             BufferedReader paredesReader = new BufferedReader(new FileReader(RUTA_PAREDES))) {

            Type tipoListaMesas = new TypeToken<List<Mesa>>() {}.getType();
            List<Mesa> listaMesas = gson.fromJson(mesasReader, tipoListaMesas);
            mesas.clear();
            mesas.addAll(listaMesas);

            Type tipoListaParedes = new TypeToken<List<Pared>>() {}.getType();
            List<Pared> listaParedes = gson.fromJson(paredesReader, tipoListaParedes);
            paredes.clear();
            paredes.addAll(listaParedes);

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
            mesas.removeLast();
            Mesa.decrementarNumeroAuto();
        } else {
            throw new IllegalStateException("No hay mesas para eliminar");
        }
    }

    public Pared agregarPared() {
        Pared nueva = new Pared();
        this.paredes.add(nueva);
        return nueva;
    }

    public Pared buscarPared(int numeroPared) {
        if (this.paredes != null && numeroPared <= this.paredes.size()) {
            return paredes.get(numeroPared - 1);
        }
        return null;
    }

    public void eliminarUltimaPared() {
        if (!paredes.isEmpty()) {
            paredes.removeLast();
            Pared.decrementarNumeroAuto();
        } else {
            throw new IllegalStateException("No hay paredes para eliminar");
        }
    }




}
