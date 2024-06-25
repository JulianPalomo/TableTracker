package org.example.view;

import org.example.models.Mesa;
import org.example.models.Mesero;
import org.example.models.SplashScreen;
import org.example.service.MesaService;
import org.example.service.PersonaService;
import org.example.view.panels.MesasPanel;

import javax.swing.SwingUtilities;
import java.util.List;

public class RestaurantApp {
    public static void main(String[] args) {

        new SplashScreen("src/main/java/org/example/resource/lg.jpg", 3000);

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
