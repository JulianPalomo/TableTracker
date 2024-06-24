package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.example.models.Administrador;
import org.example.models.Persona;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.TreeSet;

public class PersonaService {

    private static final String RUTA_JSON_PERSONAS = "src/main/java/org/example/resource/personas.json";
    private Set<Persona> personas;
    private Gson gson;

    public PersonaService() {
        personas = new TreeSet<>((p1, p2) -> p1.getDni().compareTo(p2.getDni()));
        gson = new GsonBuilder()
                .registerTypeAdapter(Persona.class, new PersonaDeserializer())
                .registerTypeAdapter(Persona.class, new PersonaSerializer())
                .create();
    }

    public void addPersona(Persona persona) {
        personas.add(persona);
    }

    public void saveToJson() throws IOException {
        try (FileWriter writer = new FileWriter(RUTA_JSON_PERSONAS)) {
            gson.toJson(personas, writer);
        }
    }

    /*
    public void loadFromJson() throws IOException {
        try (FileReader reader = new FileReader(RUTA_JSON_PERSONAS)) {
            Type personaSetType = new TypeToken<Set<Persona>>() {}.getType();
            Set<Persona> loadedPersonas = gson.fromJson(reader, personaSetType);
            personas.clear();
            personas.addAll(loadedPersonas);
        }
    }
    */
    public void loadFromJson() {
        try (FileReader reader = new FileReader(RUTA_JSON_PERSONAS)) {
            Type personaSetType = new TypeToken<Set<Persona>>() {}.getType();
            Set<Persona> loadedPersonas = gson.fromJson(reader, personaSetType);

            System.out.println("Datos cargados desde JSON:");
            for (Persona persona : loadedPersonas) {
                System.out.println(persona.toString());
            }

            personas.clear();
            personas.addAll(loadedPersonas);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos de usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (JsonParseException e) {
            JOptionPane.showMessageDialog(null, "Error de formato JSON: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean adminLogin(String dni, String password) {
        for (Persona persona : personas) {
            if (persona instanceof Administrador) {
                if (((Administrador) persona).login(dni, password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Persona> getPersonas() {
        return personas;
    }
}