package org.example.view;

import org.example.service.VentaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BalanceView extends JPanel {
    private VentaService ventaService;

    public BalanceView() {
        this.ventaService = new VentaService();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titleLabel = new JLabel("Balance de Ventas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel para las estadísticas
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.setBackground(new Color(245, 245, 245));

        // Obtener y formatear las estadísticas
        String[] estadisticas = ventaService.obtenerEstadisticas().split("\n");
        for (String linea : estadisticas) {
            JLabel statLabel = new JLabel(linea);
            statLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            statLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
            statsPanel.add(statLabel);
        }

        JScrollPane scrollPane = new JScrollPane(statsPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Botón para mostrar balance
        JButton balanceButton = new JButton("Actualizar Balance");
        balanceButton.addActionListener(e -> actualizarBalance(statsPanel));
        add(balanceButton, BorderLayout.SOUTH);
    }

    private void actualizarBalance(JPanel statsPanel) {
        statsPanel.removeAll();
        String[] estadisticas = ventaService.obtenerEstadisticas().split("\n");
        for (String linea : estadisticas) {
            JLabel statLabel = new JLabel(linea);
            statLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            statLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
            statsPanel.add(statLabel);
        }
        statsPanel.revalidate();
        statsPanel.repaint();
    }
}