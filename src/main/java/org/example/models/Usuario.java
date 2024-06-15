package org.example.models;


import org.example.interfaces.Filtrable;

import java.io.Serializable;
import java.util.Objects;

public abstract class Usuario {
    private String username;
    private String password;
    private final String dni;
    private String nombreCompleto;

    public Usuario(String username, String password, String nombreCompleto, String dni) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDni() {
        return dni;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(dni, usuario.dni) && Objects.equals(password, usuario.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni, password);
    }

}
