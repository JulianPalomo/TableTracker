package org.example.view.controllers;

import org.example.models.Mesa;
import org.example.service.MesaService;
import org.example.view.buttons.MesaButton;

import javax.swing.*;

public class MesaController {

    private MesaService mesaService = new MesaService();
 /*
    public void agregarMesa() {
        int numero = mesaService.agregarMesa();
        JButton nuevaMesa = crearMesa("Mesa " + numero, 50, 50);
        add(nuevaMesa);
        nuevaMesa.repaint();
    }

    private JButton crearMesa(String texto, int x, int y) {
        JButton button = new JButton(texto);
        button.setBounds(x, y, 100, 50); // Tamaño y posición inicial
        MesaButton adapter = new MesaButton();
        button.addMouseListener(adapter);
        button.addMouseMotionListener(adapter);

        // No es necesario el ActionListener si solo se quiere mostrar el botón

        return button;
    }
*/
}
