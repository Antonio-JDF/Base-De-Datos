package model;

public class Reservado {


    private int VENTA_ID_venta;
    private int PRODUCTO_ID_producto;
    private int PRODUCTO_TIENDA_ID_tienda;

 
    public Reservado() {
    }

  
    public Reservado(int VENTA_ID_venta, int PRODUCTO_ID_producto, int PRODUCTO_TIENDA_ID_tienda) {
        this.VENTA_ID_venta = VENTA_ID_venta;
        this.PRODUCTO_ID_producto = PRODUCTO_ID_producto;
        this.PRODUCTO_TIENDA_ID_tienda = PRODUCTO_TIENDA_ID_tienda;
    }

    public int getVENTA_ID_venta() {
        return VENTA_ID_venta;
    }

    public void setVENTA_ID_venta(int VENTA_ID_venta) {
        this.VENTA_ID_venta = VENTA_ID_venta;
    }

    public int getPRODUCTO_ID_producto() {
        return PRODUCTO_ID_producto;
    }

    public void setPRODUCTO_ID_producto(int PRODUCTO_ID_producto) {
        this.PRODUCTO_ID_producto = PRODUCTO_ID_producto;
    }

    public int getPRODUCTO_TIENDA_ID_tienda() {
        return PRODUCTO_TIENDA_ID_tienda;
    }

    public void setPRODUCTO_TIENDA_ID_tienda(int PRODUCTO_TIENDA_ID_tienda) {
        this.PRODUCTO_TIENDA_ID_tienda = PRODUCTO_TIENDA_ID_tienda;
    }
}