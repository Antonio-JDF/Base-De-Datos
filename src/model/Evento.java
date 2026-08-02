package model;

import java.sql.Date;

public class Evento {
    private int idEvento;
    private String nombre;
    private String tipoEvento;
    private int idTienda;
    
    public Evento(){};
    public Evento(int idEvento, String nombre, Date fecha, String tipoEvento, int idTienda) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.tipoEvento = tipoEvento;
        this.idTienda = idTienda;
    }

    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public int getIdTienda() { return idTienda; }
    public void setIdTienda(int idTienda) { this.idTienda = idTienda; }
}