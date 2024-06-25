package org.example.view.panels;

import org.example.exceptions.LoginFailedException;
import org.example.models.Usuario;
import org.example.service.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class Login extends JDialog {
    private JTextField tfDni;
    private JPasswordField tfContrasena;
    private JButton OKButton;
    private JButton cancelarButton;
    private JPanel loginPanel;
    private JLabel lblDni;

    private JLabel inicioSesionLabel;

    private UsuarioService personaService;
    private Usuario loginSuccessful;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Inicio de Sesión");
        setUndecorated(true);

        loginSuccessful = null;
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 223, 186));
        loginPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        inicioSesionLabel = new JLabel("Iniciar Sesión");
        inicioSesionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        inicioSesionLabel.setForeground(new Color(102, 51, 0)); // Color de texto


        lblDni = new JLabel("DNI:");
        lblDni.setFont(new Font("Arial", Font.BOLD, 14));
        lblDni.setForeground(new Color(102, 51, 0)); // Color de texto

        tfDni = new JTextField(15);
        tfDni.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        lblContrasena.setForeground(new Color(102, 51, 0)); // Color de texto

        tfContrasena = new JPasswordField(15);
        tfContrasena.setFont(new Font("Arial", Font.PLAIN, 14));

        OKButton = new JButton("OK");
        OKButton.setFont(new Font("Arial", Font.BOLD, 14));
        OKButton.setBackground(new Color(255, 153, 51)); // Botón naranja
        OKButton.setForeground(Color.WHITE);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelarButton.setBackground(new Color(255, 153, 51)); // Botón naranja
        cancelarButton.setForeground(Color.WHITE);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        Dimension buttonSize = new Dimension(100, 30); // Ancho x Alto

        gbc.gridx = 1;
        loginPanel.add(inicioSesionLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(lblDni, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(tfDni, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(lblContrasena, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(tfContrasena, gbc);

        gbc.gridy = 3;
        OKButton.setPreferredSize(buttonSize);
        loginPanel.add(OKButton, gbc);

        gbc.gridy = 4;
        cancelarButton.setPreferredSize(buttonSize);
        loginPanel.add(cancelarButton, gbc);

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
