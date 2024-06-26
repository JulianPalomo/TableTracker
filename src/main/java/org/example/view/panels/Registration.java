package org.example.view.panels;

import org.example.models.Administrador;
import org.example.models.Credenciales;
import org.example.models.Mesero;
import org.example.models.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.service.UsuarioService;

public class Registration extends JDialog {
    private JTextField tfNombre;
    private JTextField tfEmail;
    private JPasswordField tfContraseña;
    private JTextField tfApellido;
    private JPasswordField tfConfirmarContraseña;
    private JTextField tfNombreUsuario;
    private JTextField tfDni;
    private JButton btnRegistrar;
    private JButton btnCancel;
    private JComboBox<String> cbTipoCuenta;
    private JPanel registerPanel;
    private UsuarioService usuarioService; // Add the UsuarioService instance

    public Registration(JFrame parent) {
        super(parent);
        setTitle("Registro de Usuario");
        usuarioService = new UsuarioService(); // Initialize the UsuarioService instance
        usuarioService.loadFromJson(); // Load existing users from JSON

        // Inicialización de componentes
        registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(10, 2));

        tfNombre = new JTextField();
        tfEmail = new JTextField();
        tfContraseña = new JPasswordField();
        tfApellido = new JTextField();
        tfConfirmarContraseña = new JPasswordField();
        tfNombreUsuario = new JTextField();
        tfDni = new JTextField();
        cbTipoCuenta = new JComboBox<>();
        cbTipoCuenta.addItem("ADMINISTRADOR");
        cbTipoCuenta.addItem("MESERO");
        cbTipoCuenta.addItem("CAJERO");

        btnRegistrar = new JButton("Registrar");
        btnCancel = new JButton("Cancelar");

        // Añadir componentes al panel
        registerPanel.add(new JLabel("Nombre:"));
        registerPanel.add(tfNombre);
        registerPanel.add(new JLabel("Apellido:"));
        registerPanel.add(tfApellido);
        registerPanel.add(new JLabel("Email:"));
        registerPanel.add(tfEmail);
        registerPanel.add(new JLabel("Nombre de Usuario:"));
        registerPanel.add(tfNombreUsuario);
        registerPanel.add(new JLabel("DNI:"));
        registerPanel.add(tfDni);
        registerPanel.add(new JLabel("Contraseña:"));
        registerPanel.add(tfContraseña);
        registerPanel.add(new JLabel("Confirmar Contraseña:"));
        registerPanel.add(tfConfirmarContraseña);
        registerPanel.add(new JLabel("Tipo de Cuenta:"));
        registerPanel.add(cbTipoCuenta);
        registerPanel.add(btnRegistrar);
        registerPanel.add(btnCancel);

        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registrarUsuario();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registrarUsuario() throws IOException {
        String nombre = tfNombre.getText();
        String apellido = tfApellido.getText();
        String email = tfEmail.getText();
        String nombreUsuario = tfNombreUsuario.getText();
        String dni = tfDni.getText();
        String contrasena = new String(tfContraseña.getPassword());
        String confirmarContraseña = new String(tfConfirmarContraseña.getPassword());
        String tipoCuentaStr = (String) cbTipoCuenta.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || nombreUsuario.isEmpty() || dni.isEmpty() || contrasena.isEmpty() || confirmarContraseña.isEmpty() || tipoCuentaStr == null) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contrasena.equals(confirmarContraseña)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Credenciales credenciales;
        switch (tipoCuentaStr) {
            case "ADMINISTRADOR":
                credenciales = Credenciales.ADMINISTRADOR;
                break;
            case "MESERO":
                credenciales = Credenciales.MESERO;
                break;
            case "CAJERO":
                credenciales = Credenciales.CAJERO;
                break;
            default:
                throw new IllegalArgumentException("Tipo de cuenta no válido: " + tipoCuentaStr);
        }

        Usuario usuario = new Usuario(nombre, apellido, dni, contrasena, credenciales);

        usuarioService.addUsuario(usuario);
        usuarioService.saveToJson(); // Save the updated list of users to JSON

        JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        Registration dialog = new Registration(null);
    }
}