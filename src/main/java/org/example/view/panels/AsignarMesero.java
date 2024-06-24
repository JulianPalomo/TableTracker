package org.example.view.panels;

import org.example.models.Mesa;
import org.example.models.Mesero;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AsignarMesero extends JFrame {

    private JComboBox<Mesero> waiterComboBox;
    private JButton assignButton;
    private Mesa mesa;

    public AsignarMesero(Mesa mesa, List<Mesero> waiters) {
        if (waiters == null || waiters.isEmpty()) {
            throw new IllegalArgumentException("La lista de meseros no puede ser null o vacía");
        }

        this.mesa = mesa;

        setTitle("Asignar Mesero a Mesa " + mesa.getNroMesa());
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        waiterComboBox = new JComboBox<>(waiters.toArray(new Mesero[0]));
        waiterComboBox.setPreferredSize(new Dimension(200, 30));
        panel.add(waiterComboBox);

        assignButton = new JButton("Asignar");
        assignButton.setPreferredSize(new Dimension(100, 30));
        assignButton.addActionListener(e -> {
            Mesero selectedWaiter = (Mesero) waiterComboBox.getSelectedItem();
            mesa.asignarMesero(selectedWaiter);
            dispose();
        });
        panel.add(assignButton);

        add(panel, BorderLayout.CENTER);
    }
}