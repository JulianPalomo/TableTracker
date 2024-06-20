package org.example.models;

import org.example.services.TipoCuenta;

public class Administrador extends Usuario {

    public Administrador(String nombreUsuario, String contrasena, String nombre, String apellido, String email,TipoCuenta tipoCuenta) {
        super(nombreUsuario,contrasena,nombre,apellido,email,tipoCuenta);
    }

    @Override
    public String toString() {
        return "Admin";
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(!(o instanceof Administrador)) {
            return false;
        }
        Administrador administrador = (Administrador) o;
        return this.getNombreUsuario().equalsIgnoreCase(administrador.getNombreUsuario());
    }

}
