package model;

import java.util.Date;

public class Musica {

   
    private String Artista;
    private String Formato;
    private int Duracion;
    private Date Año_lanzamiento;
    private String Discografica;
    private int PRODUCTO_ID_producto;
    private int PRODUCTO_TIENDA_ID_tienda;

  
    public Musica() {
    }

 
    public Musica(String Artista, String Formato, int Duracion, Date Año_lanzamiento,
                  String Discografica, int PRODUCTO_ID_producto, int PRODUCTO_TIENDA_ID_tienda) {
        this.Artista = Artista;
        this.Formato = Formato;
        this.Duracion = Duracion;
        this.Año_lanzamiento = Año_lanzamiento;
        this.Discografica = Discografica;
        this.PRODUCTO_ID_producto = PRODUCTO_ID_producto;
        this.PRODUCTO_TIENDA_ID_tienda = PRODUCTO_TIENDA_ID_tienda;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtista(String Artista) {
        this.Artista = Artista;
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

    public Date getAño_lanzamiento() {
        return Año_lanzamiento;
    }

    public void setAño_lanzamiento(Date Año_lanzamiento) {
        this.Año_lanzamiento = Año_lanzamiento;
    }

    public String getDiscografica() {
        return Discografica;
    }

    public void setDiscografica(String Discografica) {
        this.Discografica = Discografica;
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