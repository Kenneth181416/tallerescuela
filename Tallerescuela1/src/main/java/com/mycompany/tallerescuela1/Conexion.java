
package com.mycompany.tallerescuela1;


public class Conexion {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/tallerescuela";
    private static final String USER = "root";
    private static final String PASSWORD = "Kenneth18"; 

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Conexión establecida con éxito");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos");
            e.printStackTrace();
        }
        return conn;
    }
}


