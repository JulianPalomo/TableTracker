package org.example.view.panels;

import org.example.models.Administrador;
import org.example.models.Mesero;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
/*

public class Registration extends JDialog {
    private JTextField tfNombre;
    private JTextField tfEmail;
    private JPasswordField tfContraseña;
    private JTextField tfApellido;
    private JPasswordField tfConfirmarContraseña;
    private JButton btnRegistrar;
    private JButton btnCancel;
    private JComboBox<String> cbTipoCuenta;
    private JPanel registerPanel;
    private JTextField tfNombreUsuario;

    public Registration(JFrame parent) {
        super(parent);
        setTitle("Registro de Usuario");

        // Inicialización de componentes
        registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(8, 2));

        tfNombre = new JTextField();
        tfEmail = new JTextField();
        tfContraseña = new JPasswordField();
        tfApellido = new JTextField();
        tfConfirmarContraseña = new JPasswordField();
        tfNombreUsuario = new JTextField();
        cbTipoCuenta = new JComboBox<>();
        cbTipoCuenta.addItem("Administrador");
        cbTipoCuenta.addItem("Mesero");

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
        String email = tfEmail.getText();
        String contrasena = new String(tfContraseña.getPassword());
        String confirmarContraseña = new String(tfConfirmarContraseña.getPassword());
        String apellido = tfApellido.getText();
        String tipoCuentaStr = (String) cbTipoCuenta.getSelectedItem();
        String nombreUsuario = tfNombreUsuario.getText();

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || confirmarContraseña.isEmpty() || apellido.isEmpty() || tipoCuentaStr == null) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contrasena.equals(confirmarContraseña)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoCuenta tipoCuenta = TipoCuenta.valueOf(tipoCuentaStr.toUpperCase());
        Usuario usuario;

        if (tipoCuenta == TipoCuenta.ADMINISTRADOR) {
            usuario = new Administrador(nombreUsuario, contrasena, nombre, apellido, email, TipoCuenta.ADMINISTRADOR);
        } else {
            usuario = new Mesero(nombreUsuario, contrasena, nombre, apellido, email, TipoCuenta.MESERO);
        }

        List<Usuario> usuarios = UsuarioService.readUsuarios(); // Asegúrate de usar java.util.List
        usuarios.add(usuario);
        UsuarioService.writeUsuarios(usuarios);

        JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        Registration dialog = new Registration(null);
    }
}

 */