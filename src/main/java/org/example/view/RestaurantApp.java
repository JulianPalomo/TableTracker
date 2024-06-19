package org.example.view;


import org.example.view.panels.MesasPanel;

import javax.swing.SwingUtilities;

public class RestaurantApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MesasPanel layout = new MesasPanel("LOGIN lucre");
            layout.setVisible(true);
        });
    }

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

}
