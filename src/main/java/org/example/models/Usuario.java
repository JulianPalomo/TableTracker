package org.example.models;


import org.example.interfaces.Filtrable;
import org.example.services.TipoCuenta;

import java.io.Serializable;
import java.util.Objects;

public abstract class Usuario implements Serializable {
    private String nombreUsuario;
    private String contrasena;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean isActivo;
    private TipoCuenta tipoCuenta;
    public Usuario() {
    }

    public Usuario(String nombreUsuario, String contrasena, String nombre, String apellido, String email,TipoCuenta tipoCuenta) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.isActivo = true;
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }

    public Boolean getActivo() {
        return isActivo;
    }

    public void setActivo(Boolean activo) {
        isActivo = activo;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(!(o instanceof Usuario)) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        return this.getNombreUsuario().equalsIgnoreCase(usuario.getNombreUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreUsuario);
    }

    @Override
    public String toString() {
        return "Nombre de usuario: " + nombreUsuario + "\n" +
                "Contraseña: " + contrasena + "\n" +
                "Nombre: " + nombre + "\n" +
                "Apellido: " + apellido + "\n" +
                "Correo electrónico: " + email + "\n" +
                "Activo: " + isActivo + "\n";
    }

    public void cambiarEstado() {
        if(isActivo) {
            isActivo = false;
        }
        else{
            isActivo = true;
        }
    }
}