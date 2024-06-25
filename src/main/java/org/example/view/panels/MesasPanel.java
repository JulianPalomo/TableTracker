package org.example.view.panels;

import org.example.models.EstadoMesa;
import org.example.models.Mesa;
import org.example.models.Mesero;
import org.example.models.Pared;
import org.example.service.MesaService;
import org.example.service.PersonaService;
import org.example.service.ProductoService;
import org.example.view.buttons.MesaButton;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MesasPanel extends JFrame {

    private final MesaService mesaService = MesaService.getInstance();
    public boolean modoEdicion = false;
    private PedidoPanel currentPedidoPanel;
    private ProductoService productoService = new ProductoService();
    private JButton confirmarCambios;
    private JButton agregarMesaButton;
    private JButton eliminarMesaButton;
    private JButton agregarParedButton;
    private JButton eliminarParedButton;
    private JPanel edicionBar;
    private CartaPanel cartaPanel = new CartaPanel();
    private JPanel mainPanel;
    private List<Mesero> waiters;
    public MesasPanel(String nombreComercio, boolean admin) {

        setTitle(nombreComercio);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Cambiar a BorderLayout para el JFrame
        // Oculta la barra de título y otros elementos de decoración
        setUndecorated(true);

        PersonaService personaService = new PersonaService();
        personaService.loadFromJson();

        // Cargar la lista de meseros
        waiters = personaService.getListaMeseros();

        // Verificar si waiters es null o está vacía
        if (waiters == null || waiters.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay meseros disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Opcional: Si deseas abrir la ventana maximizada al iniciar
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Establecer la posición de la ventana
        setLocationRelativeTo(null);
        cartaPanel.setVisible(false);

        mainPanel = new JPanel(null); // Panel principal con null layout
        mainPanel.setBackground(Color.DARK_GRAY);

        mesaService.cargarMesasYParedesJSON();

        crearBotonesDeMesas(mesaService.getMesas(), mesaService.getParedes());

        // Crear ToolBar en un Panel
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.CENTER,50,5));

        // Crea ToolBar para las opciones de administrador
        edicionBar = new JPanel(new FlowLayout(FlowLayout.CENTER,50,5));
        edicionBar.setVisible(false);

        JPanel toolBarPanel = new JPanel(new BorderLayout());
        toolBarPanel.add(toolBar, BorderLayout.NORTH);
        toolBarPanel.add(edicionBar, BorderLayout.SOUTH); // Agregar edicionBar en el sur


        // Crear botones para la toolbar
        // Agregar un botón personalizado para cerrar la ventana
        JButton closeButton = new JButton();
        closeButton.setContentAreaFilled(false); // Hace que el área de contenido del botón sea transparente
        closeButton.setBorderPainted(false); // Elimina el borde del botón
        closeButton.addActionListener(e -> {
            int confirmDialog = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirmDialog == JOptionPane.YES_OPTION) {
                dispose(); // Cierra la ventana
                System.exit(0); // Termina la ejecución del programa
            }
        });



        JButton toggleEdicionButton = new JButton("Editar Salón");
        JButton verMenuCompletoButton = new JButton("Carta");
        JButton aboutButton = new JButton("Acerca De");
        JButton addUserButton = new JButton("Añadir Usuario");

        toggleEdicionButton.addActionListener(e -> {
            // Mostrar el cuadro de diálogo de inicio de sesión
            Login loginDialog = new Login(this);

            // Verificar si el inicio de sesión fue exitoso
            if (loginDialog.isLoginSuccessful()) {
                this.modoEdicion = !modoEdicion; // Alternar el modo de edición
                String estado = modoEdicion ? "Activado" : "Desactivado";
                JOptionPane.showMessageDialog(this, "Modo de edición " + estado);
                actualizarModoEdicion();
                edicionBar.setVisible(modoEdicion); // Mostrar/ocultar edicionBar según el modo de edición
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo activar el modo de edición. Inicio de sesión fallido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        verMenuCompletoButton.addActionListener(e -> cargarCartaPanel());

        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Software de Gestión para Restaurante.\nVersión 1.0"));

        //addUserButton.addActionListener(e -> new Registration(this));

        Dimension buttonSize = new Dimension(150, 40); // Ancho x Alto

// Agregar botones a la toolbar
        if (admin) {
            toolBar.add(toggleEdicionButton);
            toggleEdicionButton.setPreferredSize(buttonSize);

            toolBar.add(addUserButton);
            addUserButton.setPreferredSize(buttonSize);
            toolBar.add(verMenuCompletoButton);
            verMenuCompletoButton.setPreferredSize(buttonSize);
        }

        toolBar.add(aboutButton);
        aboutButton.setPreferredSize(buttonSize);

        toolBar.add(closeButton);
        closeButton.setPreferredSize(buttonSize);
        closeButton.setIcon(new ImageIcon("src/main/java/org/example/resource/cruz.png"));

        // Botón de confirmar edición
        confirmarCambios = new JButton("Confirmar Cambios");
        confirmarCambios.setVisible(false); // Inicialmente invisible
        confirmarCambios.setBackground(Color.CYAN);
        confirmarCambios.addActionListener(e -> {
            mesaService.guardarMesasYParedesJSON();
            modoEdicion = false; // Desactivar el modo de edición
            actualizarModoEdicion(); // Ocultar botones adicionales
        });

        //Boton de agregar pared
        agregarParedButton = new JButton("Agregar Estructura");
        agregarParedButton.setVisible(false);
        agregarParedButton.setBackground(Color.YELLOW);
        agregarParedButton.addActionListener(e -> {
            agregarPared();
            repaint();
        });

        //Boton de eliminar pared
        eliminarParedButton = new JButton("Eliminar estructura");
        eliminarParedButton.setVisible(false);
        eliminarParedButton.setBackground(Color.PINK);
        eliminarParedButton.addActionListener(e -> {
            eliminarPared();
            repaint();
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
        eliminarMesaButton.setBackground(Color.LIGHT_GRAY);
        eliminarMesaButton.addActionListener(e -> {
            eliminarMesa();
            repaint();
        });

// Crear panel principal que contiene la barra de herramientas y el panel principal con null layout
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(toolBarPanel, BorderLayout.NORTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        containerPanel.add(edicionBar, BorderLayout.SOUTH); // Agregar edicionBar en el sur

// Agregar el panel contenedor al frame
        add(containerPanel);

        // Agregar el panel contenedor al frame
        add(containerPanel);

        actualizarColorMesas();
    }

    public void actualizarColorMesas() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JButton button) {
                String buttonText = button.getText();

                if (buttonText.startsWith("Mesa ")) {
                    try {
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
                    } catch (NumberFormatException e) {
                        System.out.println("Error al convertir el ID de la mesa: " + e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Formato de texto del botón incorrecto: " + buttonText);
                    }
                }
            }
        }
    }


    private void actualizarModoEdicion() {
        agregarMesaButton.setVisible(modoEdicion);
        eliminarMesaButton.setVisible(modoEdicion);
        confirmarCambios.setVisible(modoEdicion);
        agregarParedButton.setVisible(modoEdicion);
        eliminarParedButton.setVisible(modoEdicion);
        Dimension buttonSize = new Dimension(150, 40); // Ancho x Alto


        if (modoEdicion) {
            agregarMesaButton.setPreferredSize(buttonSize);
            edicionBar.add(agregarMesaButton);

            eliminarMesaButton.setPreferredSize(buttonSize);
            edicionBar.add(eliminarMesaButton);

            agregarParedButton.setPreferredSize(buttonSize);
            edicionBar.add(agregarParedButton);

            eliminarParedButton.setPreferredSize(buttonSize);
            edicionBar.add(eliminarParedButton);

            confirmarCambios.setPreferredSize(buttonSize);
//            edicionBar.add(Box.createHorizontalGlue()); // Empuja el siguiente botón a la derecha
            edicionBar.add(confirmarCambios);


        } else {
            edicionBar.remove(agregarMesaButton);
            edicionBar.remove(eliminarMesaButton);
            edicionBar.remove(confirmarCambios);
            edicionBar.remove(agregarParedButton);
        }
        edicionBar.revalidate();
        edicionBar.repaint();

        // Habilitar/deshabilitar edición en botones de mesas
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JButton button) {
                for (MouseListener ml : button.getMouseListeners()) {
                    if (ml instanceof MesaButton) {
                        ((MesaButton<?>) ml).setEditable(modoEdicion);
                    }
                }
            }
        }
    }

    public void cargarCartaPanel() {
        cartaPanel.setVisible(true);
    }

    public void eliminarMesa() {
        try {
            if (!mesaService.getMesas().isEmpty()) {
                // Obtener el ID de la última mesa
                int id = mesaService.getMesas().size();

                // Buscar y eliminar el botón correspondiente a la última mesa
                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof JButton button && button.getText().equals("Mesa " + id)) {
                        mesaService.eliminarUltimaMesa(); // Eliminar la mesa del servicio
                        mainPanel.remove(button);
                        actualizarColorMesas();
                        mainPanel.revalidate();
                        mainPanel.repaint();

                        // Depuración
                        System.out.println("Mesa visual eliminada: Mesa " + id);
                        break;
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

    public void eliminarPared() {
        try {
            if (!mesaService.getParedes().isEmpty()) {
                // Obtener la última pared
                int id = mesaService.getParedes().size();

                // Buscar y eliminar el botón correspondiente a la última pared
                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof JButton button && button.getText().equals("p")) {
                        mesaService.eliminarUltimaPared(); // Eliminar la pared del servicio
                        mainPanel.remove(button);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No hay paredes para eliminar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la pared.");
            e.printStackTrace();
        }
    }

/*
    public void eliminarObjeto() {
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
                        else if (button.getText().equals("p")){
                            mesaService.eliminarUltimaPared();
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
    }*/

    public void agregarMesa() {
        Mesa nueva = mesaService.agregarMesa();
        JButton nuevaMesa = crearMesa(nueva);
        mainPanel.add(nuevaMesa);
        nuevaMesa.repaint();
        actualizarColorMesas();

        System.out.println("Mesa visual agregada: " + nueva);
    }

    public void agregarPared() {
        Pared nueva = mesaService.agregarPared();
        JButton nuevaPared = crearPared(nueva);
        mainPanel.add(nuevaPared);
        nuevaPared.repaint();
        actualizarColorMesas();
    }

    public void crearBotonesDeMesas(List<Mesa> mesas, List<Pared> paredes) {
        System.out.println("Creando mesas:");
        for (Mesa mesa : mesas) {
            System.out.println("Mesa ID: " + mesa.getNroMesa());
            JButton nuevaMesa = crearMesa(mesa);
            mainPanel.add(nuevaMesa);
        }

        System.out.println("Creando paredes:");
        for (Pared pared : paredes) {
            System.out.println("Pared: " + pared);
            JButton nuevaPared = crearPared(pared);
            mainPanel.add(nuevaPared);
        }
    }


    private JButton crearPared(Pared pared) {
        JButton button = new JButton("p");
        button.setBounds(pared.getX(), pared.getY(), pared.getAncho(), pared.getAlto());

        button.setBackground(Color.BLACK);
        button.setForeground(Color.BLACK);

        // Deshabilitar el efecto de resaltado al pasar el cursor
        button.setFocusPainted(false);
        // Deshabilitar el efecto de presionado
        button.setContentAreaFilled(false);
        // Deshabilitar el efecto de enfoque
        button.setFocusable(false);
        // Deshabilitar el efecto de borde
        button.setBorderPainted(false);

        // Deshabilitar cualquier otro efecto visual que pueda estar presente
        button.setOpaque(true); // Asegurar que el botón sea opaco


        MesaButton<Pared> paredButton = new MesaButton<>(pared);
        paredButton.setEditable(modoEdicion);
        button.addMouseListener(paredButton);
        button.addMouseMotionListener(paredButton);
        return button;
    }

    private JButton crearMesa(Mesa nueva) {
        JButton button = new JButton("Mesa " + nueva.getNroMesa());
        button.setBounds(nueva.getX(), nueva.getY(), nueva.getAncho(), nueva.getAlto()); // Tamaño y posición inicial

        MesaButton<Mesa> mesaButton = new MesaButton<>(nueva);
        mesaButton.setEditable(modoEdicion); // Establecer la editabilidad según el modo actual
        button.addMouseListener(mesaButton);
        button.addMouseMotionListener(mesaButton);

        button.addActionListener(e -> {
            if (!modoEdicion) { // Solo abrir PedidoPanel si no estamos en modo edición
                int numeroMesa = Integer.parseInt(button.getText().split(" ")[1]);
                Mesa mesa = mesaService.buscarMesa(numeroMesa);

                if (mesa.getMesero() == null) {
                    // Mostrar la ventana para asignar mesero
                    AsignarMesero asignarMeseroFrame = new AsignarMesero(mesa, waiters);
                    asignarMeseroFrame.setVisible(true);

                    // Añadir un listener para cuando se cierre la ventana de asignar mesero
                    asignarMeseroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            // Verificar si se asignó un mesero y luego abrir el PedidoPanel
                            if (mesa.getMesero() != null) {
                                abrirPedidoPanel(mesa);
                            }
                        }
                    });
                } else {
                    // Si ya tiene mesero asignado, abrir directamente el PedidoPanel
                    abrirPedidoPanel(mesa);
                }
            }
        });

        return button;
    }

    private void abrirPedidoPanel(Mesa mesa) {
        PedidoPanel pedidoPanel = new PedidoPanel(mesa, productoService, this);

        // Cerrar el panel de pedido actual si existe
        if (currentPedidoPanel != null) {
            currentPedidoPanel.setVisible(false);
            currentPedidoPanel.dispose(); // Liberar recursos del panel anterior
        }

        // Actualizar la referencia al panel actual
        currentPedidoPanel = pedidoPanel;

        pedidoPanel.setVisible(true);
    }
}

