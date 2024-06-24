package org.example.service;

import com.google.gson.*;
import org.example.models.Administrador;
import org.example.models.Persona;

import java.lang.reflect.Type;

public class PersonaSerializer implements JsonSerializer<Persona> {
    @Override
    public JsonElement serialize(Persona persona, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", persona.getClass().getSimpleName()); // Agrega el nombre de la clase como type

        // Agrega los campos específicos de cada tipo de Persona
        jsonObject.addProperty("nombre", persona.getNombre());
        jsonObject.addProperty("apellido", persona.getApellido());
        jsonObject.addProperty("dni", persona.getDni());

        if (persona instanceof Administrador) {
            jsonObject.addProperty("password", ((Administrador) persona).getPassword());
        }

        return jsonObject;
    }
}