package org.example.service.Usuario;

import com.google.gson.*;
import org.example.models.personas.Persona;
import org.example.models.personas.Usuario;

import java.lang.reflect.Type;

public class UsuarioSerializer implements JsonSerializer<Persona> {
    @Override
    public JsonElement serialize(Persona persona, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", persona.getClass().getSimpleName()); // Agrega el nombre de la clase como type

        // Agrega los campos específicos de cada tipo de Persona
        jsonObject.addProperty("nombre", persona.getNombre());
        jsonObject.addProperty("apellido", persona.getApellido());
        jsonObject.addProperty("dni", persona.getDni());

        if (((Usuario)persona).getPassword() != null) {
            jsonObject.addProperty("password", ((Usuario)persona).getPassword());
        }

        if (((Usuario)persona).getCredenciales() != null) {
            jsonObject.addProperty("credenciales", ((Usuario)persona).getCredenciales().name());
        }

        return jsonObject;
    }
}
