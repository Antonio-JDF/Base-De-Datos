package view;

import controller.ReseñaController;
import conection.ConnectionJDBC;
import model.Reseña;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PanelReseñas extends JPanel {

    private MainFrame frame;
    private ReseñaController controller;
    private JTable tablaReseñas;
    private JComboBox<String> cbProductos;
    private JTextField txtPuntuacion;
    private JTextArea txtComentario;
    private int idClienteActual;

    public PanelReseñas(MainFrame frame) {
        this.frame = frame;
        this.controller = new ReseñaController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- LISTA DE RESEÑAS (NORTE) ---
        JPanel pnlLista = new JPanel(new BorderLayout());
        pnlLista.setBorder(BorderFactory.createTitledBorder("Mis Reseñas Publicadas"));
        tablaReseñas = new JTable();
        pnlLista.add(new JScrollPane(tablaReseñas), BorderLayout.CENTER);
        pnlLista.setPreferredSize(new Dimension(800, 250));
        add(pnlLista, BorderLayout.NORTH);

        // --- FORMULARIO NUEVA RESEÑA (CENTRO) ---
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Escribir nueva reseña"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Producto
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlForm.add(new JLabel("Producto:"), gbc);

        cbProductos = new JComboBox<>();
        gbc.gridx = 1;
        pnlForm.add(cbProductos, gbc);

        // Puntuación
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlForm.add(new JLabel("Puntuación (1-5):"), gbc);

        txtPuntuacion = new JTextField(5);
        gbc.gridx = 1;
        pnlForm.add(txtPuntuacion, gbc);

        // Comentario
        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlForm.add(new JLabel("Comentario:"), gbc);

        txtComentario = new JTextArea(3, 20);
        gbc.gridx = 1;
        pnlForm.add(new JScrollPane(txtComentario), gbc);

        // Botón publicar
        JButton btnEnviar = new JButton("Publicar Reseña");
        btnEnviar.addActionListener(e -> publicar());
        gbc.gridx = 1;
        gbc.gridy = 3;
        pnlForm.add(btnEnviar, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // --- BOTÓN VOLVER ---
        JButton btnVolver = new JButton("Volver al Perfil");
        btnVolver.addActionListener(e -> frame.cambiarPanel("CLIENTE"));
        add(btnVolver, BorderLayout.SOUTH);
    }

    /**
     * Se llama al entrar en el panel
     */
    public void cargarDatos(int idCliente) {
        this.idClienteActual = idCliente;
        tablaReseñas.setModel(controller.obtenerReseñasCliente(idCliente));
        cargarProductosComprados(); // ✅ AQUÍ
    }

    /**
     * Carga productos comprados por el cliente
     */
    private void cargarProductosComprados() {
        cbProductos.removeAllItems();

        String sql = """
            SELECT DISTINCT P.ID_producto, P.Nombre, P.TIENDA_ID_tienda
            FROM PRODUCTO P
            JOIN RESERVADO R ON P.ID_producto = R.PRODUCTO_ID_producto
            JOIN VENTA V ON R.VENTA_ID_venta = V.ID_venta
            WHERE V.CLIENTE_ID_cliente = ?
        """;

        try (Connection conn = ConnectionJDBC.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idClienteActual);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                cbProductos.addItem(
                    rs.getInt("ID_producto") + ":" +
                    rs.getInt("TIENDA_ID_tienda") + " - " +
                    rs.getString("Nombre")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Publica una nueva reseña
     */
    private void publicar() {
        try {
            String seleccion = (String) cbProductos.getSelectedItem();
            if (seleccion == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.");
                return;
            }

            String[] partes = seleccion.split(" - ")[0].split(":");
            int idProd = Integer.parseInt(partes[0]);
            int idTienda = Integer.parseInt(partes[1]);
            double puntos = Double.parseDouble(txtPuntuacion.getText());

            Reseña r = new Reseña(
                0,
                puntos,
                txtComentario.getText(),
                new java.sql.Date(System.currentTimeMillis()),
                idClienteActual,
                idProd,
                idTienda
            );

            if (controller.insertarReseña(r)) {
                JOptionPane.showMessageDialog(this, "Reseña publicada correctamente.");
                cargarDatos(idClienteActual);
                txtPuntuacion.setText("");
                txtComentario.setText("");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La puntuación debe ser un número.");
        }
    }
}