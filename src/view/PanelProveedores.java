package view;

import controller.ProveedorController;
import controller.TrabajadorController;
import model.Proveedor;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelProveedores extends JPanel {
    private MainFrame frame;
    private JPanel contenedorProveedores;
    private ProveedorController proveedorCtrl;
    private TrabajadorController trabajadorCtrl;

    public PanelProveedores(MainFrame frame) {
        this.frame = frame;
        this.proveedorCtrl = new ProveedorController();
        this.trabajadorCtrl = new TrabajadorController();
        setLayout(new BorderLayout());

        // --- CABECERA (NORTE) ---
        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titulo = new JLabel(" Listado de Proveedores", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        
     // Panel para agrupar los botones a la derecha
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton btnNuevo = new JButton("Nuevo Proveedor");
        btnNuevo.addActionListener(e -> {
            // Abrir diálogo y refrescar al cerrar
 
                 // Es recomendable validar acceso de trabajador antes de abrir
             	if (validarAccesoTrabajador()) {
             		new DialogNuevoProveedor(frame).setVisible(true);
                    cargarProveedores();  
                 }
        });
        
        JButton btnVolver = new JButton("Menú Principal");
        btnVolver.addActionListener(e -> frame.cambiarPanel("MENU"));
        
        pnlBotones.add(btnNuevo);
        pnlBotones.add(btnVolver);
        
        pnlNorte.add(titulo, BorderLayout.CENTER);
        pnlNorte.add(pnlBotones, BorderLayout.EAST);
        add(pnlNorte, BorderLayout.NORTH);

        // --- CUERPO CENTRAL (LISTADO) ---
        contenedorProveedores = new JPanel();
        contenedorProveedores.setLayout(new BoxLayout(contenedorProveedores, BoxLayout.Y_AXIS));
        
        JScrollPane scroll = new JScrollPane(contenedorProveedores);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        cargarProveedores();
    }

    /**
     * Limpia el contenedor y recarga los datos desde el controlador.
     */
    public void cargarProveedores() {
        contenedorProveedores.removeAll();
        try {
            List<Proveedor> proveedores = proveedorCtrl.obtenerProveedores();
            for (Proveedor p : proveedores) {
                agregarFilaProveedor(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
        revalidate();
        repaint();
    }

    /**
     * Crea el diseño visual para cada fila de proveedor.
     */
    private void agregarFilaProveedor(Proveedor p) {
        JPanel fila = new JPanel(new GridLayout(1, 3, 10, 0));
        fila.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Etiquetas usando los nombres de tu modelo
        JLabel lblID = new JLabel("ID: " + p.getID_proveedor());
        JLabel lblNombre = new JLabel("<html><b>" + p.getNombre() + "</b></html>");
        JLabel lblPais = new JLabel("Ubicación: " + (p.getPais() != null ? p.getPais() : "N/A"));

        lblID.setFont(new Font("Monospaced", Font.PLAIN, 14));
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));

        fila.add(lblID);
        fila.add(lblNombre);
        fila.add(lblPais);

        contenedorProveedores.add(fila);
    }
    
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
}