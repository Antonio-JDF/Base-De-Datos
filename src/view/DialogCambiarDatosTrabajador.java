package view;

import controller.TrabajadorController;
import model.ItemTrabajador;
import conection.ConnectionJDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DialogCambiarDatosTrabajador extends JDialog {

    private int idGerente;

    private JComboBox<ItemTrabajador> cbTrabajadores;
    private JTextField txtSalario, txtTienda;

    private JPanel pnlEspecifico;
    private JComboBox<String> cbTurno;
    private JTextField txtIdiomas, txtComisiones, txtCaja, txtDepartamento;

    private String tipoActual;

    public DialogCambiarDatosTrabajador(JFrame frame, int idGerente) {
        super(frame, "Cambiar datos de trabajador", true);
        this.idGerente = idGerente;

        setSize(750, 450);
        setLocationRelativeTo(frame);
        setLayout(new BorderLayout(15, 15));

        /* ================= NORTE ================= */
        JLabel lblTitulo = new JLabel("¿Qué trabajador quieres cambiar?");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        cbTrabajadores = new JComboBox<>();
        cargarSubordinados();
        cbTrabajadores.addActionListener(e -> cargarDatosTrabajador());

        JPanel pnlNorte = new JPanel(new BorderLayout(10, 10));
        pnlNorte.add(lblTitulo, BorderLayout.NORTH);
        pnlNorte.add(cbTrabajadores, BorderLayout.CENTER);

        /* ================= CENTRO ================= */
        JPanel pnlCentro = new JPanel(new GridLayout(1, 2, 20, 0));

        // ---- Datos comunes ----
        JPanel pnlComun = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlComun.setBorder(BorderFactory.createTitledBorder("Datos generales"));

        txtSalario = new JTextField();
        txtTienda = new JTextField();

        pnlComun.add(new JLabel("Salario (€):"));
        pnlComun.add(txtSalario);
        pnlComun.add(new JLabel("ID Tienda:"));
        pnlComun.add(txtTienda);

        // ---- Datos específicos ----
        pnlEspecifico = new JPanel();
        pnlEspecifico.setLayout(new BoxLayout(pnlEspecifico, BoxLayout.Y_AXIS));
        pnlEspecifico.setBorder(BorderFactory.createTitledBorder("Datos específicos"));

        pnlCentro.add(pnlComun);
        pnlCentro.add(pnlEspecifico);

        /* ================= SUR ================= */
        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        add(pnlNorte, BorderLayout.NORTH);
        add(pnlCentro, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    /* ========================================================= */

    private void cargarSubordinados() {
        try {
            TrabajadorController tc = new TrabajadorController();
            List<ItemTrabajador> lista = tc.obtenerSubordinadosCombo(idGerente);

            for (ItemTrabajador t : lista) {
                cbTrabajadores.addItem(t);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cargarDatosTrabajador() {
        pnlEspecifico.removeAll();

        ItemTrabajador item =
                (ItemTrabajador) cbTrabajadores.getSelectedItem();
        if (item == null) return;

        try (Connection conn = ConnectionJDBC.getConnection()) {

            // ===== DATOS GENERALES =====
            String sql = "SELECT Salario, TIENDA_ID_tienda, Tipo_trabajador FROM TRABAJADOR WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, item.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtSalario.setText(rs.getString("Salario"));
                txtTienda.setText(rs.getString("TIENDA_ID_tienda"));
                tipoActual = rs.getString("Tipo_trabajador");
            }

            // ===== DATOS ESPECÍFICOS =====
            cbTurno = new JComboBox<>(new String[]{"Mañana", "Partido", "Tarde"});
            pnlEspecifico.add(campo("Turno", cbTurno));

            if (tipoActual.equals("Atención_al_cliente")) {
                txtIdiomas = new JTextField();
                pnlEspecifico.add(campo("Idiomas", txtIdiomas));
                cargarSub("AT_CLIENTE", "Idiomas, Turno", item.getId(), conn);
            }
            else if (tipoActual.equals("Cajero")) {
                txtCaja = new JTextField();
                pnlEspecifico.add(campo("ID Caja", txtCaja));
                cargarSub("CAJERO", "Turno, CAJA_ID_caja", item.getId(), conn);
            }
            else if (tipoActual.equals("Vendedor")) {
                txtComisiones = new JTextField();
                txtDepartamento = new JTextField();

                pnlEspecifico.add(campo("Comisiones", txtComisiones));
                pnlEspecifico.add(campo("ID Departamento", txtDepartamento));
                cargarSub("VENDEDOR",
                        "Turno, Comisiones, DEPARTAMENTO_ID_departamento",
                        item.getId(), conn);
            }

            pnlEspecifico.revalidate();
            pnlEspecifico.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cargarSub(String tabla, String campos, int id, Connection conn)
            throws SQLException {

        String sql = "SELECT " + campos + " FROM " + tabla + " WHERE TRABAJADOR_ID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            cbTurno.setSelectedItem(rs.getString("Turno"));

            if (tabla.equals("AT_CLIENTE")) {
                txtIdiomas.setText(rs.getString("Idiomas"));
            }
            if (tabla.equals("CAJERO")) {
                txtCaja.setText(rs.getString("CAJA_ID_caja"));
            }
            if (tabla.equals("VENDEDOR")) {
                txtComisiones.setText(rs.getString("Comisiones"));
                txtDepartamento.setText(rs.getString("DEPARTAMENTO_ID_departamento"));
            }
        }
    }

    /* ================= UPDATE ================= */

    private void guardarCambios() {
        ItemTrabajador item =
                (ItemTrabajador) cbTrabajadores.getSelectedItem();
        if (item == null) return;

        try (Connection conn = ConnectionJDBC.getConnection()) {
            conn.setAutoCommit(false);

            String sql = """
                UPDATE TRABAJADOR
                SET Salario = ?, TIENDA_ID_tienda = ?
                WHERE ID = ? AND TRABAJADOR_ID = ?
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, Double.parseDouble(txtSalario.getText()));
            ps.setInt(2, Integer.parseInt(txtTienda.getText()));
            ps.setInt(3, item.getId());
            ps.setInt(4, idGerente);
            ps.executeUpdate();

            actualizarSubtipo(conn, item.getId());

            conn.commit();
            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void actualizarSubtipo(Connection conn, int id) throws SQLException {
        String turno = (String) cbTurno.getSelectedItem();

        if (tipoActual.equals("Atención_al_cliente")) {
            ejecutar("""
                UPDATE AT_CLIENTE SET Idiomas = ?, Turno = ?
                WHERE TRABAJADOR_ID = ?
            """, conn, txtIdiomas.getText(), turno, id);
        }
        else if (tipoActual.equals("Cajero")) {
            ejecutar("""
                UPDATE CAJERO SET Turno = ?, CAJA_ID_caja = ?
                WHERE TRABAJADOR_ID = ?
            """, conn, turno, Integer.parseInt(txtCaja.getText()), id);
        }
        else if (tipoActual.equals("Vendedor")) {
            ejecutar("""
                UPDATE VENDEDOR
                SET Turno = ?, Comisiones = ?, DEPARTAMENTO_ID_departamento = ?
                WHERE TRABAJADOR_ID = ?
            """, conn, turno,
                    Double.parseDouble(txtComisiones.getText()),
                    Integer.parseInt(txtDepartamento.getText()), id);
        }
    }

    private void ejecutar(String sql, Connection conn, Object... params)
            throws SQLException {

        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        ps.executeUpdate();
    }

    private JPanel campo(String nombre, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setMaximumSize(new Dimension(300, 35));
        p.add(new JLabel(nombre + ":"), BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }
}
