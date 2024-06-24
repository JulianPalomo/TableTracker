package org.example.view;

import org.example.models.Mesa;
import org.example.models.Mesero;
import org.example.service.MesaService;
import org.example.service.PersonaService;
import org.example.view.panels.MesasPanel;

import javax.swing.SwingUtilities;
import java.util.List;

public class RestaurantApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PersonaService personaService = new PersonaService();
            personaService.loadFromJson();

            // Obtener la lista de meseros
            List<Mesero> waiters = personaService.getListaMeseros();

            MesasPanel layout = new MesasPanel("Juli's", true);
            layout.setVisible(true);
        });
    }
}
        /*
        MesaService mesaService = new MesaService();
        mesaService.cargarMesasJson();
        for (Mesa mesa : mesaService.getMesas()) {
            System.out.println(mesa);
        }

        */

/*
    public static void main(String[] args) {
        MenuLoader loader = new MenuLoader();
        Map<String, List<Producto>> menu = loader.cargarMenu("src/main/java/org/example/productos.json");

        SwingUtilities.invokeLater(() -> {
            Menu display = new Menu(menu);
            display.setVisible(true);
        });
    }
    */
