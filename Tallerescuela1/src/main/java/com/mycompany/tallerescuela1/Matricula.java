
package com.mycompany.tallerescuela1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class Matricula extends JFrame {
    private JComboBox<String> comboEstudiantes, comboCursos;
    private JTextField txtNota;
    private JButton btnMatricular, btnLimpiar;
    private JTable tablaMatricula;
    private DefaultTableModel modelo;

    private HashMap<String, Integer> estudiantesMap = new HashMap<>();
    private HashMap<String, Integer> cursosMap = new HashMap<>();

    public Matricula() {
        setTitle("Matrícula de Estudiantes");
        setSize(700, 450);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 10, 10));
        comboEstudiantes = new JComboBox<>();
        comboCursos = new JComboBox<>();
        txtNota = new JTextField();
        btnMatricular = new JButton("Matricular");
        btnLimpiar = new JButton("Limpiar");

        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInput.add(new JLabel("Estudiante:"));
        panelInput.add(comboEstudiantes);
        panelInput.add(new JLabel("Curso:"));
        panelInput.add(comboCursos);
        panelInput.add(new JLabel("Nota:"));
        panelInput.add(txtNota);
        panelInput.add(btnMatricular);
        panelInput.add(btnLimpiar);

        modelo = new DefaultTableModel(new String[]{"Estudiante", "Curso", "Nota"}, 0);
        tablaMatricula = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaMatricula);

        add(panelInput, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnMatricular.addActionListener(e -> insertarMatricula());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        cargarEstudiantes();
        cargarCursos();
        cargarMatriculas();
    }

    private void cargarEstudiantes() {
        estudiantesMap.clear();
        comboEstudiantes.removeAllItems();
        String sql = "SELECT cod_estudiante, nom_estudiante FROM estudiantes";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int cod = rs.getInt("cod_estudiante");
                String nombre = rs.getString("nom_estudiante");
                comboEstudiantes.addItem(nombre);
                estudiantesMap.put(nombre, cod);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes: " + ex.getMessage());
        }
    }

    private void cargarCursos() {
        cursosMap.clear();
        comboCursos.removeAllItems();
        String sql = "SELECT cod_curso, nom_curso FROM cursos";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int cod = rs.getInt("cod_curso");
                String nombre = rs.getString("nom_curso");
                comboCursos.addItem(nombre);
                cursosMap.put(nombre, cod);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar cursos: " + ex.getMessage());
        }
    }

    private void insertarMatricula() {
        String estudianteNombre = (String) comboEstudiantes.getSelectedItem();
        String cursoNombre = (String) comboCursos.getSelectedItem();
        float nota = Float.parseFloat(txtNota.getText());

        if (estudianteNombre == null || cursoNombre == null) {
            JOptionPane.showMessageDialog(this, "Selecciona estudiante y curso.");
            return;
        }

        int codEstudiante = estudiantesMap.get(estudianteNombre);
        int codCurso = cursosMap.get(cursoNombre);
        String sql = "INSERT INTO matricula (cod_estudiante, cod_curso, nota_curso) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codEstudiante);
            stmt.setInt(2, codCurso);
            stmt.setFloat(3, nota);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Matrícula registrada con éxito.");
            cargarMatriculas();
            limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al matricular: " + ex.getMessage());
        }
    }

    private void cargarMatriculas() {
        modelo.setRowCount(0);
        String sql = """
                SELECT e.nom_estudiante, c.nom_curso, m.nota_curso
                FROM matricula m
                JOIN estudiantes e ON m.cod_estudiante = e.cod_estudiante
                JOIN cursos c ON m.cod_curso = c.cod_curso
                """;
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String estudiante = rs.getString("nom_estudiante");
                String curso = rs.getString("nom_curso");
                float nota = rs.getFloat("nota_curso");
                modelo.addRow(new Object[]{estudiante, curso, nota});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error no se puede puede caragar matricula: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNota.setText("");
        comboEstudiantes.setSelectedIndex(-1);
        comboCursos.setSelectedIndex(-1);
    }
}