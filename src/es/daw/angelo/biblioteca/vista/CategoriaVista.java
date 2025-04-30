package es.daw.angelo.biblioteca.vista;

import es.daw.angelo.biblioteca.dao.CategoriaDAO;
import es.daw.angelo.biblioteca.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CategoriaVista extends JPanel {
    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;
    
    public CategoriaVista() {
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        JButton insertarCategoria = new JButton("Añadir");
        JButton editarCategoria = new JButton("Editar");
        JButton eliminarCategoria = new JButton("Eliminar");
        JButton actualizarTabla = new JButton("Actualizar");
        
        panelBotones.add(insertarCategoria);
        panelBotones.add(editarCategoria);
        panelBotones.add(eliminarCategoria);
        panelBotones.add(actualizarTabla);

        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCategorias = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaCategorias);

        add(panelBotones, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        insertarCategoria.addActionListener(e -> mostrarDialogoInsertar());
        editarCategoria.addActionListener(e -> mostrarDialogoEditar());
        eliminarCategoria.addActionListener(e -> eliminarCategoriaSeleccionada());
        actualizarTabla.addActionListener(e -> cargarDatos());
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        ArrayList<Categoria> categorias = CategoriaDAO.listarCategorias();
        for (Categoria categoria : categorias) {
            Object[] fila = {
                categoria.getId_categoria(),
                categoria.getNombre()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarDialogoInsertar() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Añadir Nueva Categoría", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(300, 150);
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

        JPanel botones = new JPanel();
        JButton confirmar = new JButton("Añadir");
        JButton cancelar = new JButton("Cancelar");
        
        confirmar.addActionListener(e -> {
            if (!nombreField.getText().isEmpty()) {
                Categoria categoria = new Categoria(0, nombreField.getText());
                CategoriaDAO.insertarCategoria(categoria);
                cargarDatos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, rellene todos los campos");
            }
        });
        
        cancelar.addActionListener(e -> dialog.dispose());
        
        botones.add(confirmar);
        botones.add(cancelar);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(botones, gbc);

        dialog.setContentPane(panel);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void mostrarDialogoEditar() {
        int filaSeleccionada = tablaCategorias.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una categoría para editar");
            return;
        }

        int id = (int) tablaCategorias.getValueAt(filaSeleccionada, 0);
        String nombreActual = (String) tablaCategorias.getValueAt(filaSeleccionada, 1);

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Editar Categoría", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(300, 150);
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

        JPanel botones = new JPanel();
        JButton confirmar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        
        confirmar.addActionListener(e -> {
            if (!nombreField.getText().isEmpty()) {
                Categoria categoria = new Categoria(id, nombreField.getText());
                CategoriaDAO.editarCategoria(categoria);
                cargarDatos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, rellene todos los campos");
            }
        });
        
        cancelar.addActionListener(e -> dialog.dispose());
        
        botones.add(confirmar);
        botones.add(cancelar);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(botones, gbc);

        dialog.setContentPane(panel);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void eliminarCategoriaSeleccionada() {
        int filaSeleccionada = tablaCategorias.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una categoría para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar esta categoría?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
                
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = (int) tablaCategorias.getValueAt(filaSeleccionada, 0);
            String nombre = (String) tablaCategorias.getValueAt(filaSeleccionada, 1);
            
            Categoria categoria = new Categoria(id, nombre);
            CategoriaDAO.borrarCategoria(categoria);
            cargarDatos();
        }
    }
}