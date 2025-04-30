package es.daw.angelo.biblioteca.vista;

import javax.swing.*;

public class BibliotecaApp extends JFrame {
    public BibliotecaApp(){
        initcomponents();
    }

    private void initcomponents() {
        setTitle("Biblioteca");
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane menu = new JTabbedPane();
        menu.addTab("Autor", new AutorVista());
        menu.addTab("Categoría", new CategoriaVista());
        menu.addTab("Libro", new LibroVista());

        add(menu);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}