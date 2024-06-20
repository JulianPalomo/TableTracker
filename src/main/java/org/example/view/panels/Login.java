package org.example.view.panels;

import org.example.models.Usuario;
import org.example.service.UsuarioService;

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

        // Inicializar el loginPanel
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        lblEmail = new JLabel("Email:");
        tfEmail = new JTextField();
        JLabel lblContrasena = new JLabel("Contraseña:");
        tfContrasena = new JPasswordField();
        OKButton = new JButton("OK");
        cancelarButton = new JButton("Cancelar");

        loginPanel.add(lblEmail);
        loginPanel.add(tfEmail);
        loginPanel.add(lblContrasena);
        loginPanel.add(tfContrasena);
        loginPanel.add(OKButton);
        loginPanel.add(cancelarButton);

        // Configurar la ventana de diálogo
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

    private void autenticarUsuario() throws IOException {
        String email = tfEmail.getText();
        String contraseña = tfContrasena.getText();

        List<Usuario> usuarios = UsuarioService.readUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email) && usuario.getContrasena().equals(contraseña)) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                new Menu(usuario.getTipoCuenta());
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