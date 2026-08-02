package view;

import controller.ClienteController;
import controller.TrabajadorController;
import javax.swing.*;
import java.awt.*;

public class PanelMenu extends JPanel {
    private MainFrame frame;
    private ClienteController clienteCtrl;
    private TrabajadorController trabajadorCtrl;
    
    // Declaramos los botones
    private JButton btnTrabajador;
    private JButton btnCliente;
    private JButton btnCatalogo;
    private JButton btnEventos;
    private JButton btnTiendas;
    private JButton btnProveedores;

    public PanelMenu(MainFrame frame) {
        this.frame = frame;
        
        // 1. Inicialización de controladores 
        if (!java.beans.Beans.isDesignTime()) {
            this.clienteCtrl = new ClienteController();
            this.trabajadorCtrl = new TrabajadorController();
        }

        // 2. Configuración del Layout Principal
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        // --- ENCABEZADO ---
        JLabel titulo = new JLabel("BIENVENIDO");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        add(titulo, BorderLayout.NORTH);

     // --- PANEL CENTRAL DE BOTONES ---
        // Usamos 3 filas y 2 columnas para que no se amontonen
        JPanel pnlBotones = new JPanel();
        pnlBotones.setLayout(new GridLayout(3, 2, 30, 30));
        add(pnlBotones, BorderLayout.CENTER);
        Font fuenteBotones = new Font("Segoe UI", Font.BOLD, 18);
        
        this.btnTrabajador = new JButton("Acceso Trabajadores");
        this.btnTrabajador.setFont(fuenteBotones);
        this.btnCliente = new JButton("Acceso Clientes");
        this.btnCliente.setFont(fuenteBotones);
        this.btnCatalogo = new JButton("Consultar Catálogo");
        this.btnCatalogo.setFont(fuenteBotones);
        this.btnEventos = new JButton("Eventos");
        this.btnEventos.setFont(fuenteBotones);
        this.btnTiendas = new JButton("Tiendas");
        this.btnTiendas.setFont(fuenteBotones);
        this.btnProveedores = new JButton("Proveedores");
        this.btnProveedores.setFont(fuenteBotones);
        
        // Añadimos al panel
        pnlBotones.add(btnTrabajador);
        pnlBotones.add(btnCliente);
        pnlBotones.add(btnCatalogo);
        pnlBotones.add(btnEventos);
        pnlBotones.add(btnTiendas);
        pnlBotones.add(btnProveedores);

        // --- PIE DE PÁGINA ---
        JLabel lblPie = new JLabel("Pulse un botón para acceder | Bases de Datos 2025/2026");
        lblPie.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblPie, BorderLayout.SOUTH);

        // 3. ASIGNACIÓN DE EVENTOS (Solo si no es tiempo de diseño)
        if (!java.beans.Beans.isDesignTime()) {
            configurarEventos();
        }
    }

    private void configurarEventos() {
        btnTrabajador.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "ID de Trabajador:");
            if (input != null && !input.isEmpty()) {
                try {
                    int id = Integer.parseInt(input);
                    if (trabajadorCtrl.validarTrabajador(id)) {
                        frame.getPanelTrabajador().cargarDatos(id);
                        frame.getPanelCatalogo().setModoTrabajador(true);
                        frame.getPanelEventos().setModoTrabajador(true);
                        frame.cambiarPanel("TRABAJADOR");
                    } else {
                        JOptionPane.showMessageDialog(this, "ID no encontrado.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        btnCliente.addActionListener(e -> {
            // Creamos un panel para pedir el ID
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Etiqueta y campo para ID
            JLabel lblId = new JLabel("ID de Cliente:");
            JTextField txtId = new JTextField(10);
            panel.add(lblId);
            panel.add(txtId);

            // Botón para crear un nuevo cliente
            JButton btnNuevo = new JButton("Nuevo Cliente");
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio
            panel.add(btnNuevo);

            // Variable para detectar si se pulsó "Nuevo Cliente"
            final boolean[] nuevoCliente = {false};

            // Acción del botón "Nuevo Cliente"
            btnNuevo.addActionListener(ev -> {
                nuevoCliente[0] = true;
                // Cerramos el diálogo
                SwingUtilities.getWindowAncestor(panel).dispose();
            });

            // Mostramos el diálogo con OK y Cancel
            int result = JOptionPane.showConfirmDialog(this, panel, "Acceso Cliente",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (nuevoCliente[0]) {
                // Abrimos el diálogo de nuevo cliente
                DialogNuevoCliente dialog = new DialogNuevoCliente(frame);
                dialog.setVisible(true);
            } else if (result == JOptionPane.OK_OPTION) {
                String input = txtId.getText();
                if (input != null && !input.isEmpty()) {
                    try {
                        int id = Integer.parseInt(input);
                        if (clienteCtrl.validarCliente(id)) {
                            frame.getPanelCliente().cargarDatosCliente(id);
                            frame.cambiarPanel("CLIENTE");
                        } else {
                            JOptionPane.showMessageDialog(this, "Socio no registrado.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "ID inválido.");
                    }
                }
            }
        });


        btnCatalogo.addActionListener(e -> {
            frame.getPanelCatalogo().setModoTrabajador(false);
            frame.getPanelCatalogo().cargarProductos();
            frame.cambiarPanel("CATALOGO");
        });

        btnEventos.addActionListener(e -> {
            frame.getPanelEventos().setModoTrabajador(false);
            frame.getPanelEventos().cargarDatos();
            frame.cambiarPanel("EVENTOS");
        });

        btnTiendas.addActionListener(e -> frame.cambiarPanel("TIENDA"));
        
        btnProveedores.addActionListener(e -> {
            frame.getPanelProveedores().cargarProveedores();
            frame.cambiarPanel("PROVEEDORES");
        });
    }
}
