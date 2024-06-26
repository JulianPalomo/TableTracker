package org.example.view;

import org.example.models.personas.Usuario;
import org.example.service.Usuario.UsuarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Set;

public class UsuariosView extends JPanel {

    private UsuarioService usuarioService;
    private DefaultTableModel tableModel;
    private JTable userTable;

    public UsuariosView() {
        usuarioService = new UsuarioService();
        setLayout(new BorderLayout());

        usuarioService.loadFromJson();

        // Configurar la tabla de usuarios
        String[] columnNames = {"Nombre y Apellido", "DNI", "Contraseña", "Credenciales"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);

        // Cargar usuarios desde el servicio
        cargarUsuarios();

        // Configurar el panel
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Botón para actualizar la lista de usuarios
        JButton refreshButton = new JButton("Actualizar");
        JButton eliminarUsuario = new JButton("Eliminar Usuario");
        JButton agregarUsuario = new JButton("Agregar Usuario");

        eliminarUsuario.addActionListener(e -> {
            eliminarUsuarioSeleccionado();

            try {
                usuarioService.saveToJson();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        agregarUsuario.addActionListener(e -> new Registration());

        refreshButton.addActionListener(e -> {
            tableModel.setRowCount(0); // Limpiar la tabla
            cargarUsuarios(); // Recargar los usuarios
        });

        // Añadir los botones al panel de botones
        buttonPanel.add(refreshButton);
        buttonPanel.add(eliminarUsuario);
        buttonPanel.add(agregarUsuario);

        // Añadir el panel de botones a la región SOUTH del BorderLayout
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void eliminarUsuarioSeleccionado() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String dni = (String) tableModel.getValueAt(selectedRow, 1);
            String contraseña = (String) tableModel.getValueAt(selectedRow, 2);

            // Verificar si el usuario a eliminar es el admin predeterminado
            if ("0000".equals(dni) && "admin".equals(contraseña)) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar el usuario admin predeterminado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                usuarioService.eliminarPorDni(dni);
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarUsuarios() {
        usuarioService.loadFromJson();
        Set<Usuario> usuarios = usuarioService.getUsuarios();
        for (Usuario usuario : usuarios) {
            String nombreApellido = usuario.getNombre() + " " + usuario.getApellido();
            String dni = usuario.getDni();
            String contraseña = usuario.getPassword();
            String credenciales = usuario.getCredenciales().name();
            Object[] rowData = {nombreApellido, dni, contraseña, credenciales};
            tableModel.addRow(rowData);
        }
    }
}
