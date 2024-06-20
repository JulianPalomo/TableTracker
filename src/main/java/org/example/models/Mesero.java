package org.example.models;
import java.util.List;
import java.util.ArrayList;

public class Mesero extends Usuario {

    private List<Mesa> listaMesasAsignadas;

    // Constructor por defecto
    public Mesero() {
        super();
    }

    // Constructor con argumentos
    public Mesero(String nombreUsuario, String contrasena, String nombre, String apellido, String email, TipoCuenta tipoCuenta) {
        super(nombreUsuario, contrasena, nombre, apellido, email, tipoCuenta);
        this.listaMesasAsignadas=new ArrayList<>();
    }


    public List<Mesa> getListaMesasAsignadas() {
        return listaMesasAsignadas;
    }

    public void setListaMesasAsignadas(List<Mesa> listaMesasAsignadas) {
        this.listaMesasAsignadas = listaMesasAsignadas;
    }

    public void agregarMesa(Mesa mesa){
        listaMesasAsignadas.add(mesa);
    }

    @Override
    public String toString() {
        return "Mesero{" +
                "listaMesasAsignadas=" + listaMesasAsignadas +
                '}';
    }
}