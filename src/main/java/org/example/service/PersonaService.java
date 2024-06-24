package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.example.models.Administrador;
import org.example.models.Mesero;
import org.example.models.Persona;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
public class PersonaService {

    private static final String RUTA_JSON_PERSONAS = "src/main/java/org/example/resource/personas.json";
    private final Set<Persona> personas;
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

    // Nuevo método para obtener una lista de meseros
    public List<Mesero> getListaMeseros() {
        List<Mesero> meseros = new ArrayList<>();
        for (Persona persona : personas) {
            if (persona instanceof Mesero) {
                meseros.add((Mesero) persona);
            }
        }
        return meseros;
    }

    // Nuevo método para buscar una persona por DNI
    public Persona buscarPorDni(String dni) {
        for (Persona persona : personas) {
            if (persona.getDni().equals(dni)) {
                return persona;
            }
        }
        return null;
    }

    // Nuevo método para agregar un mesero
    public void agregarMesero(Mesero mesero) throws Exception {
        if (buscarPorDni(mesero.getDni()) != null) {
            throw new Exception("El DNI ya existe: " + mesero.getDni());
        }
        addPersona(mesero);
    }

    // Nuevo método para eliminar una persona por DNI
    public void eliminarPorDni(String dni) throws Exception {
        Persona persona = buscarPorDni(dni);
        if (persona == null) {
            throw new Exception("Persona no encontrada con DNI: " + dni);
        }
        personas.remove(persona);
    }
}