package model;

public class Proveedor {

   
    private int ID_proveedor;
    private String Nombre;
    private String Pais;

  
    public Proveedor() {
    }

 
    public Proveedor(int ID_proveedor, String Nombre, String Pais) {
        this.ID_proveedor = ID_proveedor;
        this.Nombre = Nombre;
        this.Pais = Pais;
    }

    public int getID_proveedor() {
        return ID_proveedor;
    }

    public void setID_proveedor(int ID_proveedor) {
        this.ID_proveedor = ID_proveedor;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String Pais) {
        this.Pais = Pais;
    }
}