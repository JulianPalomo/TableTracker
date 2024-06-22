package org.example.service;

import com.google.gson.*;
import org.example.models.Administrador;
import org.example.models.Mesero;
import org.example.models.TipoCuenta;
import org.example.models.Usuario;

import java.lang.reflect.Type;

public class UsuarioDeserializer implements JsonDeserializer<Usuario> {
    @Override
    public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String tipoCuenta = jsonObject.get("tipoCuenta").getAsString();

        if (TipoCuenta.ADMINISTRADOR.name().equals(tipoCuenta)) {
            return context.deserialize(jsonObject, Administrador.class);
        } else if (TipoCuenta.MESERO.name().equals(tipoCuenta)) {
            return context.deserialize(jsonObject, Mesero.class);
        } else {
            throw new JsonParseException("Tipo de cuenta desconocido: " + tipoCuenta);
        }
    }
}
