package view;

import javax.swing.*;
import conection.ConnectionJDBC;
import controller.ClienteController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.*;
import java.util.Calendar;

public class DialogNuevoCliente extends JDialog {

    // Componentes de cliente
    private JTextField txtNombre, txtApellido1, txtApellido2, txtEmail, txtIDTarjeta, txtPuntosAcumulados, txtFechaAlta, txtFechaCaducidad;
    private JComboBox<String> comboNivel;
    private JButton btnCrear;
    private ClienteController controller;
    private JLabel lblUltimoId,lblPreviewFoto;
    private byte[] fotoBytes = null;
    

    public DialogNuevoCliente(Frame parent) {
        super(parent, "Crear Nuevo Cliente", true);
        
        // Layout
        setLayout(new BorderLayout());
        
        this.controller = new ClienteController();
        int ultimoId = controller.obtenerUltimoIdCliente();
        

        // PANEL CLIENTE
        JPanel panelCliente = new JPanel(new GridLayout(7, 2, 10, 10));
        panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        
        
        txtIDTarjeta = new JTextField();
        txtIDTarjeta.setText(String.valueOf(ultimoId + 1));  // ID siguiente
        lblUltimoId = new JLabel("(último ID añadido = " + ultimoId + ")");
        JPanel pnlId = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlId.add(lblUltimoId);
        pnlId.add(txtIDTarjeta);
        panelCliente.add(new JLabel("ID Cliente:"));
        panelCliente.add(pnlId);

        panelCliente.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCliente.add(txtNombre);

        panelCliente.add(new JLabel("Apellido 1:"));
        txtApellido1 = new JTextField();
        panelCliente.add(txtApellido1);

        panelCliente.add(new JLabel("Apellido 2:"));
        txtApellido2 = new JTextField();
        panelCliente.add(txtApellido2);

        panelCliente.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelCliente.add(txtEmail);

        panelCliente.add(new JLabel("Nivel Tarjeta:"));
        comboNivel = new JComboBox<>(new String[]{ "Básico","Bronce", "Plata","Oro"});
        panelCliente.add(comboNivel);
        
        //FOTO DE PERFIL
        JPanel panelFoto = new JPanel(new BorderLayout(10, 10));
        panelFoto.setBorder(BorderFactory.createTitledBorder("Fotografía de Socio"));
        
        JButton btnSeleccionar = new JButton("Seleccionar Imagen");
        lblPreviewFoto = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblPreviewFoto.setPreferredSize(new Dimension(200, 200));
        lblPreviewFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnSeleccionar.addActionListener(e -> seleccionarFoto());
        
        panelFoto.add(btnSeleccionar, BorderLayout.NORTH);
        panelFoto.add(lblPreviewFoto, BorderLayout.CENTER);
        
        // Botón de acción
        JPanel panelBotones = new JPanel();
        btnCrear = new JButton("Crear");
        panelBotones.add(btnCrear);

        // Configurar la ventana
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCliente, panelFoto);
        splitPane.setDividerLocation(400);  // Ajustar el tamaño de las secciones

        add(splitPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Acción del botón "Crear"
        btnCrear.addActionListener(this::crearClienteYtarjeta);

        // Configurar la ventana
        setSize(800, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private void seleccionarFoto() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = jfc.getSelectedFile();
            try {
                this.fotoBytes = new byte[(int) archivo.length()];
                try (FileInputStream fis = new FileInputStream(archivo)) {
                    fis.read(fotoBytes);
                }
                // Vista previa
                ImageIcon icon = new ImageIcon(new ImageIcon(fotoBytes).getImage()
                        .getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                lblPreviewFoto.setIcon(icon);
                lblPreviewFoto.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar imagen.");
            }
        }
    }
    
    private void crearClienteYtarjeta(ActionEvent e) {
    	Connection conn = ConnectionJDBC.getConnection();
        try {
            // Datos
            String nombre = txtNombre.getText().trim();
            String apellido1 = txtApellido1.getText().trim();
            String apellido2 = txtApellido2.getText().trim();
            String email = txtEmail.getText().trim();
            int idTarjeta = Integer.parseInt(txtIDTarjeta.getText().trim());
            String nivel = comboNivel.getSelectedItem().toString();

            // Valores automáticos empieza siempre con 0 puntos y la fecha es la de hoy 
            int puntos = 0;
            java.sql.Date fechaAlta = new java.sql.Date(System.currentTimeMillis());

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1);
            java.sql.Date fechaCaducidad = new java.sql.Date(cal.getTimeInMillis());

            // Conexión
           
            conn.setAutoCommit(false);

            /* ================= INSERT TARJETA ================= */
            // Primero insertamos tarjeta porque es necesaria para la creación de Cliente
            try {
                String sqlTarjeta = "INSERT INTO TARJETA_DE_SOCIO (ID_tarjeta, Puntos_acumulados, Nivel, Fecha_alta, Fecha_caducidad, Foto) VALUES (?, 0, ?, CURRENT_DATE, CURRENT_DATE + 365, ?)";

                try (PreparedStatement psTarjeta = conn.prepareStatement(sqlTarjeta)) {
                	psTarjeta.setInt(1, idTarjeta);
                    //psTarjeta.setInt(2, puntos);
                    psTarjeta.setString(2, nivel);
                    //psTarjeta.setDate(4, fechaAlta);
                    //psTarjeta.setDate(5, fechaCaducidad);

                    if (fotoBytes != null) {
                        psTarjeta.setBytes(3, fotoBytes);
                    } else {
                        psTarjeta.setNull(3, java.sql.Types.BLOB);
                    }
                    psTarjeta.executeUpdate();
                }

            } catch (SQLException exTarjeta) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                    "❌ No se ha podido insertar la TARJETA DE SOCIO\n\n" +
                    exTarjeta.getMessage());
                return;
            }

            /* ================= INSERT CLIENTE ================= */
            try {
                String sqlCliente = """
                    INSERT INTO CLIENTE
                    (ID_cliente, Nombre, Apellido1, Apellido2, Email,
                     TARJETA_DE_SOCIO_ID_tarjeta, TARJETA_DE_SOCIO_Nivel)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

                try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente)) {
                    psCliente.setInt(1, idTarjeta);
                    psCliente.setString(2, nombre);
                    psCliente.setString(3, apellido1);
                    psCliente.setString(4, apellido2);
                    psCliente.setString(5, email);
                    psCliente.setInt(6, idTarjeta);
                    psCliente.setString(7, nivel);
                    psCliente.executeUpdate();
                }

            } catch (SQLException exCliente) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                    "❌ La tarjeta se creó, pero NO se pudo insertar el CLIENTE\n\n" +
                    exCliente.getMessage());
                return;
            }

            
            conn.commit();

            JOptionPane.showMessageDialog(this,
                "✅ Cliente y tarjeta creados correctamente\n" +
                "• Puntos iniciales: 0\n" +
                "• Alta: hoy\n" +
                "• Caducidad: +1 año");

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "❌ El ID de tarjeta debe ser un número válido");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "❌ Error de conexión con la base de datos\n\n" + ex.getMessage());

        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
    /*
    // Método para convertir una fecha de texto a java.sql.Date
    private java.sql.Date convertirFecha(String fechaStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date date = sdf.parse(fechaStr);
            return new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            return null;  // Si la fecha no es válida, devolver null
        }
    }
	*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DialogNuevoCliente dialog = new DialogNuevoCliente(null);
            dialog.setVisible(true);
        });
    }
}
