package org.example.view;
import org.example.models.SplashScreen;
import org.example.models.Usuario;
import org.example.service.UsuarioService;
import org.example.view.panels.Login;
import org.example.view.panels.MesasPanel;

import javax.swing.*;

public class RestaurantApp {
    public static void main(String[] args) {

        SplashScreen screen = new SplashScreen("src/main/java/org/example/resource/Logo.jpg",3000);

        // Asegurarse de que la GUI se ejecute en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            UsuarioService usuarioService = new UsuarioService();
            usuarioService.loadFromJson();
            // Crear el diálogo de login
            Login login = new Login(null); // Pasar null como parámetro ya que no hay ventana principal todavía

            Usuario logeada = (Usuario)login.isLoginSuccessful();

            // Verificar si el login fue exitoso
            if (logeada != null) {
                // Login exitoso, cargar el panel principal
                MesasPanel mainFrame = new MesasPanel("Juli's", logeada.getCredenciales());
                mainFrame.setVisible(true);
            } else {
                // Login fallido, salir de la aplicación
                System.exit(0);
            }
        });
    }
}
