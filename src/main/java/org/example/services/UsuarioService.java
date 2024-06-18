package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private static final String FILE_PATH = "usuarios.json";
    private static ObjectMapper objectMapper = new ObjectMapper();

    // Método para leer usuarios desde el archivo JSON
    public static List<Usuario> readUsuarios() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // Si el archivo no existe, se retorna una lista vacía
            return new ArrayList<>();
        }

        // Leer el archivo y convertirlo en una lista de usuarios
        return objectMapper.readValue(file, new TypeReference<List<Usuario>>() {});
    }

    // Método para escribir usuarios en el archivo JSON
    public static void writeUsuarios(List<Usuario> usuarios) throws IOException {
        // Convertir la lista de usuarios a JSON y escribirla en el archivo
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), usuarios);
    }
}
