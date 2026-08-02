package model;

/**
 * Representa un producto individual dentro del carrito de compras.
 */
public class ItemCarrito {
    private int idProducto;
    private String nombre;
    private int cantidad;
    private double precio;
    private double subtotal;

    // Constructor completo
    public ItemCarrito(int idProducto, String nombre, int cantidad, double precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = cantidad * precio; // Cálculo automático del subtotal
    }

    // Getters y Setters necesarios para la lógica del controlador y la tabla
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad;
        actualizarSubtotal(); 
    }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { 
        this.precio = precio;
        actualizarSubtotal();
    }

    public double getSubtotal() { return subtotal; }

    // Método privado para mantener la integridad del dato económico
    private void actualizarSubtotal() {
        this.subtotal = this.cantidad * this.precio;
    }
}