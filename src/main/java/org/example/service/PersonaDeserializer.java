package org.example.service;

import com.google.gson.*;
import org.example.models.Administrador;
import org.example.models.Mesero;
import org.example.models.Persona;

import java.lang.reflect.Type;

public class PersonaDeserializer implements JsonDeserializer<Persona> {

    @Override
    public Persona deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        String nombre = jsonObject.get("nombre").getAsString();
        String apellido = jsonObject.get("apellido").getAsString();
        String dni = jsonObject.get("dni").getAsString();

        switch (type) {
            case "Mesero":
                return new Mesero(nombre, apellido, dni);
            case "Administrador":
                String password = jsonObject.get("password").getAsString();
                return new Administrador(nombre, apellido, dni, password);
            default:
                throw new JsonParseException("Unknown element type: " + type);
        }
    }
}
