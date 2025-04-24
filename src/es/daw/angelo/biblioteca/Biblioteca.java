package es.daw.angelo.biblioteca;

import es.daw.angelo.biblioteca.config.ConexionDB;

import java.sql.*;

public class Biblioteca {
    public static void main(String[] args) {
        Connection conn = ConexionDB.conectar();
        ConexionDB.crearTablas(conn);
    }
}
