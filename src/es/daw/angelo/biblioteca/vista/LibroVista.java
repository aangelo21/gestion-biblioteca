package es.daw.angelo.biblioteca.vista;

import es.daw.angelo.biblioteca.dao.LibroDAO;
import es.daw.angelo.biblioteca.dao.AutorDAO;
import es.daw.angelo.biblioteca.dao.CategoriaDAO;
import es.daw.angelo.biblioteca.model.Libro;
import es.daw.angelo.biblioteca.model.Autor;
import es.daw.angelo.biblioteca.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class LibroVista extends JPanel {
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    
    public LibroVista() {
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        JButton insertarLibro = new JButton("Añadir");
        JButton editarLibro = new JButton("Editar");
        JButton eliminarLibro = new JButton("Eliminar");
        JButton actualizarTabla = new JButton("Actualizar");
        
        panelBotones.add(insertarLibro);
        panelBotones.add(editarLibro);
        panelBotones.add(eliminarLibro);
        panelBotones.add(actualizarTabla);

        String[] columnas = {"ISBN", "Título", "Año", "Autor ID", "Categoría ID"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaLibros = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);

        add(panelBotones, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        insertarLibro.addActionListener(e -> mostrarDialogoInsertar());
        editarLibro.addActionListener(e -> mostrarDialogoEditar());
        eliminarLibro.addActionListener(e -> eliminarLibroSeleccionado());
        actualizarTabla.addActionListener(e -> cargarDatos());
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        ArrayList<Libro> libros = LibroDAO.listarLibros();
        for (Libro libro : libros) {
            Object[] fila = {
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getAnio_publicacion(),
                libro.getAutor_id(),
                libro.getCategoria_id()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarDialogoInsertar() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Añadir Nuevo Libro", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentWindow);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        JTextField isbnField = new JTextField(15);
        panel.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        JTextField tituloField = new JTextField(15);
        panel.add(tituloField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        JTextField anioField = new JTextField(15);
        panel.add(anioField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1;
        JComboBox<Autor> autorCombo = new JComboBox<>();
        ArrayList<Autor> autores = AutorDAO.listarAutores();
        for (Autor autor : autores) {
            autorCombo.addItem(autor);
        }
        panel.add(autorCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        JComboBox<Categoria> categoriaCombo = new JComboBox<>();
        ArrayList<Categoria> categorias = CategoriaDAO.listarCategorias();
        for (Categoria categoria : categorias) {
            categoriaCombo.addItem(categoria);
        }
        panel.add(categoriaCombo, gbc);

        JPanel botones = new JPanel();
        JButton confirmar = new JButton("Añadir");
        JButton cancelar = new JButton("Cancelar");
        
        confirmar.addActionListener(e -> {
            if (!isbnField.getText().isEmpty() && !tituloField.getText().isEmpty() && 
                !anioField.getText().isEmpty() && autorCombo.getSelectedItem() != null && 
                categoriaCombo.getSelectedItem() != null) {
                try {
                    int anio = Integer.parseInt(anioField.getText());
                    Autor autorSeleccionado = (Autor) autorCombo.getSelectedItem();
                    Categoria categoriaSeleccionada = (Categoria) categoriaCombo.getSelectedItem();
                    
                    Libro libro = new Libro(
                        isbnField.getText(),
                        tituloField.getText(),
                        anio,
                        autorSeleccionado.getId_autor(),
                        categoriaSeleccionada.getId_categoria()
                    );
                    LibroDAO.insertarLibro(libro);
                    cargarDatos();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "El año debe ser un número válido");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, rellene todos los campos");
            }
        });
        
        cancelar.addActionListener(e -> dialog.dispose());
        
        botones.add(confirmar);
        botones.add(cancelar);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(botones, gbc);

        dialog.setContentPane(panel);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void mostrarDialogoEditar() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro para editar");
            return;
        }

        String isbn = (String) tablaLibros.getValueAt(filaSeleccionada, 0);
        String titulo = (String) tablaLibros.getValueAt(filaSeleccionada, 1);
        int anio = (int) tablaLibros.getValueAt(filaSeleccionada, 2);
        int autorId = (int) tablaLibros.getValueAt(filaSeleccionada, 3);
        int categoriaId = (int) tablaLibros.getValueAt(filaSeleccionada, 4);

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Editar Libro", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentWindow);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        JTextField isbnField = new JTextField(isbn, 15);
        isbnField.setEditable(false);
        panel.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        JTextField tituloField = new JTextField(titulo, 15);
        panel.add(tituloField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        JTextField anioField = new JTextField(String.valueOf(anio), 15);
        panel.add(anioField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1;
        JComboBox<Autor> autorCombo = new JComboBox<>();
        ArrayList<Autor> autores = AutorDAO.listarAutores();
        for (Autor autor : autores) {
            autorCombo.addItem(autor);
            if (autor.getId_autor() == autorId) {
                autorCombo.setSelectedItem(autor);
            }
        }
        panel.add(autorCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        JComboBox<Categoria> categoriaCombo = new JComboBox<>();
        ArrayList<Categoria> categorias = CategoriaDAO.listarCategorias();
        for (Categoria categoria : categorias) {
            categoriaCombo.addItem(categoria);
            if (categoria.getId_categoria() == categoriaId) {
                categoriaCombo.setSelectedItem(categoria);
            }
        }
        panel.add(categoriaCombo, gbc);

        JPanel botones = new JPanel();
        JButton confirmar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        
        confirmar.addActionListener(e -> {
            if (!tituloField.getText().isEmpty() && !anioField.getText().isEmpty() && 
                autorCombo.getSelectedItem() != null && categoriaCombo.getSelectedItem() != null) {
                try {
                    int nuevoAnio = Integer.parseInt(anioField.getText());
                    Autor autorSeleccionado = (Autor) autorCombo.getSelectedItem();
                    Categoria categoriaSeleccionada = (Categoria) categoriaCombo.getSelectedItem();
                    
                    Libro libro = new Libro(
                        isbn,
                        tituloField.getText(),
                        nuevoAnio,
                        autorSeleccionado.getId_autor(),
                        categoriaSeleccionada.getId_categoria()
                    );
                    LibroDAO.editarLibro(libro);
                    cargarDatos();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "El año debe ser un número válido");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, rellene todos los campos");
            }
        });
        
        cancelar.addActionListener(e -> dialog.dispose());
        
        botones.add(confirmar);
        botones.add(cancelar);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(botones, gbc);

        dialog.setContentPane(panel);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void eliminarLibroSeleccionado() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar este libro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
                
        if (confirmacion == JOptionPane.YES_OPTION) {
            String isbn = (String) tablaLibros.getValueAt(filaSeleccionada, 0);
            String titulo = (String) tablaLibros.getValueAt(filaSeleccionada, 1);
            int anio = (int) tablaLibros.getValueAt(filaSeleccionada, 2);
            int autorId = (int) tablaLibros.getValueAt(filaSeleccionada, 3);
            int categoriaId = (int) tablaLibros.getValueAt(filaSeleccionada, 4);
            
            Libro libro = new Libro(isbn, titulo, anio, autorId, categoriaId);
            LibroDAO.borrarLibro(libro);
            cargarDatos();
        }
    }
}