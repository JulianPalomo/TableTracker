package org.example.view;

import org.example.services.TipoCuenta;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    private JButton mesasButton;
    private JButton menuButton;
    private JButton meserosButton;
    private JButton anadirUsuarioButton;
    private JPanel mainPanel;

    public Menu(TipoCuenta tipoCuenta) {
        setTitle("Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));

        mesasButton = new JButton("Mesas");
        menuButton = new JButton("Menú");
        mainPanel.add(mesasButton);
        mainPanel.add(menuButton);

        if (tipoCuenta == TipoCuenta.ADMINISTRADOR) {
            meserosButton = new JButton("Meseros");
            anadirUsuarioButton= new JButton("Añadir usuario");
            mainPanel.add(meserosButton);
            mainPanel.add(anadirUsuarioButton);
        }

        setContentPane(mainPanel);
        setVisible(true);

        // Action Listeners for buttons can be added here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu(TipoCuenta.ADMINISTRADOR));
    }
}
