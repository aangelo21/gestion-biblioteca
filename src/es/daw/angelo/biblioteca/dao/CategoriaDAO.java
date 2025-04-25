package es.daw.angelo.biblioteca.dao;

import es.daw.angelo.biblioteca.config.ConexionDB;
import es.daw.angelo.biblioteca.model.Categoria;

import java.sql.*;
import java.util.ArrayList;

public class CategoriaDAO {
    public static void insertarCategoria(Categoria categoria) {
        Connection conn = ConexionDB.conectar();
        String insertarCategoria = "INSERT INTO Categoria(nombre) VALUES (?);";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(insertarCategoria);
            stmt.setString(1, categoria.getNombre());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha insertado la categoría correctamente");
            } else {
                System.out.println("Error al insertar la categoría");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar una categoría");
        }
    }

    public static void borrarCategoria(Categoria categoria) {
        Connection conn = ConexionDB.conectar();
        String borrarCategoria = "DELETE FROM Categoria WHERE id_categoria = ?;";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(borrarCategoria);
            stmt.setInt(1, categoria.getId_categoria());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha eliminado la categoría correctamente");
            } else {
                System.out.println("Error al eliminar la categoría");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar una categoría");
        }
    }

    public static void editarCategoria(Categoria categoria) {
        Connection conn = ConexionDB.conectar();
        String editarCategoria = "UPDATE Categoria SET nombre = ? WHERE id_categoria = ?;";
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(editarCategoria);
            stmt.setString(1, categoria.getNombre());
            stmt.setInt(2, categoria.getId_categoria());
            int resultado = stmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Se ha modificado la categoría correctamente");
            } else {
                System.out.println("Error al modificar la categoría");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar una categoría");
        }
    }

    public static ArrayList<Categoria> listarCategorias() {
        Connection conn = ConexionDB.conectar();
        ArrayList<Categoria> listaCategorias = new ArrayList<>();
        String leerCategorias = "SELECT * FROM Categoria;";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(leerCategorias);
            boolean hayRegistros = false;
            while (rs.next()) {
                listaCategorias.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
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
        return listaCategorias;
    }
}