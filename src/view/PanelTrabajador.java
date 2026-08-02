package view;

import controller.TrabajadorController;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.sql.ResultSet;
import java.util.Map;

public class PanelTrabajador extends JPanel {

    private MainFrame frame;
    private TrabajadorController controller;

    private JLabel lblNombre, lblSueldo, lblJefe, lblCargo;
    private JTextArea txtEspecialidad;

    private JButton btnNuevoTrabajador;
    private JButton btnEditarSubordinados;
    private JButton btnInfoCajas;
    private JButton btnInfoDepartamentos;

    private int idTrabajadorActual;

    public PanelTrabajador(MainFrame frame) {
        this.frame = frame;
        this.controller = new TrabajadorController();

        setLayout(new BorderLayout(25, 25));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        /* ================= ENCABEZADO ================= */
        JPanel pnlNorte = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Perfil del Empleado", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton btnMenu = new JButton("Menú Principal");
        btnMenu.addActionListener(e -> frame.cambiarPanel("MENU"));

        pnlNorte.add(titulo, BorderLayout.CENTER);
        pnlNorte.add(btnMenu, BorderLayout.EAST);
        add(pnlNorte, BorderLayout.NORTH);

        /* ================= CENTRO ================= */
        JPanel pnlCentro = new JPanel(new GridLayout(1, 2, 20, 0));
        Font atributos = new Font("Segoe UI", Font.BOLD, 18);
        Font cabecera = new Font("Segoe UI", Font.BOLD, 24);
        JPanel pnlComun = new JPanel(new GridLayout(6, 1, 5, 5));
        //pnlComun.setBorder(BorderFactory.createTitledBorder("Información General"));
        
        TitledBorder bordeInfo = BorderFactory.createTitledBorder("Información General");

        // 2. Definimos la fuente con tamaño 24 (puedes usar la variable 'cabecera' si ya existe)
        bordeInfo.setTitleFont(cabecera);

        // 3. Lo asignamos al panel
        pnlComun.setBorder(bordeInfo);
        

        lblNombre = new JLabel("Empleado:");
        lblNombre.setFont(atributos);
        lblSueldo = new JLabel("Salario:");
        lblSueldo.setFont(atributos);
        lblCargo  = new JLabel("Puesto:");
        lblCargo.setFont(atributos);
        lblJefe   = new JLabel("Jefe:");
        lblJefe.setFont(atributos);

        pnlComun.add(lblNombre);
        pnlComun.add(lblSueldo);
        pnlComun.add(lblCargo);
        pnlComun.add(lblJefe);

        pnlCentro.add(pnlComun);

        JPanel pnlSub = new JPanel(new BorderLayout());
        pnlSub.setBorder(BorderFactory.createTitledBorder("Datos de Especialización"));

        txtEspecialidad = new JTextArea();
        txtEspecialidad.setEditable(false);
        txtEspecialidad.setFont(new Font("Monospaced", Font.PLAIN, 13));

        pnlSub.add(new JScrollPane(txtEspecialidad), BorderLayout.CENTER);
        pnlCentro.add(pnlSub);

        add(pnlCentro, BorderLayout.CENTER);

        /* ================= BOTONES ================= */
        btnNuevoTrabajador = new JButton("Insertar nuevo trabajador");
        btnEditarSubordinados = new JButton("Editar trabajadores a mi cargo");
        btnInfoCajas = new JButton("Información cajas");
        btnInfoDepartamentos = new JButton("Información departamentos");

        btnNuevoTrabajador.setVisible(false);
        btnEditarSubordinados.setVisible(false);
        btnInfoCajas.setVisible(false);
        btnInfoDepartamentos.setVisible(false);

        btnNuevoTrabajador.addActionListener(e ->
                new DialogNuevoTrabajador(frame, idTrabajadorActual).setVisible(true)
        );

        btnEditarSubordinados.addActionListener(e ->
                new DialogCambiarDatosTrabajador(frame, idTrabajadorActual).setVisible(true)
        );

        btnInfoCajas.addActionListener(e ->
                new DialogInfoCajas(frame).setVisible(true)
        );

        btnInfoDepartamentos.addActionListener(e ->
                new DialogInfoDepartamentos(frame).setVisible(true)
        );

        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.add(btnInfoCajas);
        pnlSur.add(btnInfoDepartamentos);
        pnlSur.add(btnEditarSubordinados);
        pnlSur.add(btnNuevoTrabajador);

        add(pnlSur, BorderLayout.SOUTH);
    }

    /* ================= CARGAR DATOS ================= */

    public void cargarDatos(int idTrabajador) {
        this.idTrabajadorActual = idTrabajador;

        try {
            ResultSet rs = controller.obtenerDatosBasicos(idTrabajador);

            if (rs.next()) {
                String tipo = rs.getString("Tipo_trabajador");

                lblNombre.setText("Empleado: " +
                        rs.getString("Nombre") + " " + rs.getString("Apellido1"));

                lblSueldo.setText("Salario: " +
                        rs.getDouble("Salario") + " €");

                lblCargo.setText("Puesto: " + tipo);

                String jefe = rs.getString("nombre_jefe");
                lblJefe.setText("Jefe: " +
                        (jefe != null
                                ? jefe + " " + rs.getString("apellido_jefe")
                                : "No tiene (Gerencia Máxima)")
                );

                Map<String, String> detalles =
                        controller.obtenerDetallesEspecializados(idTrabajador, tipo);

                StringBuilder sb = new StringBuilder("Atributos específicos:\n\n");
                detalles.forEach((k, v) ->
                        sb.append("- ").append(k).append(": ").append(v).append("\n")
                );
                txtEspecialidad.setText(sb.toString());

                /* ===== VISIBILIDAD DE BOTONES ===== */
                btnNuevoTrabajador.setVisible("Gerente".equalsIgnoreCase(tipo));
                btnEditarSubordinados.setVisible("Gerente".equalsIgnoreCase(tipo));

                btnInfoCajas.setVisible("Cajero".equalsIgnoreCase(tipo));
                btnInfoDepartamentos.setVisible("Vendedor".equalsIgnoreCase(tipo));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar trabajador:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
