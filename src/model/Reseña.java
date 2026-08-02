package model;

import java.sql.Date;

public class Reseña {
    private int idReseña;
    private double puntuacion;
    private String comentario;
    private Date fecha;
    private int idCliente;
    private int idProducto;
    private int idTienda;
    
    public Reseña() {};
    public Reseña(int idReseña, double puntuacion, String comentario, Date fecha, int idCliente, int idProducto, int idTienda) {
        this.idReseña = idReseña;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.idTienda = idTienda;
    }

    // Getters y Setters
    public int getIdReseña() { return idReseña; }
    public void setIdReseña(int idReseña) { this.idReseña = idReseña; }

    public double getPuntuacion() { return puntuacion; }
    public void setPuntuacion(double puntuacion) { this.puntuacion = puntuacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getIdTienda() { return idTienda; }
    public void setIdTienda(int idTienda) { this.idTienda = idTienda; }
}