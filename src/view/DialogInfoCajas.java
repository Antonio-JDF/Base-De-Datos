package view;

import controller.TrabajadorController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class DialogInfoCajas extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;
    private TrabajadorController controller;

    public DialogInfoCajas(Frame parent) {
        super(parent, "Información de Cajas", true);
        this.controller = new TrabajadorController();

        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        /* ===== TÍTULO ===== */
        JLabel titulo = new JLabel("Listado de Cajas y Cajeros", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        /* ===== TABLA ===== */
        modelo = new DefaultTableModel(
        		new String[]{
        				 "ID Caja", "Balance total", "Ventas del día",
        				 "ID Trabajador", "Trabajador", "Turno", "ID Tienda"
        				},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla solo lectura
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
            ResultSet rs = controller.obtenerInfoCajas();

            while (rs.next()) {

			    String nombreCompleto =
			            rs.getString("Nombre") + " " +
			            rs.getString("Apellido1") + " " +
			            rs.getString("Apellido2");
			
			    modelo.addRow(new Object[]{
			            rs.getInt("ID_caja"),
			            rs.getDouble("Balance"),
			            rs.getDouble("Total_ventas_dia"),
			            rs.getInt("ID_trabajador"),
			            nombreCompleto,
			            rs.getString("Turno"),
			            rs.getInt("TIENDA_ID_tienda")
			    });
			}
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las cajas:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
