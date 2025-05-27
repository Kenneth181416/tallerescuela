
package com.mycompany.tallerescuela1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class Curso extends JFrame {
    private JTextField txtCodigoCurso, txtNombreCurso;
    private JComboBox<String> comboDocentes;
    private JButton btnIngresar, btnLimpiar;
    private JTable tablaCursos;
    private DefaultTableModel modelo;

    private HashMap<String, Integer> docentesMap = new HashMap<>();

    public Curso() {
        setTitle("Registro de Cursos");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 10, 10));
        txtCodigoCurso = new JTextField();
        txtNombreCurso = new JTextField();
        comboDocentes = new JComboBox<>();
        btnIngresar = new JButton("Ingresar");
        btnLimpiar = new JButton("Limpiar");

        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInput.add(new JLabel("Código Curso:"));
        panelInput.add(txtCodigoCurso);
        panelInput.add(new JLabel("Nombre Curso:"));
        panelInput.add(txtNombreCurso);
        panelInput.add(new JLabel("Docente:"));
        panelInput.add(comboDocentes);
        panelInput.add(btnIngresar);
        panelInput.add(btnLimpiar);

        modelo = new DefaultTableModel(new String[]{"Código", "Curso", "Docente"}, 0);
        tablaCursos = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaCursos);

        add(panelInput, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnIngresar.addActionListener(e -> insertarCurso());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        cargarDocentes();
        cargarCursos();
    }

    private void cargarDocentes() {
        docentesMap.clear();
        comboDocentes.removeAllItems();
        String sql = "SELECT cod_docente, nom_docente FROM docentes";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("cod_docente");
                String nombre = rs.getString("nom_docente");
                comboDocentes.addItem(nombre);
                docentesMap.put(nombre, id);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar docentes: " + ex.getMessage());
        }
    }

    private void insertarCurso() {
        int codigo = Integer.parseInt(txtCodigoCurso.getText());
        String nombre = txtNombreCurso.getText();
        String docenteNombre = (String) comboDocentes.getSelectedItem();

        if (docenteNombre == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un docente.");
            return;
        }

        int codDocente = docentesMap.get(docenteNombre);
        String sql = "INSERT INTO cursos (cod_curso, nom_curso, cod_docente) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.setString(2, nombre);
            stmt.setInt(3, codDocente);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Curso ingresado con éxito.");
            cargarCursos();
            limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar curso: " + ex.getMessage());
        }
    }

    private void cargarCursos() {
        modelo.setRowCount(0);
        String sql = """
                SELECT c.cod_curso, c.nom_curso, d.nom_docente
                FROM cursos c
                JOIN docentes d ON c.cod_docente = d.cod_docente
                """;

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int cod = rs.getInt("cod_curso");
                String curso = rs.getString("nom_curso");
                String docente = rs.getString("nom_docente");
                modelo.addRow(new Object[]{cod, curso, docente});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar cursos: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtCodigoCurso.setText("");
        txtNombreCurso.setText("");
        comboDocentes.setSelectedIndex(-1);
    }
}