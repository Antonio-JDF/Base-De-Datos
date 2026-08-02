package model;

public class Tienda {
	private int idTienda;
    private String direccion;
    private String ciudad;
    private String telefono;
    
    public Tienda() {};
    public Tienda(int id, String direccion, String ciudad, String telefono) {
        this.idTienda = id;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.telefono = telefono;
    }

    public int getIdTienda() { return idTienda; }
    public void setIdTienda(int idTienda) { this.idTienda = idTienda; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) {this.direccion = direccion;}
    
    public String getCiudad() {return ciudad;}
    public void setCiudad(String ciudad) {this.ciudad = ciudad;}
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    @Override
    public String toString() {
    	return ciudad + " - " + direccion + " (ID: " + idTienda + ")";
    }
}