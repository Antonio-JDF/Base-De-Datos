package model;

import java.util.Date;

public class Reserva {

  
    private int ID_venta;
    private Date Fecha;
    private String Estado;
    private int Pago_previo;
    private int ID_ticket;

 
    public Reserva() {
    }

 
    public Reserva(int ID_venta, Date Fecha, String Estado, int Pago_previo, int ID_ticket) {
        this.ID_venta = ID_venta;
        this.Fecha = Fecha;
        this.Estado = Estado;
        this.Pago_previo = Pago_previo;
        this.ID_ticket = ID_ticket;
    }

    public int getID_venta() {
        return ID_venta;
    }

    public void setID_venta(int ID_venta) {
        this.ID_venta = ID_venta;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public int getPago_previo() {
        return Pago_previo;
    }

    public void setPago_previo(int Pago_previo) {
        this.Pago_previo = Pago_previo;
    }

    public int getID_ticket() {
        return ID_ticket;
    }

    public void setID_ticket(int ID_ticket) {
        this.ID_ticket = ID_ticket;
    }
}