package org.example.view;

import org.example.models.Usuario;
import org.example.services.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class Login extends JDialog {
    private JTextField tfEmail;
    private JPasswordField tfContrasena;
    private JButton OKButton;
    private JButton cancelarButton;
    private JPanel loginPanel;
    private JLabel lblEmail;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Inicio de Sesión");

        initLoginPanel();

        if (loginPanel == null) {
            System.err.println("El panel de login no se ha inicializado correctamente.");
            return;
        }

        setContentPane(loginPanel);
        setMinimumSize(new Dimension(400, 300));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    autenticarUsuario();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        tfEmail = new JTextField(20);
        tfContrasena = new JPasswordField(20);
        OKButton = new JButton("OK");
        cancelarButton = new JButton("Cancelar");
        lblEmail = new JLabel("Email:");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(lblEmail);
        inputPanel.add(tfEmail);
        inputPanel.add(new JLabel("Contraseña:"));
        inputPanel.add(tfContrasena);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(OKButton);
        buttonPanel.add(cancelarButton);

        loginPanel.add(inputPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void autenticarUsuario() throws IOException {
        String email = tfEmail.getText();
        String contraseña = new String(tfContrasena.getPassword());

        List<Usuario> usuarios = UsuarioService.readUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(email) && usuario.getContrasena().equals(contraseña)) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new Menu(usuario.getTipoCuenta()); // Abrir menú principal según el rol
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Usuario o contraseña inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        Login dialog = new Login(null);
    }
}