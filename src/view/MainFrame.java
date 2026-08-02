package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // Referencias a los paneles del sistema
    private PanelTarjetaSocio panelTarjetaSocio;
    private PanelCliente panelCliente;
    private PanelHistorial panelHistorial;
    private PanelCompraReserva panelCompra;
    private PanelReseñas panelReseñas;
    private PanelTrabajador panelTrabajador;
    private PanelCatalogo panelCatalogo;
    private PanelEventos panelEventos;
    private PanelTienda panelTienda;
    private PanelMenu panelMenu;
    private PanelDetalleProducto panelDetalleProducto;
    private PanelProveedores panelProveedores;
    

    public MainFrame() {
        setTitle("Sistema CCA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Inicialización de paneles según la estructura del proyecto
        panelMenu = new PanelMenu(this);
        panelTarjetaSocio = new PanelTarjetaSocio(this);
        panelCliente = new PanelCliente(this);
        panelHistorial = new PanelHistorial(this);
        panelCompra = new PanelCompraReserva(this);
        panelReseñas = new PanelReseñas(this);
        panelTrabajador = new PanelTrabajador(this);
        panelCatalogo = new PanelCatalogo(this);
        panelEventos = new PanelEventos(this);
        panelTienda=new PanelTienda(this);
        panelDetalleProducto = new PanelDetalleProducto(this);
        panelProveedores = new PanelProveedores(this);

        // Registro de paneles en el CardLayout
        mainContainer.add(panelMenu, "MENU");
        mainContainer.add(panelTrabajador, "TRABAJADOR");
        mainContainer.add(panelTarjetaSocio, "TARJETA_SOCIO");
        mainContainer.add(panelCliente, "CLIENTE");
        mainContainer.add(panelHistorial, "HISTORIAL");
        mainContainer.add(panelCompra, "COMPRA");
        mainContainer.add(panelReseñas, "RESEÑAS");
        mainContainer.add(panelCatalogo, "CATALOGO");
        mainContainer.add(panelEventos, "EVENTOS");
        mainContainer.add(panelTienda, "TIENDA");
        mainContainer.add(panelDetalleProducto, "DETALLE_PRODUCTO");
        mainContainer.add(panelProveedores, "PROVEEDORES");

        add(mainContainer);
        cardLayout.show(mainContainer, "MENU");
    }

    // Getters para permitir el paso de datos entre pantallas
    public PanelTarjetaSocio getPanelTarjetaSocio() { return panelTarjetaSocio; }
    public PanelCliente getPanelCliente() { return panelCliente; }
    public PanelHistorial getPanelHistorial() { return panelHistorial; }
    public PanelCompraReserva getPanelCompra() { return panelCompra; }
    public PanelReseñas getPanelReseñas() { return panelReseñas; }
    public PanelTrabajador getPanelTrabajador() { return panelTrabajador; }
    public PanelCatalogo getPanelCatalogo() { return panelCatalogo; }
    public PanelEventos getPanelEventos() { return panelEventos; }
    public PanelDetalleProducto getPanelDetalleProducto() { return panelDetalleProducto; }
    public PanelProveedores getPanelProveedores() { return panelProveedores; }

    public void cambiarPanel(String nombrePanel) {
        cardLayout.show(mainContainer, nombrePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame();
            f.setVisible(true);
        });
    }
    
    public void mostrarPerfilSocio(int idCliente) {
        if (this.panelCliente != null) {
            this.panelCliente.setIdCliente(idCliente); 
            this.panelCliente.cargarDatosCliente(idCliente);    
        }
        
        // 2. Usamos tu función existente para cambiar la vista
        cambiarPanel("CLIENTE"); 
    }
}