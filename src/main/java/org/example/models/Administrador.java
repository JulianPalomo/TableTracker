package org.example.models;

public class Administrador extends Usuario {

    public Administrador(String username, String password, String nombreCompleto, String dni) {
        super(username, password, nombreCompleto, dni);
    }

    @Override
    public String toString() {
        return "Admin";
    }


}
