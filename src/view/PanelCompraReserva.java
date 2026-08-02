package view;

import controller.VentaController;
import conection.ConnectionJDBC;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import model.ItemCarrito;


public class PanelCompraReserva extends JPanel {
	private MainFrame frame;
    private JComboBox<String> cbProductos, cbFormaPago;
    private JTextField txtCantidad;
    private JTable tablaCarrito;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private int idClienteActual;
    private List<ItemCarrito> carrito = new ArrayList<>();
    private JRadioButton rbCompra, rbReserva;
    private JTextField txtPagoPrevio;
    private JPanel pnlReservaExtra;
    private JTextField txtEntregado;
    private JLabel lblEntregado;

    public PanelCompraReserva(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // --- NORTE: Selección de producto ---
        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbProductos = new JComboBox<>();
        cargarProductosCombo();
        txtCantidad = new JTextField("1", 3);
        JButton btnAgregar = new JButton("Añadir al Carrito");
        
        pnlNorte.add(new JLabel("Producto:")); pnlNorte.add(cbProductos);
        pnlNorte.add(new JLabel("Cant:")); pnlNorte.add(txtCantidad);
        pnlNorte.add(btnAgregar);

        // --- CENTRO: Tabla de Carrito ---
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Producto", "Cant", "Subtotal"}, 0);
        tablaCarrito = new JTable(modeloTabla);
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        // --- SUR: Total y Botones ---
        JPanel pnlInferior = new JPanel(new GridLayout(3, 1, 5, 5));

        // Fila A: Radio Buttons para elegir Compra o Reserva
        JPanel pnlOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbCompra = new JRadioButton("Compra Directa", true);
        rbReserva = new JRadioButton("Reserva");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbCompra); grupo.add(rbReserva);
        
        // Fila B: Panel de Pago Previo (Solo visible si es reserva)
        pnlReservaExtra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlReservaExtra.add(new JLabel("Pago Previo (€):"));
        txtPagoPrevio = new JTextField("0", 7);
        pnlReservaExtra.add(txtPagoPrevio);
        pnlReservaExtra.setVisible(false); // Oculto al inicio

        // Fila C: Total y Botones de acción
        JPanel pnlAcciones = new JPanel(new BorderLayout());
        JPanel pnlIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        
        lblTotal = new JLabel("TOTAL: 0.00 €");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        
        lblEntregado = new JLabel("Dinero Entregado €:");
        txtEntregado = new JTextField(6);
        
        pnlIzquierda.add(lblTotal);
        pnlIzquierda.add(lblEntregado);
        pnlIzquierda.add(txtEntregado);
        
        JPanel pnlBotonesDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cbFormaPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Financiación"});
        JButton btnComprar = new JButton("Confirmar Operación");
        JButton btnCancelar = new JButton("Cancelar");

        pnlBotonesDerecha.add(new JLabel("Pago:")); 
        pnlBotonesDerecha.add(cbFormaPago);
        pnlBotonesDerecha.add(btnCancelar); 
        pnlBotonesDerecha.add(btnComprar);
        
        pnlAcciones.add(pnlIzquierda, BorderLayout.WEST);
        pnlAcciones.add(pnlBotonesDerecha, BorderLayout.EAST);

        // Unir todo en el panel inferior
        pnlOpciones.add(rbCompra); pnlOpciones.add(rbReserva);
        pnlInferior.add(pnlOpciones);
        pnlInferior.add(pnlReservaExtra);
        pnlInferior.add(pnlAcciones);

        // AÑADIR AL PANEL PRINCIPAL
        add(pnlNorte, BorderLayout.NORTH);
        add(pnlInferior, BorderLayout.SOUTH);

        // --- LOGICA DE VISIBILIDAD ---
        rbReserva.addActionListener(e -> {
            pnlReservaExtra.setVisible(true);
            revalidate(); repaint(); // Fuerza a Java a redibujar el panel
        });
        rbCompra.addActionListener(e -> {
            pnlReservaExtra.setVisible(false);
            revalidate(); repaint();
        });

