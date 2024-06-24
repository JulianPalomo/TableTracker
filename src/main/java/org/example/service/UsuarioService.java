package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
/*
public class UsuarioService {
    private static final String FILE_PATH = "src/main/java/org/example/resource/usuarios.json";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Usuario.class, new UsuarioDeserializer())
            .create();

    // Método para leer usuarios desde el archivo JSON
    public static List<Usuario> readUsuarios() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // Si el archivo no existe, se retorna una lista vacía
            return new ArrayList<>();
        }

        // Leer el archivo y convertirlo en una lista de usuarios
        try (FileReader reader = new FileReader(file)) {
            Type userListType = new TypeToken<List<Usuario>>() {}.getType();
            return gson.fromJson(reader, userListType);
        }
    }

    // Método para escribir usuarios en el archivo JSON
    public static void writeUsuarios(List<Usuario> usuarios) throws IOException {
        // Convertir la lista de usuarios a JSON y escribirla en el archivo
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(usuarios, writer);
        }
    }
}

 */