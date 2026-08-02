package view;

import controller.EventoController;
import javax.swing.*;
import java.awt.*;
import controller.EventoController;

public class DialogNuevoEventoCreador extends JDialog {
	private JTextField txtIdEv, txtNomEv, txtTipoEvento, txtIdTienda;
    private JTextField txtIdCrNuevo, txtNomCr, txtNacCr, txtTipoCr, txtIdCrExistente;
    private CardLayout cardLayout = new CardLayout();
    private JPanel pnlDinamico = new JPanel(cardLayout);
    private JRadioButton rbNuevo, rbExistente;
    private EventoController controller = new EventoController();

    public DialogNuevoEventoCreador(Frame parent) {
        super(parent, "Nuevo Evento y Creador", true);
        setLayout(new BorderLayout(10, 10));
        setSize(500, 500);
        setLocationRelativeTo(parent);
        
        this.controller = new EventoController();
        int ultimoIdEv = controller.obtenerUltimoIdEvento();
        int ultimoIdCr = controller.obtenerUltimoIdCreador();

        // --- SECCIÓN EVENTO ---
        JPanel pnlEvento = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlEvento.setBorder(BorderFactory.createTitledBorder("Datos del Evento"));
        txtIdEv = new JTextField(String.valueOf(ultimoIdEv + 1));
        txtNomEv = new JTextField();
        txtTipoEvento = new JTextField();
        txtIdTienda = new JTextField();
        
        pnlEvento.add(new JLabel(" ID Evento:")); pnlEvento.add(txtIdEv);
        pnlEvento.add(new JLabel(" Nombre Evento:")); pnlEvento.add(txtNomEv);
        pnlEvento.add(new JLabel(" Tipo Evento:")); pnlEvento.add(txtTipoEvento);
        pnlEvento.add(new JLabel(" ID Tienda:")); pnlEvento.add(txtIdTienda);
        
        add(new JSeparator()); add(new JSeparator()); 
        //CREADOR
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setBorder(BorderFactory.createTitledBorder("Datos del Creador"));
        
        // RadioButtons
        rbNuevo = new JRadioButton("Nuevo Creador", true);
        rbExistente = new JRadioButton("Creador Existente");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbNuevo); 
        grupo.add(rbExistente);
        
        JPanel pnlRadios = new JPanel();
        pnlRadios.add(rbNuevo);
        pnlRadios.add(rbExistente);
        pnlCentro.add(pnlRadios, BorderLayout.NORTH);

        // Panel Nuevo Creador (4 campos)
        pnlDinamico = new JPanel(cardLayout);
        JPanel pnlNuevo = new JPanel(new GridLayout(4, 2, 5, 5));
        
        txtIdCrNuevo = new JTextField(String.valueOf(ultimoIdCr + 1));
        txtNomCr = new JTextField();
        txtNacCr = new JTextField();
        txtTipoCr = new JTextField();
        
        pnlNuevo.add(new JLabel(" ID Creador:")); pnlNuevo.add(txtIdCrNuevo);
        pnlNuevo.add(new JLabel(" Nombre y Apellidos:")); pnlNuevo.add(txtNomCr);
        pnlNuevo.add(new JLabel(" Nacionalidad:")); pnlNuevo.add(txtNacCr);
        pnlNuevo.add(new JLabel(" Tipo:")); pnlNuevo.add(txtTipoCr);

        // Panel Creador Existente (1 campo)
        JPanel pnlExistente = new JPanel(new FlowLayout());
        txtIdCrExistente = new JTextField(10);
        pnlExistente.add(new JLabel("ID del Creador existente:"));
        pnlExistente.add(txtIdCrExistente);

        pnlDinamico.add(pnlNuevo, "NUEVO");
        pnlDinamico.add(pnlExistente, "EXISTENTE");
        pnlCentro.add(pnlDinamico, BorderLayout.CENTER);

        // Lógica de intercambio
        rbNuevo.addActionListener(e -> cardLayout.show(pnlDinamico, "NUEVO"));
        rbExistente.addActionListener(e -> cardLayout.show(pnlDinamico, "EXISTENTE"));

        // --- BOTÓN GUARDAR ---
        JButton btnGuardar = new JButton("Registrar Todo");
        btnGuardar.addActionListener(e -> ejecutarRegistro());

        add(pnlEvento, BorderLayout.NORTH);
        add(pnlCentro, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }
    
    private void ejecutarRegistro() {
        try {
            int idEv = Integer.parseInt(txtIdEv.getText());
            int idTienda = Integer.parseInt(txtIdTienda.getText());

            if (rbNuevo.isSelected()) {
                controller.insertarEventoYCreador(idEv, txtNomEv.getText(), txtTipoEvento.getText(), idTienda,
                    Integer.parseInt(txtIdCrNuevo.getText()), txtNomCr.getText(), txtNacCr.getText(), txtTipoCr.getText());
            } else {
                controller.insertarEventoConCreadorExistente(idEv, txtNomEv.getText(), txtTipoEvento.getText(), idTienda,
                    Integer.parseInt(txtIdCrExistente.getText()));
            }
            
            JOptionPane.showMessageDialog(this, "Evento Registrado con éxito");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}