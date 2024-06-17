package org.example.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Mesa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

///Esta clase proporciona todos los metodos necesarios para la gestion de las mesas. Metodos que seran llamados en los botones del sistema

public class MesaService {

    private static final String RUTA_JSON = "src/main/resources/mesas.json";
    private ArrayList<Mesa> mesas = new ArrayList<>();

    public MesaService() {
        mesas = new ArrayList<>();
        // cargarDatosDesdeJson();
    }

    public ArrayList<Mesa> getMesas() {
        return mesas;
    }

    public void cargarDatosDesdeJson() {
        Gson gson = new Gson(); // Inicializa el objeto Gson para trabajar con JSON
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_JSON))) {
            // Define el tipo de la lista de Mesas
            Type tipoLista = new TypeToken<List<Mesa>>() {}.getType();
            // Lee y convierte el JSON a una lista de objetos Mesa
            List<Mesa> listaMesas = gson.fromJson(reader, tipoLista);
            // Recorre la lista y añade todas
            mesas.addAll(listaMesas);
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de excepciones en caso de error de lectura
        }
    }

    ///Esta funcion es la que se llama en el panel de mesas (boton que agrega mesa)
    public int agregarMesa ()
    {
        this.mesas.add(new Mesa());
        return this.mesas.size(); //para retornar el numero de la mesa agregada
    }

    ///Funcion que sera llamada al tocar el boton de una mesa
    public Mesa buscarMesa(int numeroMesa)
    {
        if(this.mesas != null) {
            return mesas.get(numeroMesa - 1); // va a ser la mesa
        }
        return null;
    }
}
