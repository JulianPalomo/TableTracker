package org.example.models;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    public SplashScreen(String imagePath, int duration) {
        // Cargar la imagen
        ImageIcon splashIcon = new ImageIcon(imagePath);
        JLabel splashLabel = new JLabel(splashIcon);
        getContentPane().add(splashLabel, BorderLayout.CENTER);

        // Configurar el tamaño de la ventana
        pack();
        setLocationRelativeTo(null);

        // Mostrar la ventana de splash screen durante la duración especificada
        setVisible(true);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setVisible(false);
        dispose();
    }
}
