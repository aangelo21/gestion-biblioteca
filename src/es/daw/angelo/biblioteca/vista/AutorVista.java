package es.daw.angelo.biblioteca.vista;

import es.daw.angelo.biblioteca.dao.AutorDAO;
import es.daw.angelo.biblioteca.model.Autor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("Convert2Lambda")
public class AutorVista extends JPanel {
    public AutorVista() {
        initcomponents();
    }

    private void initcomponents() {
        JPanel panelAutor = new JPanel();

        JButton insertarAutor = new JButton("Añadir");
        JButton editarAutor = new JButton("Editar");
        JButton eliminarAutor = new JButton("Eliminar");

        JTable tablaAutores = new JTable();

        panelAutor.add(tablaAutores);
        panelAutor.add(insertarAutor);
        panelAutor.add(editarAutor);
        panelAutor.add(eliminarAutor);
        add(panelAutor);

        insertarAutor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window parentWindow = SwingUtilities.getWindowAncestor(AutorVista.this);
                JDialog insertarAutores = new JDialog(parentWindow, "Añadir Nuevo Autor", Dialog.ModalityType.APPLICATION_MODAL);
                insertarAutores.setSize(350, 200);
                insertarAutores.setLocationRelativeTo(parentWindow);

                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                gbc.gridx = 0;
                gbc.gridy = 0;
                panel.add(new JLabel("Nombre del autor:"), gbc);
                gbc.gridx = 1;
                JTextField nombreAutor = new JTextField(15);
                panel.add(nombreAutor, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                panel.add(new JLabel("Nacionalidad:"), gbc);
                gbc.gridx = 1;
                JTextField nacionalidadAutor = new JTextField(15);
                panel.add(nacionalidadAutor, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                JPanel botones = new JPanel();
                JButton confirmar = new JButton("Añadir");
                JButton cancelar = new JButton("Cancelar");
                botones.add(confirmar);
                botones.add(cancelar);
                panel.add(botones, gbc);

                cancelar.addActionListener(ev -> insertarAutores.dispose());
                confirmar.addActionListener(ev -> {
                    Autor autor = new Autor(0, nombreAutor.getText() , nacionalidadAutor.getText());
                    AutorDAO.insertarAutor(autor);
                    insertarAutores.dispose();
                });

                insertarAutores.setContentPane(panel);
                insertarAutores.setResizable(false);
                insertarAutores.setVisible(true);
            }
        });

    }
}
