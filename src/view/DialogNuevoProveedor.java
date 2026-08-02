package view;

import javax.swing.*;
import java.awt.*;
import controller.ProveedorController;

public class DialogNuevoProveedor extends JDialog {
    private JTextField txtId, txtNombre, txtPais;
    private ProveedorController controller;

    public DialogNuevoProveedor(Frame parent) {
        super(parent, "Añadir Nuevo Proveedor", true);
        this.controller = new ProveedorController();
        int ultimoId = controller.obtenerUltimoId(); 

        setLayout(new GridLayout(0, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Etiquetas y Campos
        add(new JLabel("ID Sugerido (Último: " + ultimoId + "):"));
        txtId = new JTextField(String.valueOf(ultimoId + 1));
        add(txtId);

        add(new JLabel("Nombre del Proveedor:"));
        txtNombre = new JTextField(); 
        add(txtNombre);

        add(new JLabel("País / Ubicación:"));
        txtPais = new JTextField(); 
        add(txtPais);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());
        
        add(new JLabel("")); 
        add(btnGuardar);

        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    private void guardar() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText().trim();
            String pais = txtPais.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            if (controller.insertarProveedor(id, nombre, pais)) {
                JOptionPane.showMessageDialog(this, "Proveedor insertado correctamente.");
                dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.");
        }
    }
}