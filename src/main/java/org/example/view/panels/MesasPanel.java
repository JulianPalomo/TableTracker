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

public class MesasPanel extends JFrame {

    private final MesaService mesaService = new MesaService();
    public boolean modoEdicion = false;
    private PedidoPanel currentPedidoPanel;
    private ProductoService productoService = new ProductoService();
    private JButton confirmarCambios;
    private JButton agregarMesaButton;
    private JButton eliminarMesaButton;
    private JToolBar toolBar;
    private JToolBar edicionBar;
    private CartaPanel cartaPanel = new CartaPanel();
    private JPanel mainPanel;

    public MesasPanel(String nombreComercio, boolean admin) {

        setTitle(nombreComercio);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Cambiar a BorderLayout para el JFrame

        cartaPanel.setVisible(false);

        mainPanel = new JPanel(null); // Panel principal con null layout
        mainPanel.setBackground(Color.DARK_GRAY);

        mesaService.cargarMesasJson();
        crearBotonesDeMesas(mesaService.getMesas());

        // Crear JToolBar
        toolBar = new JToolBar();
        toolBar.setFloatable(false); // Para que no se pueda mover

        edicionBar = new JToolBar();
        edicionBar.setFloatable(false);
        edicionBar.setVisible(false);

        // Crear botones para la toolbar
        JButton toggleEdicionButton = new JButton("Editar Mesas");
        JButton verMenuCompletoButton = new JButton("Carta");
        JButton aboutButton = new JButton("Acerca De");


        toggleEdicionButton.addActionListener(e -> {
            this.modoEdicion = !modoEdicion; // Alternar el modo de edición
            String estado = modoEdicion ? "Activado" : "Desactivado";
            JOptionPane.showMessageDialog(this, "Modo de edición " + estado);
            actualizarModoEdicion();
            edicionBar.setVisible(modoEdicion); // Mostrar/ocultar edicionBar según el modo de edición
        });

        verMenuCompletoButton.addActionListener(e -> cargarCartaPanel());

        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Software de Gestión para Restaurante.\nVersión 1.0"));

        // Agregar botones a la toolbar
        if (admin) {
            toolBar.add(toggleEdicionButton);
        }
        toolBar.add(verMenuCompletoButton);
        toolBar.add(aboutButton);

        // Botón de confirmar edición
        confirmarCambios = new JButton("Confirmar Cambios");
        confirmarCambios.setVisible(false); // Inicialmente invisible
        confirmarCambios.setBackground(Color.CYAN);
        confirmarCambios.addActionListener(e -> {
            mesaService.guardarMesasEnJson();
            modoEdicion = false; // Desactivar el modo de edición
            actualizarModoEdicion(); // Ocultar botones adicionales
        });

        // Botón de agregar mesa
        agregarMesaButton = new JButton("Agregar Mesa");
        agregarMesaButton.setVisible(false); // Inicialmente invisible
        agregarMesaButton.setBackground(Color.GREEN);
        agregarMesaButton.addActionListener(e -> {
            agregarMesa();
            repaint();
        });

        // Botón de eliminar mesa
        eliminarMesaButton = new JButton("Eliminar Mesa");
        eliminarMesaButton.setVisible(false); // Inicialmente invisible
        eliminarMesaButton.setBackground(Color.red);
        eliminarMesaButton.addActionListener(e -> {
            eliminarMesa();
            repaint();
        });

        // Crear un panel para las barras de herramientas
        JPanel toolBarPanel = new JPanel(new BorderLayout());
        toolBarPanel.add(toolBar, BorderLayout.NORTH);
        toolBarPanel.add(edicionBar, BorderLayout.SOUTH);

        // Crear panel principal que contiene la barra de herramientas y el panel principal con null layout
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(toolBarPanel, BorderLayout.NORTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        // Agregar el panel contenedor al frame
        add(containerPanel);

        actualizarColorMesas();
    }

    public void actualizarColorMesas() {
        for (Component comp : mainPanel.getComponents()) {
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
            edicionBar.add(agregarMesaButton);
            edicionBar.add(eliminarMesaButton);
            edicionBar.add(Box.createHorizontalGlue()); // Empuja el siguiente botón a la derecha
            edicionBar.add(confirmarCambios);
        } else {
            edicionBar.remove(agregarMesaButton);
            edicionBar.remove(eliminarMesaButton);
            edicionBar.remove(confirmarCambios);
        }
        edicionBar.revalidate();
        edicionBar.repaint();

        // Habilitar/deshabilitar edición en botones de mesas
        for (Component comp : mainPanel.getComponents()) {
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
                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof JButton button) {
                        if (button.getText().equals("Mesa " + id)) {
                            mesaService.eliminarUltimaMesa(); // Eliminar la mesa del servicio
                            mainPanel.remove(button);
                            mainPanel.revalidate();
                            mainPanel.repaint();
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
        mainPanel.add(nuevaMesa);
        nuevaMesa.repaint();
        actualizarColorMesas();
    }

    public void crearBotonesDeMesas(List<Mesa> mesas) {
        for (Mesa mesa : mesas) {
            JButton nuevaMesa = crearMesa(mesa);
            mainPanel.add(nuevaMesa);
        }
    }

    private JButton crearMesa(Mesa nueva) {
        JButton button = new JButton("Mesa " + nueva.getId());
        button.setBounds(nueva.getX(), nueva.getY(), nueva.getAncho(), nueva.getAlto()); // Tamaño y posición inicial

        MesaButton mesaButton = new MesaButton(nueva);
        mesaButton.setEditable(modoEdicion); // Establecer la editabilidad según el modo actual
        button.addMouseListener(mesaButton);
        button.addMouseMotionListener(mesaButton);

        button.addActionListener(e -> {
            if (!modoEdicion) { // Solo abrir PedidoPanel si no estamos en modo edición
                int numeroMesa = Integer.parseInt(button.getText().split(" ")[1]);
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
