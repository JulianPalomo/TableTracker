package org.example.models.personas;

import java.util.Objects;

public class Usuario extends Persona {

    private String password;
    private Credenciales credenciales;

    public Usuario(String nombre, String apellido, String dni,String password, Credenciales credenciales) {
        super(nombre, apellido, dni);
        this.password = password;
        this.credenciales = credenciales;
    }

    public boolean login(String dni, String password) {
        return getDni().equals(dni) && this.password.equals(password);
    }

    public String getPassword() {
        return password;
    }

    public Credenciales getCredenciales() {
        return credenciales;
    }

}
