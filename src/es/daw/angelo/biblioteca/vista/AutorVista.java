package es.daw.angelo.biblioteca.vista;

import es.daw.angelo.biblioteca.dao.AutorDAO;
import es.daw.angelo.biblioteca.model.Autor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AutorVista extends JPanel {
    private JTable tablaAutores;
    private DefaultTableModel modeloTabla;
    
    public AutorVista() {
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        JButton insertarAutor = new JButton("Añadir");
        JButton editarAutor = new JButton("Editar");
        JButton eliminarAutor = new JButton("Eliminar");
        JButton actualizarTabla = new JButton("Actualizar");
        
        panelBotones.add(insertarAutor);
        panelBotones.add(editarAutor);
        panelBotones.add(eliminarAutor);
        panelBotones.add(actualizarTabla);

        String[] columnas = {"ID", "Nombre", "Nacionalidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaAutores = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaAutores);

        add(panelBotones, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        insertarAutor.addActionListener(e -> mostrarDialogoInsertar());
        editarAutor.addActionListener(e -> mostrarDialogoEditar());
        eliminarAutor.addActionListener(e -> eliminarAutorSeleccionado());
        actualizarTabla.addActionListener(e -> cargarDatos());
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        ArrayList<Autor> autores = AutorDAO.listarAutores();
        for (Autor autor : autores) {
            Object[] fila = {
                autor.getId_autor(),
                autor.getNombre(),
                autor.getNacionalidad()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarDialogoInsertar() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Añadir Nuevo Autor", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(parentWindow);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        JTextField nombreField = new JTextField(15);
        panel.add(nombreField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nacionalidad:"), gbc);
        gbc.gridx = 1;
        JTextField nacionalidadField = new JTextField(15);
        panel.add(nacionalidadField, gbc);

        JPanel botones = new JPanel();
        JButton confirmar = new JButton("Añadir");
        JButton cancelar = new JButton("Cancelar");
        
        confirmar.addActionListener(e -> {
            if (!nombreField.getText().isEmpty() && !nacionalidadField.getText().isEmpty()) {
                Autor autor = new Autor(0, nombreField.getText(), nacionalidadField.getText());
                AutorDAO.insertarAutor(autor);
                cargarDatos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, rellene todos los campos");
            }
        });
        
        cancelar.addActionListener(e -> dialog.dispose());
        
        botones.add(confirmar);
        botones.add(cancelar);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(botones, gbc);

        dialog.setContentPane(panel);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void mostrarDialogoEditar() {
        int filaSeleccionada = tablaAutores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un autor para editar");
            return;
        }

        int id = (int) tablaAutores.getValueAt(filaSeleccionada, 0);
        String nombreActual = (String) tablaAutores.getValueAt(filaSeleccionada, 1);
        String nacionalidadActual = (String) tablaAutores.getValueAt(filaSeleccionada, 2);

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Editar Autor", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(parentWindow);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        JTextField nombreField = new JTextField(nombreActual, 15);
        panel.add(nombreField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nacionalidad:"), gbc);
        gbc.gridx = 1;
        JTextField nacionalidadField = new JTextField(nacionalidadActual, 15);
        panel.add(nacionalidadField, gbc);

        JPanel botones = new JPanel();
        JButton confirmar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        
        confirmar.addActionListener(e -> {
            if (!nombreField.getText().isEmpty() && !nacionalidadField.getText().isEmpty()) {
                Autor autor = new Autor(id, nombreField.getText(), nacionalidadField.getText());
                AutorDAO.editarAutor(autor);
                cargarDatos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, rellene todos los campos");
            }
        });
        
        cancelar.addActionListener(e -> dialog.dispose());
        
        botones.add(confirmar);
        botones.add(cancelar);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(botones, gbc);

        dialog.setContentPane(panel);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void eliminarAutorSeleccionado() {
        int filaSeleccionada = tablaAutores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un autor para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar este autor?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
                
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = (int) tablaAutores.getValueAt(filaSeleccionada, 0);
            String nombre = (String) tablaAutores.getValueAt(filaSeleccionada, 1);
            String nacionalidad = (String) tablaAutores.getValueAt(filaSeleccionada, 2);
            
            Autor autor = new Autor(id, nombre, nacionalidad);
            AutorDAO.borrarAutor(autor);
            cargarDatos();
        }
    }
}