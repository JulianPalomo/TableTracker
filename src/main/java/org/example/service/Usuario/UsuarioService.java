package org.example.service.Usuario;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.example.models.personas.Credenciales;
import org.example.models.personas.Persona;
import org.example.models.personas.Usuario;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class UsuarioService {

    private static final String RUTA_JSON_PERSONAS = "src/main/java/org/example/resource/usuarios.json";
    private final Set<Usuario> usuarios;
    private Gson gson;

    public UsuarioService() {
        usuarios = new TreeSet<>(Comparator.comparing(Persona::getDni));
        gson = new GsonBuilder()
                .registerTypeAdapter(Usuario.class, new UsuarioDeserializer())
                .registerTypeAdapter(Usuario.class, new UsuarioSerializer())
                .create();
    }

    public void addUsuario(Usuario persona) {
        usuarios.add(persona);
    }

    public void saveToJson() throws IOException {
        try (FileWriter writer = new FileWriter(RUTA_JSON_PERSONAS)) {
            gson.toJson(usuarios, writer);
        }
    }

    public boolean existeUsuario(String dni) {
        for (Usuario usuario : usuarios) {
            if (usuario.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }

    public void loadFromJson() {
        try (FileReader reader = new FileReader(RUTA_JSON_PERSONAS)) {
            Type personaSetType = new TypeToken<Set<Usuario>>() {}.getType();
            Set<Usuario> loadedUsuarios = gson.fromJson(reader, personaSetType);

            usuarios.clear();
            usuarios.addAll(loadedUsuarios);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos de usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (JsonParseException e) {
            JOptionPane.showMessageDialog(null, "Error de formato JSON: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public Usuario login(String dni, String password) {
        for (Usuario persona : usuarios) {
            if (persona instanceof Usuario && ((Usuario) persona).login(dni, password)) {
                return persona; // Retorna el usuario encontrado
            }
        }
        return null; // Retorna null si no se encuentra ningún usuario con ese usuario y contraseña
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    // Nuevo método para obtener una lista de meseros
    public List<Usuario> getListaMeseros() {
        List<Usuario> meseros = new ArrayList<>();
        for (Usuario persona : usuarios) {
            if(persona.getCredenciales() == Credenciales.MESERO){
                meseros.add(persona);
            }
        }
        return meseros;
    }

    // Nuevo método para buscar una persona por DNI
    public Usuario buscarPorDni(String dni) {
        for (Usuario persona : usuarios) {
            if (persona.getDni().equals(dni)) {
                return persona;
            }
        }
        return null;
    }

    // Nuevo método para agregar un mesero
    public void agregarMesero(Usuario usuario) throws Exception {
        if (buscarPorDni(usuario.getDni()) != null) {
            throw new Exception("El DNI ya existe: " + usuario.getDni());
        }
        addUsuario(usuario);
    }

    // Nuevo método para eliminar una persona por DNI
    public void eliminarPorDni(String dni) throws Exception {
        Usuario persona = buscarPorDni(dni);
        if (persona == null) {
            throw new Exception("Usuario no encontrada con DNI: " + dni);
        }
        usuarios.remove(persona);
    }

}