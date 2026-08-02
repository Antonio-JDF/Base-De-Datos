package view;

import controller.ProveedorController;
import controller.TiendaController;
import javax.swing.*;
import java.awt.*;

public class DialogNuevaTienda extends JDialog {
    private JTextField txtId, txtDir, txtCiudad, txtTelf;
    private TiendaController controller;

    public DialogNuevaTienda(Frame parent) {
        super(parent, "Añadir Tienda", true);
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        this.controller = new TiendaController();
        int ultimoId = controller.obtenerUltimoId();

        add(new JLabel(" ID Tienda (Último Id = " + ultimoId + "):")); txtId = new JTextField(String.valueOf(ultimoId + 1));
        add(txtId);
        add(new JLabel(" Ciudad:")); txtCiudad = new JTextField(); add(txtCiudad);
        add(new JLabel(" Dirección:")); txtDir = new JTextField(); add(txtDir);
        add(new JLabel(" Teléfono:")); txtTelf = new JTextField(); add(txtTelf);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String ciudad = txtCiudad.getText();
                String direccion = txtDir.getText();
                String telefono = txtTelf.getText();

                new TiendaController().insertarTienda(id, direccion, ciudad, telefono);
                
                JOptionPane.showMessageDialog(this, "Tienda guardada con éxito.");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al insertar: " + ex.getMessage());
            }
        });
        add(new JLabel("")); add(btnGuardar);
    }
}