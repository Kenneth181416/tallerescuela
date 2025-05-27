
package com.mycompany.tallerescuela1;

import javax.swing.*;
import java.awt.event.*;

public class Menu extends JFrame {

    public Menu() {
        setTitle("Plataforma de Gestión Educativa");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuIngresar = new JMenu("Registrar");
        JMenuItem menuEstudiantes = new JMenuItem("Alumnos");
        JMenuItem menuDocentes = new JMenuItem("Profesores");
        JMenuItem menuCursos = new JMenuItem("Asignaturas");

        JMenu menuMatricula = new JMenu("Inscripción");
        JMenuItem menuMatricular = new JMenuItem("Inscribir Curso");

        JMenu menuNotas = new JMenu("Calificaciones");
        JMenuItem menuNotasVista = new JMenuItem("Consultar Calificaciones");

        JMenu menuSalir = new JMenu("Cerrar");
        JMenuItem salirItem = new JMenuItem("Salir de la aplicación");

        menuIngresar.add(menuEstudiantes);
        menuIngresar.add(menuDocentes);
        menuIngresar.add(menuCursos);
        menuMatricula.add(menuMatricular);
        menuNotas.add(menuNotasVista);
        menuSalir.add(salirItem);

        menuBar.add(menuIngresar);
        menuBar.add(menuMatricula);
        menuBar.add(menuNotas);
        menuBar.add(menuSalir);
        setJMenuBar(menuBar);

        menuEstudiantes.addActionListener(e -> new Estudiante().setVisible(true));
        menuDocentes.addActionListener(e -> new Docente().setVisible(true));
        menuCursos.addActionListener(e -> new Curso().setVisible(true));
        menuMatricular.addActionListener(e -> new Matricula().setVisible(true));
        menuNotasVista.addActionListener(e -> new Nota().setVisible(true));
        salirItem.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}
