package org.example.service.Usuario;

import com.google.gson.*;
import org.example.models.personas.Credenciales;
import org.example.models.personas.Persona;
import org.example.models.personas.Usuario;

import java.lang.reflect.Type;

public class UsuarioDeserializer implements JsonDeserializer<Persona> {

    @Override
    public Persona deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String nombre = jsonObject.get("nombre").getAsString();
        String apellido = jsonObject.get("apellido").getAsString();
        String dni = jsonObject.get("dni").getAsString();
        String password = jsonObject.get("password").getAsString();

        // Convertir el JsonElement a Credenciales
        String credencialesStr = jsonObject.get("credenciales").getAsString();
        Credenciales credenciales;

        try {
            credenciales = Credenciales.valueOf(credencialesStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Credenciales desconocidas: " + credencialesStr);
        }

        return new Usuario(nombre, apellido, dni, password, credenciales);
    }
}
