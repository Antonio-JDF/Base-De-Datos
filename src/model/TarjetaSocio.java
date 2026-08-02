package model;

import java.sql.Date;

public class TarjetaSocio {
    private int idTarjeta;
    private int puntosAcumulados;
    private String nivel;
    private Date fechaAlta;
    private Date fechaCaducidad;
    private byte[] foto; // BLOB convertido a bytes
    
    public TarjetaSocio() {};
    public TarjetaSocio(int idTarjeta, int puntosAcumulados, String nivel, Date fechaAlta, Date fechaCaducidad, byte[] foto) {
        this.idTarjeta = idTarjeta;
        this.puntosAcumulados = puntosAcumulados;
        this.nivel = nivel;
        this.fechaAlta = fechaAlta;
        this.fechaCaducidad = fechaCaducidad;
        this.foto = foto;
    }

    // Getters y Setters
    public int getIdTarjeta() { return idTarjeta; }
    public void setIdTarjeta(int idTarjeta) { this.idTarjeta = idTarjeta; }

    public int getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(int puntosAcumulados) { this.puntosAcumulados = puntosAcumulados; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Date getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(Date fechaAlta) { this.fechaAlta = fechaAlta; }

    public Date getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(Date fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
}