        // --- EVENTOS DE BOTONES ---
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        btnCancelar.addActionListener(e -> {
            limpiarCarrito();
            frame.mostrarPerfilSocio(idClienteActual);
        });
        btnComprar.addActionListener(e -> realizarCompra());
    }
    
    private void agregarAlCarrito() {
        try {
            String itemStr = (String) cbProductos.getSelectedItem();
            int id = Integer.parseInt(itemStr.split(" - ")[0]);
            String nombre = itemStr.split(" - ")[1];
            int cant = Integer.parseInt(txtCantidad.getText());
            VentaController control = new VentaController();
            double precioReal = control.obtenerPrecioProducto(id);
            if (precioReal > 0) {
                ItemCarrito item = new ItemCarrito(id, nombre, cant, precioReal);
                carrito.add(item);
                modeloTabla.addRow(new Object[]{id, nombre, cant, item.getSubtotal()});
                actualizarTotal();
            } else {
                JOptionPane.showMessageDialog(this, "El producto seleccionado no tiene un precio válido.");
            }
            
            
            actualizarTotal();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al añadir: " + ex.getMessage());
        }
    }

    private void realizarCompra() {
    	if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.");
            return;
        }
    	String textoEntregado = txtEntregado.getText().trim().replace(",", ".");
        if (textoEntregado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe indicar cuánto dinero entrega el cliente.");
            return ;
        }
        
    	boolean esReserva = rbReserva.isSelected(); // Si está marcado "Reserva", será true
        double pagoAcuenta = 0;
        VentaController control = new VentaController();
        String formaPago = (String) cbFormaPago.getSelectedItem();
        try {
        	double dineroEntregado = Double.parseDouble(textoEntregado);
            if (esReserva) {
                String textoPago = txtPagoPrevio.getText().trim();
                if (!textoPago.isEmpty()) {
                    pagoAcuenta = Double.parseDouble(textoPago);
                    //dineroEntregado=dineroEntregado+pagoAcuenta;
                    
                }
            }
            
            //double dineroEntregado = Double.parseDouble(textoEntregado);
            double total = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
            if (dineroEntregado+pagoAcuenta < total) {
                JOptionPane.showMessageDialog(null, "Importe insuficiente. Faltan: " + (total - dineroEntregado) + "€");
                return ;
            }

            if (control.procesarVentaMultiple(carrito, idClienteActual, formaPago, esReserva, pagoAcuenta,dineroEntregado)) {
                JOptionPane.showMessageDialog(this, "¡Operación realizada con éxito!");
                limpiarCarrito();
                frame.mostrarPerfilSocio(idClienteActual); // Volvemos al perfil del cliente
            } 
            // Nota: Si el controlador devuelve false, los mensajes de error (como pago > total)
            // ya se muestran dentro del controlador mediante JOptionPanes.

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: El pago previo debe ser un número válido.");
        }
    }

    public void setIdCliente(int id) { this.idClienteActual = id; }

    private void cargarProductosCombo() {
        try (Connection conn = ConnectionJDBC.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ID_producto, Nombre FROM PRODUCTO ORDER BY ID_producto")) {
            while (rs.next()) {
                cbProductos.addItem(rs.getInt("ID_producto") + " - " + rs.getString("Nombre"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    
    private void actualizarTotal() {
        double total = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        lblTotal.setText(String.format("TOTAL: %.2f €", total));
    }
    
    private void limpiarCarrito() {
        // 1. Vaciar la lista lógica
        carrito.clear();
        
        // 2. Vaciar la tabla visual
        modeloTabla.setRowCount(0);
        
        // 3. Resetear etiquetas y campos
        lblTotal.setText("TOTAL: 0.00 €");
        txtCantidad.setText("1");
        txtPagoPrevio.setText("0");
        rbCompra.setSelected(true);
        pnlReservaExtra.setVisible(false);
        
        // 4. Refrescar el panel
        revalidate();
        repaint();
    }
}