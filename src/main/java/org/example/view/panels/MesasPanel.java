package org.example.view.panels;

import org.example.models.EstadoMesa;
import org.example.models.Mesa;
import org.example.service.MesaService;
import org.example.service.ProductoService;
import org.example.view.buttons.MesaButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;

import static java.awt.SystemColor.menu;

public class MesasPanel extends JFrame {

    private final MesaService mesaService = new MesaService();
    public boolean modoEdicion = false;
    private PedidoPanel currentPedidoPanel;
    private ProductoService productoService = new ProductoService();
    private JButton confirmarCambios;
    private JButton agregarMesaButton;
    private JButton eliminarMesaButton;
    private JMenuBar menuBar;
    private CartaPanel cartaPanel = new CartaPanel();

    public MesasPanel(String nombreComercio, boolean admin) {



        setTitle(nombreComercio);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Utilizamos null layout para posicionamiento absoluto


        mesaService.cargarMesasJson();
        crearBotonesDeMesas(mesaService.getMesas());

        // Botón de confirmar edición
        confirmarCambios = new JButton("Confirmar Cambios");
        confirmarCambios.setVisible(false); // Inicialmente invisible

        confirmarCambios.addActionListener(e -> {
            mesaService.guardarMesasEnJson();
            modoEdicion = false; // Desactivar el modo de edición
            actualizarModoEdicion(); // Ocultar botones adicionales
        });

        // Botón de agregar mesa
        agregarMesaButton = new JButton("Agregar Mesa");
        agregarMesaButton.setVisible(false); // Inicialmente invisible
        agregarMesaButton.addActionListener(e -> {
            agregarMesa();
            repaint();
        });

        // Botón de eliminar mesa
        eliminarMesaButton = new JButton("Eliminar Mesa");
        eliminarMesaButton.setVisible(false); // Inicialmente invisible
        eliminarMesaButton.addActionListener(e -> {
            eliminarMesa();
            repaint();
        });

        // Agregar JMenuBar
        menuBar = new JMenuBar();

        // Crear menús
        JMenu menuMesas = new JMenu("Mesas");
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenu verMenu = new JMenu("Ver Menú");

        // Crear elementos de menú
        JMenuItem toggleEdicionItem = new JMenuItem("Activar/Desactivar Edición");
        JMenuItem verMenuCompleto = new JMenuItem("Ver Menú Completo");
        JMenuItem aboutItem = new JMenuItem("Acerca de");

        // Añadir listener para el toggle de edición
        toggleEdicionItem.addActionListener(e -> {
            this.modoEdicion = !modoEdicion; // Alternar el modo de edición
            String estado = modoEdicion ? "Activado" : "Desactivado";
            JOptionPane.showMessageDialog(this, "Modo de edición " + estado);
            actualizarModoEdicion();
        });

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Software de Gestión para Restaurante.\nVersión 1.0"));

        // Agregar elementos de menú a los menús
        menuMesas.add(toggleEdicionItem); // Añadir el toggle de edición al menú
        menuAyuda.add(aboutItem);
        verMenu.add(verMenuCompleto);
        verMenuCompleto.addActionListener(e ->
                {
                    cargarCartaPanel();
                }
                );

        // Agregar menús a la barra de menú
        if (admin) {
            menuBar.add(menuMesas);
        }
        menuBar.add(menuAyuda);
        menuBar.add(verMenu);

        // Establecer la barra de menú
        setJMenuBar(menuBar);


        getContentPane().setBackground(Color.DARK_GRAY);
        actualizarColorMesas();
    }

