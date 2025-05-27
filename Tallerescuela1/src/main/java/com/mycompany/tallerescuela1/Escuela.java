
package com.mycompany.tallerescuela1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Estudiante extends JFrame {
    private JTextField txtCodigo, txtNombre;
    private JButton btnIngresar, btnLimpiar;
    private JTable tablaEstudiantes;
    private DefaultTableModel modelo;

    public Estudiante() {
        setTitle("Registro de Estudiantes");
        setSize(500, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panelInput = new JPanel(new GridLayout(3, 2, 10, 10));
        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        btnIngresar = new JButton("Ingresar");
        btnLimpiar = new JButton("Limpiar");

        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInput.add(new JLabel("Código:"));
        panelInput.add(txtCodigo);
        panelInput.add(new JLabel("Nombre:"));
        panelInput.add(txtNombre);
        panelInput.add(btnIngresar);
        panelInput.add(btnLimpiar);

        modelo = new DefaultTableModel(new String[]{"Código", "Nombre"}, 0);
        tablaEstudiantes = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaEstudiantes);

        add(panelInput, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnIngresar.addActionListener(e -> insertarEstudiante());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        cargarEstudiantes();
    }

    private void insertarEstudiante() {
        int codigo = Integer.parseInt(txtCodigo.getText());
        String nombre = txtNombre.getText();

        String sql = "INSERT INTO estudiantes (cod_estudiante, nom_estudiante) VALUES (?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.setString(2, nombre);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Estudiante ingresado con éxito.");
            cargarEstudiantes();
            limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar estudiante: " + ex.getMessage());
        }
    }

    private void cargarEstudiantes() {
        modelo.setRowCount(0);

        String sql = "SELECT * FROM estudiantes";
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int cod = rs.getInt("cod_estudiante");
                String nom = rs.getString("nom_estudiante");
                modelo.addRow(new Object[]{cod, nom});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
    }
}