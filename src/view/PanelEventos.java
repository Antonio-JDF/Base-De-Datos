package view;

import controller.EventoController; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelEventos extends JPanel {
    private MainFrame frame;
    private JTable tablaEventos;
    private EventoController controller;
    private JButton btnNuevoEvento;

    public PanelEventos(MainFrame frame) {
        this.frame = frame;
        
        // Evitamos errores en WindowBuilder
        if (!java.beans.Beans.isDesignTime()) {
            this.controller = new EventoController();
        }

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ENCABEZADO ---
        JLabel titulo = new JLabel("Cartelera de Eventos y Creadores", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA ---
        tablaEventos = new JTable();
        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);

        // --- BOTONES ---
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        
        JButton btnNuevo = new JButton("Programar Evento");
        btnNuevo.addActionListener(e -> {
            // Abrimos el diálogo doble
            new DialogNuevoEventoCreador(frame).setVisible(true);
            cargarDatos();
        });
        
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> frame.cambiarPanel("MENU"));

        pnlBotones.add(btnNuevo);
        pnlBotones.add(btnVolver);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    /**
     * Esta función activa el botón cuando entramos como trabajador.
     * Soluciona el segundo error de "cannot be resolved".
     */
    public void setModoTrabajador(boolean esTrabajador) {
        if (btnNuevoEvento != null) {
            btnNuevoEvento.setVisible(esTrabajador);
        }
    }

    /**
     * Carga los datos de EVENTO y CREADOR desde el DDL.
     */
    public void cargarDatos() {
        if (controller != null) {
            DefaultTableModel modelo = controller.obtenerEventosConCreadores();
            tablaEventos.setModel(modelo);
        }
    }
}