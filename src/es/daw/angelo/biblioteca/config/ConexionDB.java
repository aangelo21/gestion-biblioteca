package es.daw.angelo.biblioteca.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    public static Connection conectar() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
            System.out.println("Conexión a la base de datos establecida");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver no encontrado");
        }
        return conn;
    }
}
