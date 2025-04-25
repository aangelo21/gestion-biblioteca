package es.daw.angelo.biblioteca.dao;

import es.daw.angelo.biblioteca.config.ConexionDB;
import es.daw.angelo.biblioteca.model.Libro;

import java.sql.*;
import java.util.ArrayList;

public class LibroDAO {
    public static void insertarLibro(Libro libro) {
        Connection conn = ConexionDB.conectar();
        String insertarLibro = "INSERT INTO Libro(isbn, titulo, anio_publicacion, autor_id, categoria_id) VALUES (?,?,?,?,?);";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(insertarLibro);
            stmt.setString(1, libro.getIsbn());
            stmt.setString(2, libro.getTitulo());
            stmt.setInt(3, libro.getAnio_publicacion());
            stmt.setInt(4, libro.getAutor_id());
            stmt.setInt(5, libro.getCategoria_id());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha insertado el libro correctamente");
            } else {
                System.out.println("Error al insertar el libro");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar un libro");
        }
    }

    public static void borrarLibro(Libro libro) {
        Connection conn = ConexionDB.conectar();
        String borrarLibro = "DELETE FROM Libro WHERE isbn = ?;";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(borrarLibro);
            stmt.setString(1, libro.getIsbn());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha eliminado el libro correctamente");
            } else {
                System.out.println("Error al eliminar el libro");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar un libro");
        }
    }

    public static void editarLibro(Libro libro) {
        Connection conn = ConexionDB.conectar();
        String editarLibro = "UPDATE Libro SET titulo = ?, anio_publicacion = ?, autor_id = ?, categoria_id = ? WHERE isbn = ?;";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(editarLibro);
            stmt.setString(1, libro.getTitulo());
            stmt.setInt(2, libro.getAnio_publicacion());
            stmt.setInt(3, libro.getAutor_id());
            stmt.setInt(4, libro.getCategoria_id());
            stmt.setString(5, libro.getIsbn());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha modificado el libro correctamente");
            } else {
                System.out.println("Error al modificar el libro");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar un libro");
        }
    }

    public static ArrayList<Libro> listarLibros() {
        Connection conn = ConexionDB.conectar();
        ArrayList<Libro> listaLibros = new ArrayList<>();
        String leerLibros = "SELECT * FROM Libro;";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(leerLibros);
            boolean hayRegistros = false;
            while (rs.next()) {
                listaLibros.add(new Libro(
                        rs.getString("isbn"),
                        rs.getString("titulo"),
                        rs.getInt("anio_publicacion"),
                        rs.getInt("autor_id"),
                        rs.getInt("categoria_id")
                ));
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
        return listaLibros;
    }
}