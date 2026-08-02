package view;

import controller.ImagenUtil;
import controller.ProductoController;
import controller.TrabajadorController;
import conection.ConnectionJDBC;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import model.Producto;

public class PanelCatalogo extends JPanel {
	private MainFrame frame;
    private JPanel contenedorProductos;
    private JButton btnInsertar;
    private TrabajadorController trabajadorCtrl;
    private ProductoController productoCtrl;
 // Atributos para el filtrado múltiple (Paso A)
    private JCheckBox chkCine, chkLibro, chkMusica, chkTecno, chkVideo;
    private JButton btnAplicarFiltro;
    

    public PanelCatalogo(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        
        this.trabajadorCtrl = new TrabajadorController();
        this.productoCtrl = new ProductoController();
        
        // --- CABECERA (NORTE) ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titulo = new JLabel(" Catálogo", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        
        btnInsertar = new JButton("Insertar Producto");
        btnInsertar.addActionListener(e -> {
            if (validarAccesoTrabajador()) {
                DialogNuevoProducto dialog = new DialogNuevoProducto(frame);
                dialog.setVisible(true);
                cargarProductos(); 
            }
        });

        JButton btnVolver = new JButton("Menú");
        btnVolver.addActionListener(e -> frame.cambiarPanel("MENU"));
        
        JPanel botonesCabecera = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botonesCabecera.add(btnInsertar); 
        botonesCabecera.add(btnVolver);
        panelNorte.add(titulo, BorderLayout.CENTER);
        panelNorte.add(botonesCabecera, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);
        
     // --- CREACIÓN DEL PANEL DE FILTROS LATERAL ---
        JPanel pnlFiltros = new JPanel();
        pnlFiltros.setLayout(new BoxLayout(pnlFiltros, BoxLayout.Y_AXIS));
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtrar por Tipo"));
        pnlFiltros.setPreferredSize(new Dimension(150, 0));

        // Inicializamos las casillas con los nombres exactos del DDL 
        chkCine = new JCheckBox("Cine");
        chkLibro = new JCheckBox("Libro");
        chkMusica = new JCheckBox("Música");
        chkTecno = new JCheckBox("Tecnología");
        chkVideo = new JCheckBox("Videojuego");
        btnAplicarFiltro = new JButton("Aplicar Filtros");

        // Añadimos componentes al panel lateral
        pnlFiltros.add(chkCine);
        pnlFiltros.add(chkLibro);
        pnlFiltros.add(chkMusica);
        pnlFiltros.add(chkTecno);
        pnlFiltros.add(chkVideo);
        pnlFiltros.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado
        pnlFiltros.add(btnAplicarFiltro);

        // Colocamos el panel de filtros a la izquierda (Oeste)
        this.add(pnlFiltros, BorderLayout.WEST);
        btnAplicarFiltro.addActionListener(e -> {
            List<String> seleccionados = new ArrayList<>();
            
            // Verificamos qué CheckBoxes están marcados (basado en el constraint del DDL) 
            if (chkCine.isSelected()) seleccionados.add("Cine");
            if (chkLibro.isSelected()) seleccionados.add("Libro");
            if (chkMusica.isSelected()) seleccionados.add("Música");
            if (chkTecno.isSelected()) seleccionados.add("Tecnología");
            if (chkVideo.isSelected()) seleccionados.add("Videojuego");

            if (seleccionados.isEmpty()) {
                // Si no hay filtros, cargamos todo el catálogo de nuevo
                cargarProductos();
            } else {
                // Ejecutamos la consulta filtrada
                ejecutarFiltroMultiple(seleccionados);
            }
        });

        contenedorProductos = new JPanel();
        contenedorProductos.setLayout(new BoxLayout(contenedorProductos, BoxLayout.Y_AXIS));
        add(new JScrollPane(contenedorProductos), BorderLayout.CENTER);
        cargarProductos();
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
    
    public void cargarProductos() {
        contenedorProductos.removeAll();
        
        try (Connection conn = ConnectionJDBC.getConnection(); Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM PRODUCTO ORDER BY Tipo_producto ASC, Nombre ASC")) {
            while (rs.next()) {
                int id = rs.getInt("ID_producto");
                int tienda = rs.getInt("TIENDA_ID_tienda");
                String nom = rs.getString("Nombre");
                String tipo = rs.getString("Tipo_producto");
                byte[] imgData = rs.getBytes("Portada");
                
                JPanel fila = new JPanel(new BorderLayout(15, 0));
                fila.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
                
                // Portada
                JLabel lblImg = new JLabel();
                lblImg.setHorizontalAlignment(SwingConstants.CENTER);
                lblImg.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
                
                if (imgData != null) {
                    ImageIcon originalIcon = new ImageIcon(imgData);
                    Image img = originalIcon.getImage();
                    int newHeight = (img.getHeight(null) * 120) / img.getWidth(null);
                    lblImg.setIcon(new ImageIcon(img.getScaledInstance(120, newHeight, Image.SCALE_SMOOTH)));
                } else {
                    lblImg.setPreferredSize(new Dimension(120, 160));
                    lblImg.setText("Sin Portada");
                }

                JLabel lblTexto = new JLabel("<html><body style='width: 300px;'><b style='font-size: 16px;'>" 
                                             + nom + "</b><br><i style='color: gray;'>" + tipo + "</i></body></html>");
                
                // PANEL DE ACCIONES (Contenedor de botones)
                JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 40));
                
                JButton btnActualizar = new JButton("Actualizar Portada");
                btnActualizar.addActionListener(e -> {
                    if (validarAccesoTrabajador()) {
                        ejecutarActualizarPortada(id);
                    }
                });

                JButton btnVer = new JButton("Ver Detalles");
                btnVer.addActionListener(e -> {
                    frame.getPanelDetalleProducto().cargarDetalles(id, tienda, nom, tipo);
                    frame.cambiarPanel("DETALLE_PRODUCTO");
                });
                
                // AÑADIMOS AMBOS BOTONES AL PANEL
                pnlAcciones.add(btnActualizar);
                pnlAcciones.add(btnVer);
                
                // ENSAMBLAJE DE LA FILA
                fila.add(lblImg, BorderLayout.WEST);
                fila.add(lblTexto, BorderLayout.CENTER);
                
                // CORRECCIÓN: Añadimos el PANEL de acciones al EAST, no solo un botón
                fila.add(pnlAcciones, BorderLayout.EAST); 
                
                contenedorProductos.add(fila);
                contenedorProductos.add(new JSeparator());
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        revalidate(); 
        repaint();
    }
    
    private void ejecutarActualizarPortada(int idProducto) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                productoCtrl.actualizarPortadaProducto(idProducto, selectedFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Portada actualizada con éxito.");
                cargarProductos(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
            }
        }
    }
    
