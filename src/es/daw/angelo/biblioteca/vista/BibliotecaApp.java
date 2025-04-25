package es.daw.angelo.biblioteca.vista;

import javax.swing.*;

public class BibliotecaApp extends JFrame {
    public BibliotecaApp(){
        initcomponents();
    }

    private void initcomponents() {
        setTitle("Biblioteca");
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane menu = new JTabbedPane();
        menu.addTab("Autor", new AutorVista());

        add(menu);
        setVisible(true);
    }
}
