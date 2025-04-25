package es.daw.angelo.biblioteca.dao;

import es.daw.angelo.biblioteca.config.ConexionDB;
import es.daw.angelo.biblioteca.model.Autor;

import java.sql.*;
import java.util.ArrayList;

public class AutorDAO {
    public static void insertarAutor(Autor autor) {
        Connection conn = ConexionDB.conectar();
        String insertarAutor = "INSERT INTO Autor(nombre, nacionalidad) VALUES (?,?);";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(insertarAutor);
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getNacionalidad());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha insertado el registro correctamente");
            } else {
                System.out.println("Error al insertar el registro");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar un autor");
        }
    }

    public static void borrarAutor(Autor autor) {
        Connection conn = ConexionDB.conectar();
        String borrarAutor = "DELETE FROM AUTOR WHERE NOMBRE LIKE '?';";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(borrarAutor);
            stmt.setString(1, autor.getNombre());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha eliminado el registro correctamente");
            } else {
                System.out.println("Error al eliminar el registro");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar un autor");
        }
    }

    public static void editarAutor(Autor autor) {
        Connection conn = ConexionDB.conectar();
        String editarAutor = "UPDATE Autor SET nombre = ?, nacionalidad = ?;";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(editarAutor);
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getNacionalidad());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha modificado el registro correctamente");
            } else {
                System.out.println("Error al modificar el registro");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar un autor");
        }
    }

    public static ArrayList<Autor> listarAutores() {
        Connection conn = ConexionDB.conectar();
        ArrayList<Autor> listaAutores = new ArrayList<>();
        String leerAutores = "SELECT * FROM Autor;";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(leerAutores);
            boolean hayRegistros = false;
            while (rs.next()) {
                listaAutores.add(new Autor(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("nacionalidad")));
                hayRegistros = true;
            }
            if (hayRegistros) {
                System.out.println("Se han leído correctamente los registros");
            } else {
                System.out.println("No se han encontrado registros en la base de datos");
            }
        } catch (SQLException e) {
            System.out.println("Error al preparar la query");
        }
        return listaAutores;
    }

}
