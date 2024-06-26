package org.example.view;
import org.example.models.SplashScreen;
import org.example.models.personas.Usuario;
import org.example.service.Usuario.UsuarioService;

import javax.swing.*;

public class RestaurantApp {
    public static void main(String[] args) {

        new SplashScreen("src/main/java/org/example/resource/Logo.jpg", 3000);

        SwingUtilities.invokeLater(() -> {
            UsuarioService usuarioService = new UsuarioService();
            usuarioService.loadFromJson();

            LoginView loginView = new LoginView(null); // Pasar null como parámetro ya que no hay ventana principal todavía

            Usuario logeada = loginView.isLoginSuccessful();

            // Verificar si el login fue exitoso
            if (logeada != null) {
                // Login exitoso, cargar el panel principal
                MesasView mainFrame = new MesasView("Juli's", logeada.getCredenciales());
                mainFrame.setVisible(true);
            } else {
                // Login fallido, salir de la aplicación
                System.exit(0);
            }
        });
    }
}
