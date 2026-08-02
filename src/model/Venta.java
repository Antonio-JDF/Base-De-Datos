package model;

public class Venta {
    private int idVenta;
    private int cantidad;
    private double precioTotal;
    private double descuentoAplicado;
    private int idVendedor; // FK Trabajador
    private int idTicket;   // FK Ticket
    private int idCliente;  // FK Cliente
    
    public Venta() {};
    public Venta(int idVenta, int cantidad, double precioTotal, double descuentoAplicado, int idVendedor, int idTicket, int idCliente) {
        this.idVenta = idVenta;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.descuentoAplicado = descuentoAplicado;
        this.idVendedor = idVendedor;
        this.idTicket = idTicket;
        this.idCliente = idCliente;
    }

    // Getters y Setters
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(double precioTotal) { this.precioTotal = precioTotal; }

    public double getDescuentoAplicado() { return descuentoAplicado; }
    public void setDescuentoAplicado(double descuentoAplicado) { this.descuentoAplicado = descuentoAplicado; }

    public int getIdVendedor() { return idVendedor; }
    public void setIdVendedor(int idVendedor) { this.idVendedor = idVendedor; }

    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
}