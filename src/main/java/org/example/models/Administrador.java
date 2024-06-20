package org.example.models;

public class Administrador extends Usuario {


    // Constructor por defecto
    public Administrador() {
        super();
    }

    // Constructor con argumentos
    public Administrador(String nombreUsuario, String contrasena, String nombre, String apellido, String email, TipoCuenta tipoCuenta) {
        super(nombreUsuario, contrasena, nombre, apellido, email, tipoCuenta);
    }
    @Override
    public String toString() {
        return "Admin";
    }
/**
    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(!(o instanceof Administrador administrador)) {
            return false;
        }
        return this.getNombreCompleto().equalsIgnoreCase(administrador.getNombreCompleto());
    }
    **/
    }

