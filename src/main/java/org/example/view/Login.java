package org.example.view;

import org.example.models.Usuario;
import org.example.services.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Login extends JDialog {
    private JTextField tfEmail;
    private JPasswordField tfContraseña;
    private JButton OKButton;
    private JButton cancelarButton;
    private JPanel loginPanel;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Inicio de Sesión");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(400, 300));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
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

    private void autenticarUsuario() throws IOException {
        String email = tfEmail.getText();
        String contraseña = new String(tfContraseña.getPassword());

            List<Usuario> usuarios = UsuarioService.readUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(email) && usuario.getContrasena().equals(contraseña)) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Usuario o contraseña inválido.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        Login dialog = new Login(null);
    }
}