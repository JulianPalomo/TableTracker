package org.example.view;

import org.example.models.mesas.EstadoMesa;
import org.example.models.mesas.Mesa;
import org.example.models.objetos.Pared;
import org.example.models.personas.Credenciales;
import org.example.models.personas.Usuario;
import org.example.service.MesaService;
import org.example.service.ProductoService;
import org.example.service.Usuario.UsuarioService;
import org.example.view.buttons.MesaButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;

public class MesasView extends JFrame {

    private final MesaService mesaService = MesaService.getInstance();
    public boolean modoEdicion = false;
    private PedidoView currentPedidoView;
    private final ProductoService productoService = new ProductoService();
    private JButton confirmarCambios;
    private JButton agregarMesaButton;
    private JButton eliminarMesaButton;
    private JButton agregarParedButton;
    private JButton eliminarParedButton;
    private JPanel edicionBar;
    private final CartaView cartaView = new CartaView();
    private JPanel mainPanel;
    private final List<Usuario> waiters;
    private final Credenciales credenciales;


    public MesasView(String nombreComercio, Credenciales credenciales) {

        setTitle(nombreComercio);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Cambiar a BorderLayout para el JFrame
        // Oculta la barra de título y otros elementos de decoración
        setUndecorated(true);

        this.credenciales = credenciales;

        UsuarioService usuarioService = new UsuarioService();
        usuarioService.loadFromJson();

        // Cargar la lista de meseros
        waiters = usuarioService.getListaMeseros();

        // Verificar si waiters es null o está vacía
        if (waiters == null || waiters.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay meseros disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Opcional: Si deseas abrir la ventana maximizada al iniciar
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Establecer la posición de la ventana
        setLocationRelativeTo(null);
        cartaView.setVisible(false);

        mainPanel = new JPanel(null); // Panel principal con null layout
        mainPanel.setBackground(new Color(219,180,119));


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
        JButton balanceButton = new JButton("Balance de Ventas");
        JButton listaDeUsuarios = new JButton("Usuarios");

        toggleEdicionButton.addActionListener(e -> {
            // Mostrar el cuadro de diálogo de inicio de sesión
                this.modoEdicion = !modoEdicion; // Alternar el modo de edición
                String estado = modoEdicion ? "Activado" : "Desactivado";
                JOptionPane.showMessageDialog(this, "Modo de edición " + estado);
                actualizarModoEdicion();
                edicionBar.setVisible(modoEdicion); // Mostrar/ocultar edicionBar según el modo de edición

        });

        verMenuCompletoButton.addActionListener(e -> cargarCartaPanel());

        listaDeUsuarios.addActionListener(e -> {
            UsuariosView usuarios = new UsuariosView();
            // Crear un JDialog para mostrar BalanceView
            JDialog balanceDialog = new JDialog(this, "Usuarios", false); // false para modalidad no modal
            balanceDialog.add(usuarios);
            balanceDialog.pack();
            balanceDialog.setVisible(true);
        });

        balanceButton.addActionListener(e -> {
            BalanceView balanceView = new BalanceView();

            // Crear un JDialog para mostrar BalanceView
            JDialog balanceDialog = new JDialog(this, "Balance de Ventas", false); // false para modalidad no modal
            balanceDialog.add(balanceView);
            balanceDialog.pack();
            balanceDialog.setVisible(true);
        });

        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Software de Gestión para Restaurante.\nVersión 1.0"));

        Dimension buttonSize = new Dimension(150, 40); // Ancho x Alto

// Agregar botones a la toolbar
        if (credenciales == Credenciales.ADMINISTRADOR) {
            toolBar.add(toggleEdicionButton);
            toggleEdicionButton.setPreferredSize(buttonSize);

            toolBar.add(verMenuCompletoButton);
            verMenuCompletoButton.setPreferredSize(buttonSize);


            toolBar.add(listaDeUsuarios);
            listaDeUsuarios.setPreferredSize(buttonSize);


        }

        if (credenciales == Credenciales.ADMINISTRADOR || credenciales == Credenciales.CAJERO) {
            toolBar.add(balanceButton);
            balanceButton.setPreferredSize(buttonSize);
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
                            } else if (mesa.getEstado() == EstadoMesa.OCUPADA) {
                                button.setBackground(Color.RED);
                            } else {
                                button.setBackground(Color.PINK);
                            }
                            button.repaint();
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                                mainPanel,
                                "Error al convertir el ID de la mesa: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } catch (ArrayIndexOutOfBoundsException e) {
                        JOptionPane.showMessageDialog(
                                mainPanel,
                                "Formato de texto del botón incorrecto: " + buttonText,
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
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
        cartaView.setVisible(true);
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

    public void agregarMesa() {
        Mesa nueva = mesaService.agregarMesa();
        JButton nuevaMesa = crearMesa(nueva);
        mainPanel.add(nuevaMesa);
        nuevaMesa.repaint();
        actualizarColorMesas();
    }

    public void agregarPared() {
        Pared nueva = mesaService.agregarPared();
        JButton nuevaPared = crearPared(nueva);
        mainPanel.add(nuevaPared);
        nuevaPared.repaint();
        actualizarColorMesas();
    }

    public void crearBotonesDeMesas(List<Mesa> mesas, List<Pared> paredes) {
        for (Mesa mesa : mesas) {
            JButton nuevaMesa = crearMesa(mesa);
            mainPanel.add(nuevaMesa);
        }

        for (Pared pared : paredes) {
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

                if (mesa.getEstado() == EstadoMesa.DISPONIBLE) {
                    // Mostrar la ventana para asignar mesero
                    AsignarMeseroView asignarMeseroViewFrame = new AsignarMeseroView(mesa, waiters);
                    asignarMeseroViewFrame.setVisible(true);

                    // Añadir un listener para cuando se cierre la ventana de asignar mesero
                    asignarMeseroViewFrame.addWindowListener(new java.awt.event.WindowAdapter() {
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
        PedidoView pedidoView = new PedidoView(mesa, productoService, this,credenciales);

        // Cerrar el panel de pedido actual si existe
        if (currentPedidoView != null) {
            currentPedidoView.setVisible(false);
            currentPedidoView.dispose(); // Liberar recursos del panel anterior
        }

        // Actualizar la referencia al panel actual
        currentPedidoView = pedidoView;

        pedidoView.setVisible(true);
    }
}