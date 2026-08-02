package view;

import controller.ProductoController;
import controller.ProveedorController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DialogNuevoProducto extends JDialog {
    private JTextField txtId, txtNombre, txtPrecio, txtStock, txtTienda, txtCategoria, txtGarantia, txtProveedor;
    private JComboBox<String> cbTipo;
    private JPanel pnlDinamico;
    private Map<String, JComponent> camposExtra = new HashMap<>(); // Para guardar los valores de los subtipos
    private JLabel lblNombreArchivo;
    private String rutaImagenActual = null;
    private MainFrame frame;
    private ProductoController controller;

    public DialogNuevoProducto(MainFrame frame) {
        super(frame, "Insertar Nuevo Producto", true);
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setSize(550, 750);
        setLocationRelativeTo(frame);
        this.controller = new ProductoController();
        int ultimoId = controller.obtenerUltimoId(); // Sugerencia de ID

        // --- PARTE SUPERIOR: DATOS GENERALES (SUPER TIPO) ---
        JPanel pnlGeneral = new JPanel(new GridLayout(8, 2, 10, 10));
        pnlGeneral.setBorder(BorderFactory.createTitledBorder("Información de Producto"));
        
        pnlGeneral.add(new JLabel("  ID Producto (Último ID = " + ultimoId + " ):")); txtId = new JTextField(String.valueOf(ultimoId + 1)); 
        pnlGeneral.add(txtId);
        pnlGeneral.add(new JLabel("  Nombre:")); txtNombre = new JTextField(); pnlGeneral.add(txtNombre);
        pnlGeneral.add(new JLabel("  Precio:")); txtPrecio = new JTextField(); pnlGeneral.add(txtPrecio);
        pnlGeneral.add(new JLabel("  Stock:")); txtStock = new JTextField(); pnlGeneral.add(txtStock);
        pnlGeneral.add(new JLabel("  ID Tienda:")); txtTienda = new JTextField(); pnlGeneral.add(txtTienda);
        pnlGeneral.add(new JLabel("  Categoría (Género):")); txtCategoria = new JTextField(); pnlGeneral.add(txtCategoria);
        pnlGeneral.add(new JLabel("  Garantía (DD/MM/AAAA):")); txtGarantia = new JTextField(); pnlGeneral.add(txtGarantia);
        pnlGeneral.add(new JLabel("  ID Proveedor:")); txtProveedor = new JTextField(); pnlGeneral.add(txtProveedor);
        
        // --- PARTE CENTRAL: DESPLEGABLE Y CAMPOS DINÁMICOS ---
        JPanel pnlCentro = new JPanel(new BorderLayout(5, 5));
        
        JPanel pnlTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTipo.add(new JLabel("Seleccione Subtipo: "));
        cbTipo = new JComboBox<>(new String[]{"Cine", "Libro", "Música", "Tecnología", "Videojuego"});
        pnlTipo.add(cbTipo);
        
        pnlDinamico = new JPanel();
        pnlDinamico.setLayout(new BoxLayout(pnlDinamico, BoxLayout.Y_AXIS));
        pnlDinamico.setBorder(BorderFactory.createTitledBorder("Atributos Específicos del Subtipo"));

        cbTipo.addActionListener(e -> actualizarInterfazSubtipo());

        pnlCentro.add(pnlTipo, BorderLayout.NORTH);
        pnlCentro.add(new JScrollPane(pnlDinamico), BorderLayout.CENTER);

        // --- PARTE INFERIOR: IMAGEN Y GUARDAR ---
        JPanel pnlInferior = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnImg = new JButton("Elegir Imagen");
        lblNombreArchivo = new JLabel("Sin archivo");
        btnImg.addActionListener(e -> seleccionarImagen());
        
        JButton btnOk = new JButton("Guardar Producto Completo");
        btnOk.addActionListener(e -> guardarProducto());

        pnlInferior.add(btnImg); pnlInferior.add(lblNombreArchivo);
        pnlInferior.add(new JLabel("")); pnlInferior.add(btnOk);

        add(pnlGeneral, BorderLayout.NORTH);
        add(pnlCentro, BorderLayout.CENTER);
        add(pnlInferior, BorderLayout.SOUTH);

        actualizarInterfazSubtipo(); // Carga inicial
    }

    private void actualizarInterfazSubtipo() {
        pnlDinamico.removeAll();
        camposExtra.clear();
        String seleccion = (String) cbTipo.getSelectedItem();
        String[] atributos = {};

        // Definición de atributos según el diagrama
        switch (seleccion) {
            case "Tecnología": atributos = new String[]{"Marca", "Modelo", "Especificaciones técnicas", "Número serie"}; break;
            case "Videojuego": atributos = new String[]{"Plataforma", "Desarrolladora", "Clasificación edad", "Modo", "Género"}; break;
            case "Libro": atributos = new String[]{"Editorial", "ISBN", "Autor", "Genero literario", "Num paginas"}; break;
            case "Música": atributos = new String[]{"Artista", "Formato", "Duración", "Fecha de lanzamiento", "Discográfica"}; break;
            case "Cine": atributos = new String[]{"Director", "Formato", "Duración", "Productora", "Fecha lanzamiento"}; break;
        }

        for (String attr : atributos) {
        	JPanel p = new JPanel(new BorderLayout(5, 2));
            p.setMaximumSize(new Dimension(500, 40));
            p.add(new JLabel(attr + ":"), BorderLayout.WEST);

            JComponent input;
            // Lógica para cambiar JTextField por JComboBox según el dominio
            if (attr.equals("Formato") && (seleccion.equals("Música") || seleccion.equals("Cine"))) {
                input = new JComboBox<>(new String[]{"BLU-RAY", "CD", "DVD", "VINILO"});
            } else if (attr.equals("Plataforma") && seleccion.equals("Videojuego")) {
                input = new JComboBox<>(new String[]{"PS5", "Xbox Series", "Nintendo Switch", "PC", "PS4"});
            } else if (attr.equals("Modo") && seleccion.equals("Videojuego")) {
                input = new JComboBox<>(new String[]{"Multijugador", "Online_individual", "Online_multijugador", "Un_Jugador"});
            } else {
                input = new JTextField();
            }

            p.add(input, BorderLayout.CENTER);
            pnlDinamico.add(p);
            camposExtra.put(attr, input);
        }
        pnlDinamico.revalidate();
        pnlDinamico.repaint();
    }

    private void seleccionarImagen() {
        JFileChooser jf = new JFileChooser();
        if(jf.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = jf.getSelectedFile();
            this.rutaImagenActual = f.getAbsolutePath();
            lblNombreArchivo.setText(f.getName());
            lblNombreArchivo.setForeground(new Color(0, 102, 0));
        }
    }

    private void guardarProducto() {
        try {
        	if (txtId == null || txtGarantia == null || cbTipo == null) {
                throw new Exception("Error interno: Componentes de la interfaz no inicializados.");
            }
        	
        	if (txtId.getText().isEmpty() || txtNombre.getText().isEmpty() || txtGarantia.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Nombre y Garantía son obligatorios.");
                return;
            }
            // Captura de datos básicos
            int id = Integer.parseInt(txtId.getText());
            int tienda = Integer.parseInt(txtTienda.getText());
            String nom = txtNombre.getText();
            double pre = Double.parseDouble(txtPrecio.getText().replace(",", "."));
            int stk = Integer.parseInt(txtStock.getText());
            String cat = txtCategoria.getText();
            String gar = txtGarantia.getText();
            String tipo = (String) cbTipo.getSelectedItem();
            int proveedor = Integer.parseInt(txtProveedor.getText());

            // Capturamos todos los atributos extra del mapa
            Map<String, String> valoresExtra = new HashMap<>(); 
            for (Map.Entry<String, JComponent> entry : camposExtra.entrySet()) {
                String valor = "";
                if (entry.getValue() instanceof JTextField) {
                    valor = ((JTextField) entry.getValue()).getText();
                } else if (entry.getValue() instanceof JComboBox) {
                    valor = ((JComboBox<?>) entry.getValue()).getSelectedItem().toString();
                }
                valoresExtra.put(entry.getKey(), valor);
            }

            ProductoController pc = new ProductoController();
            pc.insertarProductoYSubtipo(id, tienda,proveedor, nom, pre, stk, cat,gar, tipo, rutaImagenActual, valoresExtra);

            JOptionPane.showMessageDialog(this, "Producto y Subtipo guardados correctamente.");
            if (frame != null && frame.getPanelCatalogo() != null) {
                frame.getPanelCatalogo().cargarProductos();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}