package model;

public class Tecnologia {

   
    private String Marca;
    private String Modelo;
    private String Especificaciones_tecnicas;
    private int Número_serie;
    private int PRODUCTO_ID_producto;
    private int PRODUCTO_TIENDA_ID_tienda;

 
    public Tecnologia() {
    }

  
    public Tecnologia(String Marca, String Modelo, String Especificaciones_tecnicas,
                      int Número_serie, int PRODUCTO_ID_producto, int PRODUCTO_TIENDA_ID_tienda) {
        this.Marca = Marca;
        this.Modelo = Modelo;
        this.Especificaciones_tecnicas = Especificaciones_tecnicas;
        this.Número_serie = Número_serie;
        this.PRODUCTO_ID_producto = PRODUCTO_ID_producto;
        this.PRODUCTO_TIENDA_ID_tienda = PRODUCTO_TIENDA_ID_tienda;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String Modelo) {
        this.Modelo = Modelo;
    }

    public String getEspecificaciones_tecnicas() {
        return Especificaciones_tecnicas;
    }

    public void setEspecificaciones_tecnicas(String Especificaciones_tecnicas) {
        this.Especificaciones_tecnicas = Especificaciones_tecnicas;
    }

    public int getNúmero_serie() {
        return Número_serie;
    }

    public void setNúmero_serie(int Número_serie) {
        this.Número_serie = Número_serie;
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
