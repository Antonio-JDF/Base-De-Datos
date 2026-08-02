package model;

import java.util.Date;

public class Ticket {

    // Atributos
    private int ID_ticket;
    private Date Fecha_hora;
    private double Total_pagado;
    private double Total_venta;
    private double Vuelta_dinero;
    private String Forma_pago;
    private double Total_a_pagar;
    private int CAJA_ID_caja;

    // Constructor vacío
    public Ticket() {
    }

    // Constructor parametrizado
    public Ticket(int ID_ticket, Date Fecha_hora, double Total_pagado, double Total_venta,
                  double Vuelta_dinero, String Forma_pago, double Total_a_pagar, int CAJA_ID_caja) {
        this.ID_ticket = ID_ticket;
        this.Fecha_hora = Fecha_hora;
        this.Total_pagado = Total_pagado;
        this.Total_venta = Total_venta;
        this.Vuelta_dinero = Vuelta_dinero;
        this.Forma_pago = Forma_pago;
        this.Total_a_pagar = Total_a_pagar;
        this.CAJA_ID_caja = CAJA_ID_caja;
    }

    public int getID_ticket() {
        return ID_ticket;
    }

    public void setID_ticket(int ID_ticket) {
        this.ID_ticket = ID_ticket;
    }

    public Date getFecha_hora() {
        return Fecha_hora;
    }

    public void setFecha_hora(Date Fecha_hora) {
        this.Fecha_hora = Fecha_hora;
    }

    public double getTotal_pagado() {
        return Total_pagado;
    }

    public void setTotal_pagado(double Total_pagado) {
        this.Total_pagado = Total_pagado;
    }

    public double getTotal_venta() {
        return Total_venta;
    }

    public void setTotal_venta(double Total_venta) {
        this.Total_venta = Total_venta;
    }

    public double getVuelta_dinero() {
        return Vuelta_dinero;
    }

    public void setVuelta_dinero(double Vuelta_dinero) {
        this.Vuelta_dinero = Vuelta_dinero;
    }

    public String getForma_pago() {
        return Forma_pago;
    }

    public void setForma_pago(String Forma_pago) {
        this.Forma_pago = Forma_pago;
    }

    public double getTotal_a_pagar() {
        return Total_a_pagar;
    }

    public void setTotal_a_pagar(double Total_a_pagar) {
        this.Total_a_pagar = Total_a_pagar;
    }

    public int getCAJA_ID_caja() {
        return CAJA_ID_caja;
    }

    public void setCAJA_ID_caja(int CAJA_ID_caja) {
        this.CAJA_ID_caja = CAJA_ID_caja;
    }
}