    public void actualizarColorMesas() {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String buttonText = button.getText();
                int mesaId = Integer.parseInt(buttonText.split(" ")[1]);
                Mesa mesa = mesaService.buscarMesa(mesaId);
                if (mesa != null) {
                    if (mesa.getEstado() == EstadoMesa.DISPONIBLE) {
                        button.setBackground(Color.GREEN);
                    } else {
                        button.setBackground(Color.RED);
                    }
                    button.repaint();
                }
            }
        }
    }



    private void actualizarModoEdicion() {
        agregarMesaButton.setVisible(modoEdicion);
        eliminarMesaButton.setVisible(modoEdicion);
        confirmarCambios.setVisible(modoEdicion);

        if (modoEdicion) {
            menuBar.add(agregarMesaButton);
            menuBar.add(eliminarMesaButton);
            menuBar.add(Box.createHorizontalGlue()); // Empuja el siguiente botón a la derecha
            menuBar.add(confirmarCambios);
        } else {
            menuBar.remove(agregarMesaButton);
            menuBar.remove(eliminarMesaButton);
            menuBar.remove(confirmarCambios);
        }
        menuBar.revalidate();
        menuBar.repaint();

        // Habilitar/deshabilitar edición en botones de mesas
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                for (MouseListener ml : button.getMouseListeners()) {
                    if (ml instanceof MesaButton) {
                        ((MesaButton) ml).setEditable(modoEdicion);
                    }
                }
            }
        }
    }

    public void cargarCartaPanel(){
        cartaPanel.setVisible(true);
    }

    public void eliminarMesa() {
        try {
            if (!mesaService.getMesas().isEmpty()) {
                // Obtener el ID de la última mesa
                int id = mesaService.getMesas().size();

                // Buscar y eliminar el botón correspondiente a la última mesa
                for (Component comp : getContentPane().getComponents()) {
                    if (comp instanceof JButton button) {
                        if (button.getText().equals("Mesa " + id)) {
                            mesaService.eliminarUltimaMesa(); // Eliminar la mesa del servicio
                            remove(button);
                            revalidate();
                            repaint();
                            break;
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No hay mesas para eliminar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la mesa.");
            e.printStackTrace();
        }
    }


    public void agregarMesa() {
        Mesa nueva = mesaService.agregarMesa();
        JButton nuevaMesa = crearMesa(nueva);
        add(nuevaMesa);
        nuevaMesa.repaint();
        actualizarColorMesas();
    }

    ///CONVIERTE CADA MESA EN JBUTTON Y LAS AGREGA AL PANEL QUE SE LE PASA POR PARAMETRO
    public void crearBotonesDeMesas(List<Mesa> mesas) {
        for (Mesa mesa : mesas) {
                JButton nuevaMesa = crearMesa(mesa);
                add(nuevaMesa);
        }
    }

    private JButton crearMesa(Mesa nueva) {
        JButton button = new JButton("Mesa " + nueva.getId());
        String texto = button.getText();
        button.setBounds(nueva.getX(), nueva.getY(), nueva.getAncho(), nueva.getAlto()); // Tamaño y posición inicial

        MesaButton mesaButton = new MesaButton(nueva);
        mesaButton.setEditable(modoEdicion); // Establecer la editabilidad según el modo actual
        button.addMouseListener(mesaButton);
        button.addMouseMotionListener(mesaButton);

        button.addActionListener(e -> {
            if (!modoEdicion) { // Solo abrir PedidoPanel si no estamos en modo edición
                int numeroMesa = Integer.parseInt(texto.split(" ")[1]);
                PedidoPanel pedidoPanel = new PedidoPanel(mesaService.buscarMesa(numeroMesa), productoService,this);

                // Cerrar el panel de pedido actual si existe
                if (currentPedidoPanel != null) {
                    currentPedidoPanel.setVisible(false);
                    currentPedidoPanel.dispose(); // Liberar recursos del panel anterior
                }

                // Actualizar la referencia al panel actual
                currentPedidoPanel = pedidoPanel;

                pedidoPanel.setVisible(true);
            }
        });

        return button;
    }
}

/*
    private JButton crearMesa(String texto, int x, int y) {
        JButton button = new JButton(texto);
        button.setBounds(x, y, 100, 50); // Tamaño y posición inicial

        MesaButton adapter = new MesaButton();
        button.addMouseListener(adapter);
        button.addMouseMotionListener(adapter);

        button.addActionListener(e -> {

            int numeroMesa = Integer.parseInt(texto.split(" ")[1]);
            PedidoPanel pedidoPanel = new PedidoPanel(mesaService.buscarMesa(numeroMesa));
            pedidoPanel.setVisible(true);

        });

        return button;
    }
*/


/*
    private void irAlMenu(){
        MenuLoader loader = new MenuLoader();

        SwingUtilities.invokeLater(() -> {
            Menu display = new Menu(menu);
            display.disable(true);
        });
    }*/


