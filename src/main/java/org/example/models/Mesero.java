package org.example.models;
import java.util.List;
import java.util.ArrayList;

public class Mesero extends Persona {

    public Mesero(String nombre, String apellido, String dni) {
        super(nombre, apellido, dni);
    }

    @Override
    public String toString() {
        return "Mesero{}";
    }
}