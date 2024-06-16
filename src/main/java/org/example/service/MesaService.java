package org.example.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Mesa;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MesaService {
    private static final String RUTA_JSON = "src/main/resources/mesas.json";


    public Map<Integer, Mesa> cargarDatosDesdeJson() {
        Map<Integer, Mesa>  mesas = new HashMap<>();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(RUTA_JSON)) {
            Type tipoLista = new TypeToken<List<Mesa>>() {}.getType();
            List<Mesa> listaMesas = gson.fromJson(reader, tipoLista);
            for (Mesa mesa : listaMesas) {
                mesas.put(mesa.getNumero(), mesa);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mesas;
    }
}
