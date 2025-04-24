package es.daw.angelo.biblioteca.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void crearTablas(Connection conn) {
        String sqlAutor = "create table if not exists Autor (" +
                "id_persona INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "nacionalidad TEXT NOT NULL);";

        String sqlCategoria = "create table if not exists Categoria (" +
                "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL);";

        String sqlLibro = "create table if not exists Libro (" +
                "isbn TEXT PRIMARY KEY," +
                "titulo TEXT NOT NULL," +
                "anio_publicacion INTEGER NOT NULL," +
                "autor_id NUMBER NOT NULL," +
                "categoria_id NUMBER NOT NULL," +
                "FOREIGN KEY (autor_id) REFERENCES Autor(id)," +
                "FOREIGN KEY (categoria_id) REFERENCES Categoria(id));";

        try (Statement stmt = conn.createStatement()) {
            try {
                stmt.execute(sqlAutor);
                stmt.execute(sqlCategoria);
                stmt.execute(sqlLibro);
            } catch (SQLException e) {
                System.out.println("Error: tabla/s ya existente/s");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas");
        }
    }
}
