package view;

import controller.VentaController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHistorial extends JPanel {
    private MainFrame frame;
    private JTable tablaHistorial;
    private VentaController controller;
    private JButton btnVerTicket;

    public PanelHistorial(MainFrame frame) {
        this.frame = frame;
        this.controller = new VentaController();
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ENCABEZADO ---
        JLabel titulo = new JLabel("Historial de Compras", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA DE DATOS ---
        // Ahora incluye columnas de la tabla TICKET del DDL 
        tablaHistorial = new JTable();
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        add(scrollPane, BorderLayout.CENTER);

        // --- PANEL DE ACCIONES (SUR) ---
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnVerTicket = new JButton("Ver Recibo Detallado");
        btnVerTicket.setEnabled(false); // Se activa al seleccionar una fila
        
        JButton btnVolver = new JButton("Volver al Perfil");
        
        // Lógica para abrir el detalle del Ticket 
        btnVerTicket.addActionListener(e -> {
            int fila = tablaHistorial.getSelectedRow();
            if (fila != -1) {
                int idVenta = (int) tablaHistorial.getValueAt(fila, 0);
                new DialogTicket(frame, idVenta);
            }
        });

        // Habilitar botón al seleccionar una venta
        tablaHistorial.getSelectionModel().addListSelectionListener(e -> {
            btnVerTicket.setEnabled(tablaHistorial.getSelectedRow() != -1);
        });

        btnVolver.addActionListener(e -> frame.cambiarPanel("CLIENTE"));
        
        pnlAcciones.add(btnVerTicket);
        pnlAcciones.add(btnVolver);
        add(pnlAcciones, BorderLayout.SOUTH);
    }

    /**
     * Carga los datos usando el nuevo controlador que une VENTA y TICKET.
     */
    public void cargarDatos(int idCliente) {
        DefaultTableModel modelo = controller.obtenerHistorial(idCliente);
        tablaHistorial.setModel(modelo);
        btnVerTicket.setEnabled(false);
    }
}