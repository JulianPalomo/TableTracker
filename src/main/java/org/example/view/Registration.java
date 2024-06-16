package org.example.view;

import org.example.models.Administrador;
import org.example.models.Mesero;
import org.example.models.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registration extends JDialog {
    private JTextField tfNombre;
    private JTextField tfEmail;
    private JTextField tfContraseña;
    private JTextField tfDNI;
    private JTextField tfConfirmarContraseña;
    private JButton btnRegistrar;
    private JButton btnCancel;
    private JComboBox cbTipoCuenta;
    private JPanel registerPanel;

    public Registration(JFrame parent) {
        super(parent);
        setTitle("Registro de Usuario");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Rellenar JComboBox
        cbTipoCuenta.addItem("Administrador");
        cbTipoCuenta.addItem("Mesero");

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
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

    private void registrarUsuario() {
        String nombre = tfNombre.getText();
        String email = tfEmail.getText();
        String contraseña = new String(String.valueOf(tfContraseña));
        String confirmarContraseña = new String(String.valueOf(tfConfirmarContraseña));
        String dni = tfDNI.getText();
        String tipoCuenta = (String) cbTipoCuenta.getSelectedItem();

        if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty() || dni.isEmpty() || tipoCuenta == null) {
            JOptionPane.showMessageDialog(this, "Por favor, rellene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contraseña.equals(confirmarContraseña)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aquí podrías añadir la lógica para crear el usuario en tu sistema.
        // Dependiendo del tipo de cuenta, creas un Administrador o Mesero.

        Usuario usuario;
        if (tipoCuenta.equals("Administrador")) {
            usuario = new Administrador(nombre, contraseña, nombre, dni);
        } else {
            usuario = new Mesero(nombre, contraseña, nombre, dni);
        }

        // Aquí deberías guardar el usuario en tu base de datos o sistema de almacenamiento
        // ...

        JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    public static void main(String[] args) {
        Registration dialog = new Registration(null);
    }
}

