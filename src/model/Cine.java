package model;

public class Cine {

    private String Director;
    private String Formato;
    private int Duracion;
    private String Productora;
    private java.util.Date Fecha_lanzamiento;
    private int PRODUCTO_ID_producto;
    private int PRODUCTO_TIENDA_ID_tienda;

    public Cine() {
    }

    public Cine(String Director, String Formato, int Duracion, String Productora,
                java.util.Date Fecha_lanzamiento, int PRODUCTO_ID_producto,
                int PRODUCTO_TIENDA_ID_tienda) {
        this.Director = Director;
        this.Formato = Formato;
        this.Duracion = Duracion;
        this.Productora = Productora;
        this.Fecha_lanzamiento = Fecha_lanzamiento;
        this.PRODUCTO_ID_producto = PRODUCTO_ID_producto;
        this.PRODUCTO_TIENDA_ID_tienda = PRODUCTO_TIENDA_ID_tienda;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String Director) {
        this.Director = Director;
    }

    public String getFormato() {
        return Formato;
    }

    public void setFormato(String Formato) {
        this.Formato = Formato;
    }

    public int getDuracion() {
        return Duracion;
    }

    public void setDuracion(int Duracion) {
        this.Duracion = Duracion;
    }

    public String getProductora() {
        return Productora;
    }

    public void setProductora(String Productora) {
        this.Productora = Productora;
    }

    public java.util.Date getFecha_lanzamiento() {
        return Fecha_lanzamiento;
    }

    public void setFecha_lanzamiento(java.util.Date Fecha_lanzamiento) {
        this.Fecha_lanzamiento = Fecha_lanzamiento;
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