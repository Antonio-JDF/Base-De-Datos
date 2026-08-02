package view;

import controller.TrabajadorController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class DialogInfoDepartamentos extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;
    private TrabajadorController controller;

    public DialogInfoDepartamentos(Frame parent) {
        super(parent, "Información de Departamentos", true);
        this.controller = new TrabajadorController();

        setSize(600, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        /* ===== TÍTULO ===== */
        JLabel titulo = new JLabel("Departamentos y Número de Vendedores", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        /* ===== TABLA ===== */
        modelo = new DefaultTableModel(
                new String[]{"ID Departamento", "Nombre", "Nº Empleados"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        /* ===== BOTÓN ===== */
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.add(btnCerrar);
        add(pnlSur, BorderLayout.SOUTH);

        cargarDatos();
    }

    /* ===== CARGAR DATOS ===== */
    private void cargarDatos() {
        modelo.setRowCount(0);

        try {
            ResultSet rs = controller.obtenerInfoDepartamentos();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id_departamento"),
                        rs.getString("nombre"),
                        rs.getInt("num_empleados")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar departamentos:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
