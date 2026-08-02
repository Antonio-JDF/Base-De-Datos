package model;

import java.sql.Date;

public class Producto {
    private int idProducto;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;
    private Date garantia;
    private int idProveedor;
    private String tipoProducto;
    private byte[] portada; // BLOB convertido a bytes
    private int idTienda;   // Parte de la PK compuesta
    
    public Producto() {};
    public Producto(int idProducto, String nombre, String categoria, double precio, int stock, Date garantia, int idProveedor, String tipoProducto, byte[] portada, int idTienda) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.garantia = garantia;
        this.idProveedor = idProveedor;
        this.tipoProducto = tipoProducto;
        this.portada = portada;
        this.idTienda = idTienda;
    }

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Date getGarantia() { return garantia; }
    public void setGarantia(Date garantia) { this.garantia = garantia; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }

    public byte[] getPortada() { return portada; }
    public void setPortada(byte[] portada) { this.portada = portada; }

    public int getIdTienda() { return idTienda; }
    public void setIdTienda(int idTienda) { this.idTienda = idTienda; }
    
    @Override
    public String toString() {
        return nombre + " - " + precio + "€";
    }
}