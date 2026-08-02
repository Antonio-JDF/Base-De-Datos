package view;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import controller.TrabajadorController;

import java.awt.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class DialogNuevoTrabajador extends JDialog {

    private JTextField txtId, txtNombre, txtApe1, txtApe2, txtNacimiento, txtContrato, txtSalario, txtTienda;
    private JComboBox<String> cbTipo;
    private JPanel pnlDinamico;
    private Map<String, JComponent> camposExtra = new HashMap<>();
    private TrabajadorController controller;
    private int idGerente; // jefe es el que inserta al nuevo trabajador
    private JLabel lblUltimoId;

    public DialogNuevoTrabajador(MainFrame frame, int idGerente) {
        super(frame, "Insertar Nuevo Trabajador", true);
        this.idGerente = idGerente;

        setSize(550, 650);
        setLocationRelativeTo(frame);
        setLayout(new BorderLayout(10, 10));
        
        this.controller = new TrabajadorController();
        int ultimoIdTr = controller.obtenerUltimoIdTrabajador();
        
        lblUltimoId = new JLabel("(último ID añadido = " + ultimoIdTr + ")");
        

        /* ================= DATOS GENERALES ================= */
        JPanel pnlGeneral = new JPanel(new GridLayout(8, 2, 10, 10));
        pnlGeneral.setBorder(BorderFactory.createTitledBorder("Datos del Trabajador"));

        txtId = new JTextField();
        txtId.setText(String.valueOf(ultimoIdTr + 1)); 
        txtNombre = new JTextField();
        txtApe1 = new JTextField();
        txtApe2 = new JTextField();
        txtNacimiento = new JTextField("DD-MM-AAAA");
        txtContrato = new JTextField("DD-MM-AAAA");
        txtSalario = new JTextField();
        txtTienda = new JTextField();

        pnlGeneral.add(new JLabel("ID:"));
        pnlGeneral.add(crearPanelId());
        pnlGeneral.add(new JLabel("Nombre:")); pnlGeneral.add(txtNombre);
        pnlGeneral.add(new JLabel("Apellido 1:")); pnlGeneral.add(txtApe1);
        pnlGeneral.add(new JLabel("Apellido 2:")); pnlGeneral.add(txtApe2);
        pnlGeneral.add(new JLabel("Fecha Nacimiento:")); pnlGeneral.add(txtNacimiento);
        pnlGeneral.add(new JLabel("Fecha Contrato:")); pnlGeneral.add(txtContrato);
        pnlGeneral.add(new JLabel("Salario (€):")); pnlGeneral.add(txtSalario);
        pnlGeneral.add(new JLabel("ID Tienda:")); pnlGeneral.add(txtTienda);

        /* ================= TIPO Y DINÁMICO ================= */
        JPanel pnlCentro = new JPanel(new BorderLayout(5, 5));

        JPanel pnlTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTipo.add(new JLabel("Tipo de trabajador:"));
        cbTipo = new JComboBox<>(new String[]{
                "Atención_al_cliente", "Cajero", "Vendedor"
        });
        pnlTipo.add(cbTipo);

        pnlDinamico = new JPanel();
        pnlDinamico.setLayout(new BoxLayout(pnlDinamico, BoxLayout.Y_AXIS));
        pnlDinamico.setBorder(BorderFactory.createTitledBorder("Datos específicos"));

        cbTipo.addActionListener(e -> actualizarSubtipo());

        pnlCentro.add(pnlTipo, BorderLayout.NORTH);
        pnlCentro.add(pnlDinamico, BorderLayout.CENTER);

        /* ================= BOTÓN ================= */
        JButton btnGuardar = new JButton("Guardar Trabajador");
        btnGuardar.addActionListener(e -> guardar());

        add(pnlGeneral, BorderLayout.NORTH);
        add(pnlCentro, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);

        actualizarSubtipo();
    }
    
    private JPanel crearPanelId() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.add(lblUltimoId);
        p.add(txtId);
        return p;
    }

    /* ================= SUBTIPOS ================= */

    private void actualizarSubtipo() {
        pnlDinamico.removeAll();
        camposExtra.clear();

        String tipo = (String) cbTipo.getSelectedItem();

        if (tipo.equals("Atención_al_cliente")) {
            addCampo("Idiomas", new JTextField());
            addTurno();
        }
        else if (tipo.equals("Cajero")) {
            addTurno();
        }
        else if (tipo.equals("Vendedor")) {
            addTurno();
            addCampo("Comisiones", new JTextField());
            addCampo("ID Departamento", new JTextField());
        }

        pnlDinamico.revalidate();
        pnlDinamico.repaint();
    }

    private void addTurno() {
        JComboBox<String> cbTurno = new JComboBox<>(new String[]{
                "Mañana", "Partido", "Tarde"
        });
        addCampo("Turno", cbTurno);
    }

    private void addCampo(String nombre, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setMaximumSize(new Dimension(500, 35));
        p.add(new JLabel(nombre + ":"), BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        pnlDinamico.add(p);
        camposExtra.put(nombre, comp);
    }

    /* ================= GUARDAR ================= */

    private void guardar() {
    try {
        double salario = Double.parseDouble(txtSalario.getText());
        if (salario < 1100) {
            JOptionPane.showMessageDialog(this,
                    "El salario mínimo es 1100€");
            return;
        }

        int id = Integer.parseInt(txtId.getText());

        TrabajadorController tc = new TrabajadorController();
        if (tc.existeTrabajador(id)) {
            JOptionPane.showMessageDialog(this,
                    "❌ El ID " + id + " ya está en uso.",
                    "ID duplicado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        /* ===== FECHAS (DD-MM-AAAA) ===== */
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Date fn, fc;

        try {
            fn = Date.valueOf(LocalDate.parse(
                    txtNacimiento.getText().trim(), fmt));
            fc = Date.valueOf(LocalDate.parse(
                    txtContrato.getText().trim(), fmt));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "❌ Formato de fecha incorrecto.\nUsa DD-MM-AAAA",
                    "Error de fecha",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        /* ===== DATOS BASE ===== */
        String nombre = txtNombre.getText().trim();
        String ape1   = txtApe1.getText().trim();
        String ape2   = txtApe2.getText().trim();
        int tienda    = Integer.parseInt(txtTienda.getText());
        String tipo   = (String) cbTipo.getSelectedItem();

        /* ===== DATOS DINÁMICOS ===== */
        Map<String, String> extras = new HashMap<>();
        for (Map.Entry<String, JComponent> e : camposExtra.entrySet()) {
            if (e.getValue() instanceof JTextField) {
                extras.put(e.getKey(),
                        ((JTextField) e.getValue()).getText().trim());
            } else if (e.getValue() instanceof JComboBox) {
                extras.put(e.getKey(),
                        ((JComboBox<?>) e.getValue())
                                .getSelectedItem().toString());
            }
        }

        /* ===== INSERT ===== */
        tc.insertarTrabajadorCompleto(
                id, nombre, ape1, ape2, fn, fc, salario,
                tienda, idGerente, tipo, extras
        );

        JOptionPane.showMessageDialog(this,
                "✅ Trabajador insertado correctamente");

        dispose();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "❌ Número inválido en salario, tienda o ID",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Error al insertar trabajador",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
}

