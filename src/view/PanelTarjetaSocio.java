package view;

import controller.TarjetaSocioController;
import model.TarjetaSocio;

import javax.swing.*;
import java.awt.*;
import java.sql.Blob;

public class PanelTarjetaSocio extends JPanel {

    private MainFrame frame;
    private TarjetaSocioController controller;
    private int idClienteActual;

    // Componentes UI
    private JLabel lblIdTarjeta;
    private JLabel lblNivel;
    private JLabel lblPuntos;
    private JLabel lblFechaAlta;
    private JLabel lblFechaCaducidad;
    private JLabel lblRelleno;
    private JLabel lblFoto;

    public PanelTarjetaSocio(MainFrame frame) {
        this.frame = frame;
        this.controller = new TarjetaSocioController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- TITULO PANEL ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Tarjeta de Socio", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        
        panelNorte.add(titulo, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);
        
        // --- PANEL INFO TARJETA ---
        JPanel pnlInfo = new JPanel(new BorderLayout(30,10));
        pnlInfo.setBorder(BorderFactory.createTitledBorder("Información"));
        
        
        Font fuenteInfo = new Font("Segoe UI", Font.BOLD, 18); 
        JPanel panelDatos = new JPanel(new GridLayout(4, 1));
        lblIdTarjeta = new JLabel("ID Tarjeta: ---");
        lblIdTarjeta.setFont(fuenteInfo);
        lblRelleno = new JLabel("     ");
        lblRelleno.setFont(fuenteInfo);
        lblNivel = new JLabel("Nivel: ---");
        lblNivel.setFont(fuenteInfo);
        lblPuntos = new JLabel("Puntos Acumulados: ---");
        lblPuntos.setFont(fuenteInfo);
        lblFechaAlta = new JLabel("Fecha de alta: ---");
        lblFechaAlta.setFont(fuenteInfo);
        lblFechaCaducidad = new JLabel("Fecha de caducidad: ---");
        lblFechaCaducidad.setFont(fuenteInfo);
        panelDatos.add(lblIdTarjeta); panelDatos.add(lblRelleno);
        panelDatos.add(lblNivel); panelDatos.add(lblPuntos); 
        panelDatos.add(lblFechaAlta); panelDatos.add(lblFechaCaducidad);
        pnlInfo.add(panelDatos, BorderLayout.CENTER);
        add(pnlInfo, BorderLayout.CENTER);

        // --- FOTO ---
        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(300, 400));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel pnlFoto = new JPanel();
        pnlFoto.setBorder(BorderFactory.createTitledBorder("Foto"));
        pnlFoto.add(lblFoto,BorderLayout.CENTER);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(pnlInfo, BorderLayout.CENTER);
        centro.add(pnlFoto, BorderLayout.WEST);

        add(centro, BorderLayout.CENTER);

        // --- BOTÓN VOLVER ---
        JButton btnVolver = new JButton("Volver al Perfil");
        btnVolver.addActionListener(e -> frame.cambiarPanel("CLIENTE"));
        add(btnVolver, BorderLayout.SOUTH);
    }


    /**
     * Carga la información de la tarjeta del cliente
     */
    public void cargarDatos(int idCliente) {
        this.idClienteActual = idCliente;

        TarjetaSocio t = controller.obtenerTarjetaPorCliente(idCliente);
        if (t == null) {
            JOptionPane.showMessageDialog(this, "El cliente no tiene tarjeta de socio.");
            return;
        }

        lblIdTarjeta.setText("ID Tarjeta: " + String.valueOf(t.getIdTarjeta()));
        lblNivel.setText("Nivel: " + t.getNivel());
        lblPuntos.setText("Puntos Acumulados: " + String.valueOf(t.getPuntosAcumulados()));
        lblFechaAlta.setText("Fecha de Alta: " + t.getFechaAlta().toString());
        lblFechaCaducidad.setText("Fecha de Caducidad: " + t.getFechaCaducidad().toString());
        lblRelleno.setText("     ");
        
        //Cargar la Foto
        byte[] data = t.getFoto();
        if (data != null) {
            ImageIcon icon = new ImageIcon(data);
            
            // Calculamos el escalado manteniendo la proporción original
            Image img = icon.getImage();
            int anchoDeseado = 300; // Tamaño de la foto grande
            int altoDeseado = (img.getHeight(null) * anchoDeseado) / img.getWidth(null);
            
            Image newImg = img.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(newImg));
        } else {
            lblFoto.setIcon(null);
            lblFoto.setText("                   	   	 Sin Foto de Perfil");
            lblFoto.setPreferredSize(new Dimension(300, 620)); // Tamaño por defecto si no hay foto
        }
        // Forzamos al panel a recalcular los espacios con el nuevo tamaño de imagen
        this.revalidate();
        this.repaint();

    }

}
