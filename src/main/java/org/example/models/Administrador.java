package org.example.models;

public class Administrador extends Persona{

    private String password;

    public Administrador(String nombre, String apellido, String dni, String password) {
        super(nombre, apellido, dni);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public boolean login(String dni, String password) {
        if(getDni().equals(dni) && this.password.equals(password))
        {
            return true;
        }
        return false;
    }

}