package org.example.view.panels;

import org.example.exceptions.LoginFailedException;
import org.example.models.Persona;
import org.example.service.PersonaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class Login extends JDialog {
    private JTextField tfDni;
    private JPasswordField tfContrasena;
    private JButton OKButton;
    private JButton cancelarButton;
    private JPanel loginPanel;
    private JLabel lblDni;

    private PersonaService personaService;
    private boolean loginSuccessful;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Inicio de Sesión");

        // Inicializar el loginPanel
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

        // Configurar la ventana de diálogo
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(400, 300));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Inicializar PersonaService
        personaService = new PersonaService();

        personaService.loadFromJson();


        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    autenticarUsuario();
                } catch (LoginFailedException ex) {
                    JOptionPane.showMessageDialog(Login.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginSuccessful = false;
                dispose();
            }
        });

        setVisible(true);
    }

    private boolean autenticarUsuario() throws IOException, LoginFailedException {
        String dni = tfDni.getText();
        String contraseña = new String(tfContrasena.getPassword());

        boolean isLoggedIn = personaService.adminLogin(dni, contraseña);
        if (isLoggedIn) {
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            loginSuccessful = true;

            return true;
        } else {
            throw new LoginFailedException("Usuario o contraseña inválido.");
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Login loginDialog = new Login(frame);
        boolean loggedIn = loginDialog.isLoginSuccessful();
        System.out.println("Login successful: " + loggedIn);
    }
}