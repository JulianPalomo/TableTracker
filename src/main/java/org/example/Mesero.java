package org.example.models;
import java.util.List;
import java.util.ArrayList;

public class Mesero extends Usuario {

    private List<Mesa> listaMesasAsignadas;

    public Mesero(String username, String password, String nombreApellido, String dni) {
        super(username, password, nombreApellido, dni);
        this.listaMesasAsignadas = new ArrayList<Mesa>();
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