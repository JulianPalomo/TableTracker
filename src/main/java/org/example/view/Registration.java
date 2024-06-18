package org.example.view;

import org.example.models.Administrador;
import org.example.models.Mesero;
import org.example.models.Usuario;
import org.example.services.UsuarioService;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

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
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cbTipoCuenta.addItem("Administrador");
        cbTipoCuenta.addItem("Mesero");

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
        String tipoCuenta = (String) cbTipoCuenta.getSelectedItem();
        String nombreUsuario = tfNombreUsuario.getText();
        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || confirmarContraseña.isEmpty() || apellido.isEmpty() || tipoCuenta == null) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contrasena.equals(confirmarContraseña)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario;
        if (tipoCuenta.equals("Administrador")) {
            usuario = new Administrador(nombreUsuario,contrasena,nombre,apellido, email);
        } else {
            usuario = new Mesero(nombreUsuario,contrasena,nombre,apellido,email);
        }

        List<Usuario> usuarios = UsuarioService.readUsuarios();
        usuarios.add(usuario);
        UsuarioService.writeUsuarios(usuarios);

        JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        Registration dialog = new Registration(null);
    }
}