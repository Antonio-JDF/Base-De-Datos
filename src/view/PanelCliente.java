package view;

import controller.ClienteController;
import controller.ImagenUtil;
import conection.ConnectionJDBC;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.io.File;
import controller.TarjetaSocioController;
import model.TarjetaSocio;

public class PanelCliente extends JPanel {
    private MainFrame frame;
    private JLabel lblFoto, lblNombre, lblEmail, lblPuntos, lblNivel;
    private int idClienteActual; 
    private ClienteController clienteCtrl;

    public PanelCliente(MainFrame frame) {
        this.frame = frame;
        this.clienteCtrl = new ClienteController();
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- NORTE: Título y Salida ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Mi Perfil de Socio", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        JButton btnVolver = new JButton("Cerrar Sesión");
        btnVolver.addActionListener(e -> frame.cambiarPanel("MENU"));
        
        panelNorte.add(titulo, BorderLayout.CENTER);
        panelNorte.add(btnVolver, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);

        // --- CENTRO: Foto y Datos ---
        JPanel panelInfo = new JPanel(new BorderLayout(30, 10));
        
        JPanel panelFotoVertical = new JPanel(new BorderLayout(20, 20));
        lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));
        panelInfo.add(lblFoto, BorderLayout.WEST);
        
        JButton btnCambiarFoto = new JButton("Actualizar Foto");
        btnCambiarFoto.addActionListener(e -> abrirSelectorDeImagen());
        
        panelFotoVertical.add(lblFoto, BorderLayout.CENTER);
        panelFotoVertical.add(btnCambiarFoto, BorderLayout.SOUTH);
        panelInfo.add(panelFotoVertical, BorderLayout.WEST);
        
        Font fuenteInfo = new Font("Segoe UI", Font.BOLD, 18); 
        JPanel panelDatos = new JPanel(new GridLayout(4, 1));
        lblNombre = new JLabel("Nombre: ---");
        lblNombre.setFont(fuenteInfo);
        lblEmail = new JLabel("Email: ---");
        lblEmail.setFont(fuenteInfo);
        lblNivel = new JLabel("Nivel: ---");
        lblNivel.setFont(fuenteInfo);
        lblPuntos = new JLabel("Puntos: ---");
        lblPuntos.setFont(fuenteInfo);
        panelDatos.add(lblNombre); panelDatos.add(lblEmail);
        panelDatos.add(lblNivel); panelDatos.add(lblPuntos);
        panelInfo.add(panelDatos, BorderLayout.CENTER);
        add(panelInfo, BorderLayout.CENTER);

        // --- SUR: Botones de Acción Integrados ---
        JPanel panelAcciones = new JPanel(new GridLayout(1, 4, 10, 0));
        
        JButton btnInfo = new JButton("Info Tarjeta");
        JButton btnReseñas = new JButton("Reseñas");
        JButton btnHistorial = new JButton("Historial Compra");
        JButton btnCompra = new JButton("Comprar/Reservar");
        
        // Acción: Navegar a Información de Tarjeta de Socio
        btnInfo.addActionListener(e -> {
            frame.getPanelTarjetaSocio().cargarDatos(idClienteActual);
            frame.cambiarPanel("TARJETA_SOCIO");
        });

        // Acción: Navegar a Reseñas
        btnReseñas.addActionListener(e -> {
            frame.getPanelReseñas().cargarDatos(idClienteActual);
            frame.cambiarPanel("RESEÑAS");
        });

        // Acción: Navegar a Historial
        btnHistorial.addActionListener(e -> {
            frame.getPanelHistorial().cargarDatos(idClienteActual);
            frame.cambiarPanel("HISTORIAL");
        });

        // Acción: Navegar a Compra/Reserva
        btnCompra.addActionListener(e -> {
            frame.getPanelCompra().setIdCliente(idClienteActual);
            frame.cambiarPanel("COMPRA");
        });

        panelAcciones.add(btnInfo);
        panelAcciones.add(btnReseñas);
        panelAcciones.add(btnHistorial);
        panelAcciones.add(btnCompra);
        add(panelAcciones, BorderLayout.SOUTH);
    }
    
    /**
     * Abre un selector de archivos para que el socio elija su imagen.
     */
    private void abrirSelectorDeImagen() {
        JFileChooser fileChooser = new JFileChooser();
        // Filtro para que solo se vean imágenes
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Llamamos al controlador para guardar el BLOB
                clienteCtrl.actualizarFotoSocio(idClienteActual, selectedFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Foto de perfil actualizada correctamente.");
                
                // Refrescamos los datos para ver la nueva foto
                cargarDatosCliente(idClienteActual);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar la foto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Carga la información del cliente desde la base de datos.
     */
    public void cargarDatosCliente(int idCliente) {
        this.idClienteActual = idCliente;
        String sql = "SELECT c.Nombre, c.Apellido1, c.Email, t.Puntos_acumulados, t.Nivel, t.Foto " +
                     "FROM CLIENTE c JOIN TARJETA_DE_SOCIO t " +
                     "ON c.TARJETA_DE_SOCIO_ID_tarjeta = t.ID_tarjeta " +
                     "WHERE c.ID_cliente = ?";

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lblNombre.setText("Nombre: " + rs.getString("Nombre") + " " + rs.getString("Apellido1"));
                lblEmail.setText("Email: " + rs.getString("Email"));
                lblNivel.setText("Nivel: " + rs.getString("Nivel"));
                lblPuntos.setText("Puntos: " + rs.getInt("Puntos_acumulados"));

             // MANEJO DE LA FOTO GRANDE
                Blob blob = rs.getBlob("Foto");
                if (blob != null) {
                    byte[] data = blob.getBytes(1, (int) blob.length());
                    ImageIcon icon = new ImageIcon(data);
                    
                    // Calculamos el escalado manteniendo la proporción original
                    Image img = icon.getImage();
                    int anchoDeseado = 500; // Tamaño de la foto grande
                    int altoDeseado = (img.getHeight(null) * anchoDeseado) / img.getWidth(null);
                    
                    Image newImg = img.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(newImg));
                } else {
                    lblFoto.setIcon(null);
                    lblFoto.setText("Sin Foto de Perfil");
                    lblFoto.setPreferredSize(new Dimension(300, 400)); // Tamaño por defecto si no hay foto
                }
                // Forzamos al panel a recalcular los espacios con el nuevo tamaño de imagen
                this.revalidate();
                this.repaint();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Permite al MainFrame asignar qué cliente se debe mostrar.
     */
    public void setIdCliente(int id) {
        this.idClienteActual = id;
    }

    /**
     * Carga o refresca los datos del cliente desde la base de datos.
     */
    public void cargarDatosCliente() {
        if (this.idClienteActual <= 0) return;

        // 1. Usamos el controlador de tarjetas para obtener puntos y foto
        TarjetaSocioController tCtrl = new TarjetaSocioController();
        TarjetaSocio tarjeta = tCtrl.obtenerTarjetaPorCliente(this.idClienteActual);

        if (tarjeta != null) {
            // Actualizar el texto de los puntos
            // lblPuntos.setText("Puntos: " + tarjeta.getPuntosAcumulados());

            // 2. Cargar la foto si existe
            if (tarjeta.getFoto() != null) {
                byte[] imagenBytes = tarjeta.getFoto();
                ImageIcon original = new ImageIcon(imagenBytes);
                // Escalamos la imagen para que quepa en el panel f4ff4f.jpg
                Image escalada = original.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH);
                // lblFoto.setIcon(new ImageIcon(escalada));
            }
        }
        
        // Refrescar visualmente el panel
        revalidate();
        repaint();
    }
}