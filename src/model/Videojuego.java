package model;

public class Videojuego {


    private String Plataforma;
    private String Desarrolladora;
    private int Clasificacion_edad;
    private String Modo;
    private String Genero;
    private int PRODUCTO_ID_producto;
    private int PRODUCTO_TIENDA_ID_tienda;


    public Videojuego() {
    }

  
    public Videojuego(String Plataforma, String Desarrolladora, int Clasificacion_edad, String Modo,
                      String Genero, int PRODUCTO_ID_producto, int PRODUCTO_TIENDA_ID_tienda) {
        this.Plataforma = Plataforma;
        this.Desarrolladora = Desarrolladora;
        this.Clasificacion_edad = Clasificacion_edad;
        this.Modo = Modo;
        this.Genero = Genero;
        this.PRODUCTO_ID_producto = PRODUCTO_ID_producto;
        this.PRODUCTO_TIENDA_ID_tienda = PRODUCTO_TIENDA_ID_tienda;
    }

    public String getPlataforma() {
        return Plataforma;
    }

    public void setPlataforma(String Plataforma) {
        this.Plataforma = Plataforma;
    }

    public String getDesarrolladora() {
        return Desarrolladora;
    }

    public void setDesarrolladora(String Desarrolladora) {
        this.Desarrolladora = Desarrolladora;
    }

    public int getClasificacion_edad() {
        return Clasificacion_edad;
    }

    public void setClasificacion_edad(int Clasificacion_edad) {
        this.Clasificacion_edad = Clasificacion_edad;
    }

    public String getModo() {
        return Modo;
    }

    public void setModo(String Modo) {
        this.Modo = Modo;
    }

    public String getGénero() {
        return Genero;
    }

    public void setGénero(String Genero) {
        this.Genero = Genero;
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
