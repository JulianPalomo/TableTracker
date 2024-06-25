package org.example.view.panels;

import org.example.exceptions.LoginFailedException;
import org.example.models.Persona;
import org.example.models.Usuario;
import org.example.service.UsuarioSerializer;
import org.example.service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Login extends JDialog {
    private JTextField tfDni;
    private JPasswordField tfContrasena;
    private JButton OKButton;
    private JButton cancelarButton;
    private JPanel loginPanel;
    private JLabel lblDni;

    private UsuarioService personaService;
    private Usuario loginSuccessful;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Inicio de Sesión");

        loginSuccessful = null;
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        lblDni = new JLabel("DNI:");
        tfDni = new JTextField();
        JLabel lblContrasena = new JLabel("Contraseña:");
        tfContrasena = new JPasswordField();
        OKButton = new JButton("OK");
        cancelarButton = new JButton("Cancelar");

        loginPanel.add(lblDni);
        loginPanel.add(tfDni);
        loginPanel.add(lblContrasena);
        loginPanel.add(tfContrasena);
        loginPanel.add(OKButton);
        loginPanel.add(cancelarButton);

        setContentPane(loginPanel);
        setMinimumSize(new Dimension(400, 300));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        personaService = new UsuarioService();
        personaService.loadFromJson();

        OKButton.addActionListener(e -> {
            try {
                autenticarUsuario();
            } catch (LoginFailedException ex) {
                JOptionPane.showMessageDialog(Login.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        cancelarButton.addActionListener(e -> {
            dispose();
        });

        setVisible(true);
    }

    private Usuario autenticarUsuario() throws IOException, LoginFailedException {
        String dni = tfDni.getText();
        String contraseña = new String(tfContrasena.getPassword());

        Usuario isLoggedIn = personaService.login(dni, contraseña);

        if (isLoggedIn != null) {
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            loginSuccessful = isLoggedIn;
            dispose();  // Cerrar el diálogo de login
            return isLoggedIn;
        } else {
            throw new LoginFailedException("Usuario o contraseña inválido.");
        }
    }

    public Usuario isLoginSuccessful() {
        return loginSuccessful;
    }
}
