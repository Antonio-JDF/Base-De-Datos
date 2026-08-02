package model;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
    private int idTarjetaSocio; // FK hacia TARJETA_DE_SOCIO
    private String nivelTarjeta;
    
    public Cliente() {};
    public Cliente(int idCliente, String nombre, String apellido1, String apellido2, String email, int idTarjetaSocio, String nivelTarjeta) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.email = email;
        this.idTarjetaSocio = idTarjetaSocio;
        this.nivelTarjeta = nivelTarjeta;
    }

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getIdTarjetaSocio() { return idTarjetaSocio; }
    public void setIdTarjetaSocio(int idTarjetaSocio) { this.idTarjetaSocio = idTarjetaSocio; }

    public String getNivelTarjeta() { return nivelTarjeta; }
    public void setNivelTarjeta(String nivelTarjeta) { this.nivelTarjeta = nivelTarjeta; }

    @Override
    public String toString() {
        return nombre + " " + apellido1 + " (" + email + ")";
    }
}