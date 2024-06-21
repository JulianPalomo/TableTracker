package org.example.view.panels;


import org.example.models.TipoCuenta;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    public Menu(TipoCuenta tipoCuenta) {
        setTitle("Menú Principal");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));

        JButton mesasButton = new JButton("Mesas");
        JButton menuButton = new JButton("Menú");

        mainPanel.add(mesasButton);
        mainPanel.add(menuButton);

        if (tipoCuenta == TipoCuenta.ADMINISTRADOR) {
            JButton meserosButton = new JButton("Meseros");
            JButton anadirUsuarioButton = new JButton("Añadir usuario");
            mainPanel.add(meserosButton);
            mainPanel.add(anadirUsuarioButton);

            anadirUsuarioButton.addActionListener(e -> new Registration(this));
        }

        setContentPane(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu(TipoCuenta.ADMINISTRADOR));
    }
}