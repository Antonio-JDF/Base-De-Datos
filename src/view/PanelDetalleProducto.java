package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// RESTO DE IMPORTS
import controller.ProductoController;
import conection.ConnectionJDBC;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Ahora es un JPanel integrado en el CardLayout del MainFrame
 */
public class PanelDetalleProducto extends JPanel {
    private ProductoController productoCtrl;
    private MainFrame frame;
    private JPanel pnlContenidoDinamico;
    private JLabel lblPortada, lblTituloNombre;

    // EL CONSTRUCTOR: Recibe el frame para coincidir con MainFrame
    public PanelDetalleProducto(MainFrame frame) {
        this.frame = frame;
        this.productoCtrl = new ProductoController();
        
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- PARTE SUPERIOR: Título y Botón Volver ---
        JPanel pnlNorte = new JPanel(new BorderLayout());
        lblTituloNombre = new JLabel("Detalles del Producto");
        lblTituloNombre.setFont(new Font("Arial", Font.BOLD, 26));
        
        JButton btnVolver = new JButton("Volver al Catálogo");
        btnVolver.addActionListener(e -> frame.cambiarPanel("CATALOGO"));
        
        pnlNorte.add(lblTituloNombre, BorderLayout.WEST);
        pnlNorte.add(btnVolver, BorderLayout.EAST);
        add(pnlNorte, BorderLayout.NORTH);

        // --- PARTE CENTRAL: Foto y Detalles ---
        JPanel pnlCentral = new JPanel(new BorderLayout(30, 0));
        
        // Foto a la izquierda 
        lblPortada = new JLabel();
        lblPortada.setPreferredSize(new Dimension(350, 450));
        lblPortada.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblPortada.setHorizontalAlignment(SwingConstants.CENTER);
        pnlCentral.add(lblPortada, BorderLayout.WEST);

        // Contenedor para los datos técnicos (BoxLayout para filas)
        pnlContenidoDinamico = new JPanel();
        pnlContenidoDinamico.setLayout(new BoxLayout(pnlContenidoDinamico, BoxLayout.Y_AXIS));
        pnlCentral.add(new JScrollPane(pnlContenidoDinamico), BorderLayout.CENTER);

        add(pnlCentral, BorderLayout.CENTER);
    }

    /**
     * Método que se llama desde el Catálogo para rellenar la info
     */
    public void cargarDetalles(int idProd, int idTienda, String nombre, String tipo) {
        // 1. Limpiamos datos anteriores
        pnlContenidoDinamico.removeAll();
        lblTituloNombre.setText(nombre + " (" + tipo + ")");

        // 2. Cargar Imagen BLOB desde la base de datos
        cargarImagen(idProd);

        // 3. Obtener y mostrar detalles técnicos específicos (CINE, LIBRO, etc.)
        Map<String, String> detalles = productoCtrl.obtenerDetallesEspecificos(idProd, idTienda, tipo);

        if (detalles.isEmpty()) {
            pnlContenidoDinamico.add(new JLabel("No hay detalles adicionales."));
        } else {
            Font fuenteDato = new Font("Segoe UI", Font.PLAIN, 18);
            for (Map.Entry<String, String> entry : detalles.entrySet()) {
                JLabel lbl = new JLabel("<html><body style='margin-bottom:10px;'><b>" + 
                                         entry.getKey() + ":</b> " + entry.getValue() + "</body></html>");
                lbl.setFont(fuenteDato);
                pnlContenidoDinamico.add(lbl);
            }
        }

        pnlContenidoDinamico.revalidate();
        pnlContenidoDinamico.repaint();
    }

    private void cargarImagen(int idProd) {
        try (Connection conn = ConnectionJDBC.getConnection()) {
            String sql = "SELECT Portada FROM PRODUCTO WHERE ID_producto = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProd);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                byte[] imgData = rs.getBytes("Portada");
                if (imgData != null) {
                    ImageIcon icon = new ImageIcon(imgData);
                    Image img = icon.getImage().getScaledInstance(350, -1, Image.SCALE_SMOOTH);
                    lblPortada.setIcon(new ImageIcon(img));
                } else {
                    lblPortada.setIcon(null);
                    lblPortada.setText("Sin Portada");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}