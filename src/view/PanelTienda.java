package view;

import conection.ConnectionJDBC;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import controller.TiendaController;
import controller.TrabajadorController;
import model.Tienda;
import java.util.List;
import java.util.ArrayList;

public class PanelTienda extends JPanel {
    private MainFrame frame;
    private JTable tablaTiendas;
    private DefaultTableModel modelo;
    private TrabajadorController trabajadorCtrl;
    private TiendaController tiendaCtrl;

    public PanelTienda(MainFrame frame) {
        this.frame = frame;
        this.tiendaCtrl = new TiendaController();
        this.trabajadorCtrl = new TrabajadorController();
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(" Nuestras Tiendas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"ID", "Ubicación", "Ciudad", "Teléfono"}, 0);
        tablaTiendas = new JTable(modelo);
        add(new JScrollPane(tablaTiendas), BorderLayout.CENTER);
        
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnInsertar = new JButton("Nueva Tienda");
        btnInsertar.addActionListener(e -> {
            // Es recomendable validar acceso de trabajador antes de abrir
        	if (validarAccesoTrabajador()) {
                DialogNuevaTienda dialog = new DialogNuevaTienda(frame);
                dialog.setVisible(true);
                cargarTiendas(); 
            }
        });

        // Añadir el botón al panel de botones superior
        add(btnInsertar, BorderLayout.SOUTH);
        
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> frame.cambiarPanel("MENU"));
        add(btnVolver, BorderLayout.SOUTH);
        
        pnlAcciones.add(btnInsertar);
        pnlAcciones.add(btnVolver);
        add(pnlAcciones, BorderLayout.SOUTH);

        cargarTiendas();
    }
    /**
     * Consulta las tiendas al controlador y las muestra en el panel.
     */
    public void cargarTiendas() {
        // Limpiamos las filas actuales de la tabla
        modelo.setRowCount(0);

        try {
            // Obtenemos los datos de la tabla TIENDA
            List<Tienda> lista = tiendaCtrl.obtenerTiendas();
            
            for (Tienda t : lista) {
                // Añadimos cada tienda como una nueva fila en el modelo
                modelo.addRow(new Object[]{
                    t.getIdTienda(),
                    t.getDireccion(),
                    t.getCiudad(),
                    t.getTelefono()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }
    
    /**
     * Método auxiliar para pedir y validar el ID del trabajador antes de una acción
     */
    private boolean validarAccesoTrabajador() {
        String input = JOptionPane.showInputDialog(this, "Esta acción requiere permisos de personal.\nIntroduzca su ID de Trabajador:");
        if (input != null && !input.isEmpty()) {
            try {
                int idNum = Integer.parseInt(input);
                if (trabajadorCtrl.validarTrabajador(idNum)) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "ID de trabajador no válido.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    /**
     * Crea la representación visual de una tienda basada en el DDL.
     */
    private void agregarFilaTienda(Tienda t) {
        JPanel fila = new JPanel(new GridLayout(1, 4, 10, 0));
        fila.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblID = new JLabel("ID: " + t.getIdTienda());
        lblID.setForeground(Color.GRAY);
        lblID.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JLabel lblCiudad = new JLabel("<html><b>" + t.getCiudad() + "</b></html>");
        lblCiudad.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel lblDir = new JLabel(t.getDireccion());
        JLabel lblTelf = new JLabel("Teléfono: " + t.getTelefono());

        // Añadimos los componentes al grid de la fila
        fila.add(lblID);
        fila.add(lblCiudad);
        fila.add(lblDir);
        fila.add(lblTelf);

        // Finalmente, añadimos la fila y un separador al contenedor principal
        tablaTiendas.add(fila);
    }
}