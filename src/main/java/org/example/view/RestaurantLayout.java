package org.example.view;


import org.example.models.Mesa;
import org.example.models.Producto;
import org.example.service.MesaService;
import org.example.view.buttons.MesaButton;
import org.example.view.controllers.MesaController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import static java.awt.SystemColor.menu;

public class RestaurantLayout extends JFrame {

    // aca se deberian crear las instancias de todos los controladores
    private final MesaService mesaService = new MesaService();
    public boolean modoEdicion = false; // Flag para el modo de edición

    public RestaurantLayout(String nombreComercio) {
        setTitle(nombreComercio);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Utilizamos null layout para posicionamiento absoluto

        // Agregar JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Crear menús
        JMenu menuMesas = new JMenu("Mesas");
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenu verMenu = new JMenu("Ver Menú");


        // Crear elementos de menú
        JMenuItem toggleEdicionItem = new JMenuItem("Activar/Desactivar Edición");
        JMenuItem addMesaItem = new JMenuItem("Agregar Mesa");
        JMenuItem verMenuCompleto = new JMenuItem("Ver Menú Completo");
        JMenuItem aboutItem = new JMenuItem("Acerca de");


        // Añadir listener para el toggle de edición
        toggleEdicionItem.addActionListener(e -> {
            modoEdicion = !modoEdicion; // Alternar el modo de edición
            String estado = modoEdicion ? "Activado" : "Desactivado";
            JOptionPane.showMessageDialog(this, "Modo de edición " + estado);
        });

        addMesaItem.addActionListener(e -> {
            agregarMesa();
            repaint();
        });

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Software de Gestión para Restaurante.\nVersión 1.0"));

        // Agregar elementos de menú a los menús
        menuMesas.add(toggleEdicionItem); // Añadir el toggle de edición al menú
        menuMesas.add(addMesaItem);
        menuAyuda.add(aboutItem);
        verMenu.add(verMenuCompleto);

        // Agregar menús a la barra de menú
        menuBar.add(menuMesas);
        menuBar.add(menuAyuda);
        menuBar.add(verMenu);


        // Establecer la barra de menú
        setJMenuBar(menuBar);
    }


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

        return button;
    }

    /*

    private void irAlMenu(){
        MenuLoader loader = new MenuLoader();

        SwingUtilities.invokeLater(() -> {
            Menu display = new Menu(menu);
            display.disable(true);
        });
    }*/


}