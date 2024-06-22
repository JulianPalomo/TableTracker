package org.example.view;


import org.example.models.Mesa;
import org.example.service.MesaService;
import org.example.view.panels.MesasPanel;

import javax.swing.SwingUtilities;

public class RestaurantApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MesasPanel layout = new MesasPanel("Juli's", true);
            layout.setVisible(true);

        });
        /*
        MesaService mesaService = new MesaService();
        mesaService.cargarMesasJson();
        for (Mesa mesa : mesaService.getMesas()) {
            System.out.println(mesa);
        }

        */

    }

/**
    public static void main(String[] args) {
        MenuLoader loader = new MenuLoader();
        Map<String, List<Producto>> menu = loader.cargarMenu("src/main/java/org/example/productos.json");

        SwingUtilities.invokeLater(() -> {
            Menu display = new Menu(menu);
            display.setVisible(true);
        });
    }
    */

}