    private void ejecutarFiltroMultiple(List<String> tipos) {
        contenedorProductos.removeAll(); // Limpiamos la vista actual
        try {
            // Llamada al controlador para obtener los productos que cumplen el criterio IN
            List<Producto> filtrados = productoCtrl.filtrarPorTipos(tipos);
            
            for (Producto p : filtrados) {
                // Reutilizamos la lógica de diseño de filas
                agregarFilaProducto(
                    p.getIdProducto(), 
                    p.getIdTienda(), 
                    p.getNombre(), 
                    p.getTipoProducto(), 
                    p.getPortada()
                );
            }
            
            if (filtrados.isEmpty()) {
                contenedorProductos.add(new JLabel(" No hay productos disponibles para los tipos seleccionados."));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al filtrar productos: " + ex.getMessage(), 
                                          "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        
        // Refrescamos el scroll pane
        contenedorProductos.revalidate();
        contenedorProductos.repaint();
    }
    
    /**
     * Esta función crea la "tarjeta" visual de cada producto.
     * Usa ID_producto, Nombre, Tipo_producto y Portada del DDL.
     */
    private void agregarFilaProducto(int id, int tienda, String nom, String tipo, byte[] imgData) {
        JPanel fila = new JPanel(new BorderLayout(15, 0));
        fila.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Columna Portada (BLOB) [cite: 37]
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        lblImg.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
        
        if (imgData != null) {
            ImageIcon originalIcon = new ImageIcon(imgData);
            Image img = originalIcon.getImage();
            int newHeight = (img.getHeight(null) * 120) / img.getWidth(null);
            lblImg.setIcon(new ImageIcon(img.getScaledInstance(120, newHeight, Image.SCALE_SMOOTH)));
        } else {
            lblImg.setPreferredSize(new Dimension(120, 160));
            lblImg.setText("Sin Portada");
        }

        // Columnas Nombre y Tipo_producto [cite: 35, 36]
        JLabel lblTexto = new JLabel("<html><body style='width: 300px;'><b style='font-size: 16px;'>" 
                                   + nom + "</b><br><i style='color: gray;'>" + tipo + "</i></body></html>");

        // Panel de botones de acción
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 40));
        
        JButton btnActualizar = new JButton("Actualizar Portada");
        btnActualizar.addActionListener(e -> {
            if (validarAccesoTrabajador()) { ejecutarActualizarPortada(id); }
        });

        JButton btnVer = new JButton("Ver Detalles");
        btnVer.addActionListener(e -> {
            // Usa la PK compuesta: ID_producto y TIENDA_ID_tienda [cite: 39]
            frame.getPanelDetalleProducto().cargarDetalles(id, tienda, nom, tipo);
            frame.cambiarPanel("DETALLE_PRODUCTO");
        });

        pnlAcciones.add(btnActualizar);
        pnlAcciones.add(btnVer);

        fila.add(lblImg, BorderLayout.WEST);
        fila.add(lblTexto, BorderLayout.CENTER);
        fila.add(pnlAcciones, BorderLayout.EAST);

        contenedorProductos.add(fila);
        contenedorProductos.add(new JSeparator());
    }
    public void setModoTrabajador(boolean es) { }
    
}