package org.example.models;

public class Administrador extends Usuario {

    public Administrador(String username, String password, String nombreCompleto, String dni) {
        super(username, password, nombreCompleto, dni);
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
        if(!(o instanceof Administrador administrador)) {
            return false;
        }
        return this.getNombreCompleto().equalsIgnoreCase(administrador.getNombreCompleto());
    }
}