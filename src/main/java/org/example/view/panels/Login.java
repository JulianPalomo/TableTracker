package org.example.view.panels;

import org.example.models.TipoCuenta;
import org.example.models.Usuario;
import org.example.service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class Login extends JDialog {
    private JTextField tfEmailOrUsername;
    private JPasswordField tfContrasena;
    private JButton OKButton;
    private JButton cancelarButton;
    private JPanel loginPanel;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Inicio de Sesión");

        // Inicializar el loginPanel con GridBagLayout
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Crear y añadir componentes
        JLabel lblEmailOrUsername = new JLabel("Email o Nombre de Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(lblEmailOrUsername, gbc);

        tfEmailOrUsername = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(tfEmailOrUsername, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(lblContrasena, gbc);

        tfContrasena = new JPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        loginPanel.add(tfContrasena, gbc);

        OKButton = new JButton("OK");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginPanel.add(OKButton, gbc);

        cancelarButton = new JButton("Cancelar");
        gbc.gridx = 2;
        gbc.gridy = 2;
        loginPanel.add(cancelarButton, gbc);

        // Configurar la ventana de diálogo
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(400, 200));
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
        String emailOrUsername = tfEmailOrUsername.getText();
        String contraseña = new String(tfContrasena.getPassword());

        List<Usuario> usuarios = UsuarioService.readUsuarios();
        for (Usuario usuario : usuarios) {
            if ((usuario.getEmail().equals(emailOrUsername) || usuario.getNombreUsuario().equals(emailOrUsername))
                    && usuario.getContrasena().equals(contraseña)) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                MesasPanel mesasPanel = new MesasPanel("TableTracker", usuario.getTipoCuenta() == TipoCuenta.ADMINISTRADOR);
                mesasPanel.setVisible(true);
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