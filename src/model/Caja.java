package model;

public class Caja {

    private int ID_caja;
    private double Total_ventas_dia;
    private double Balance;

    public Caja() {
    }

    public Caja(int ID_caja, double Total_ventas_dia, double Balance) {
        this.ID_caja = ID_caja;
        this.Total_ventas_dia = Total_ventas_dia;
        this.Balance = Balance;
    }

    public int getID_caja() {
        return ID_caja;
    }

    public void setID_caja(int ID_caja) {
        this.ID_caja = ID_caja;
    }

    public double getTotal_ventas_dia() {
        return Total_ventas_dia;
    }

    public void setTotal_ventas_dia(double Total_ventas_dia) {
        this.Total_ventas_dia = Total_ventas_dia;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double Balance) {
        this.Balance = Balance;
    }
}