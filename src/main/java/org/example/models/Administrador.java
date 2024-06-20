package org.example.models;

import org.example.services.TipoCuenta;

public class Administrador extends Usuario{
    public Administrador() {
    }

    public Administrador(String nombreUsuario, String contrasena, String nombre, String apellido, String email, TipoCuenta tipoCuenta) {
        super(nombreUsuario, contrasena, nombre, apellido, email,tipoCuenta);
    }

    @Override
    public String getContrasena() {
        return super.getContrasena();
    }

    @Override
    public void setContrasena(String contrasena) {
        super.setContrasena(contrasena);
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
        Administrador administrativo = (Administrador) o;
        return this.getNombreUsuario().equalsIgnoreCase(administrativo.getNombreUsuario());
    }



    @Override
    public String toString() {
        return "ADMINISTRATIVO\n" +
                super.toString();
    }
}
