package model;

public class Vendedor {

    private String Turno;
    private double Comisiones;
    private int TRABAJADOR_ID;
    private int DEPARTAMENTO_ID_departamento;

 
    public Vendedor() {
    }

    public Vendedor(String Turno, double Comisiones, int TRABAJADOR_ID, int DEPARTAMENTO_ID_departamento) {
        this.Turno = Turno;
        this.Comisiones = Comisiones;
        this.TRABAJADOR_ID = TRABAJADOR_ID;
        this.DEPARTAMENTO_ID_departamento = DEPARTAMENTO_ID_departamento;
    }

    public String getTurno() {
        return Turno;
    }

    public void setTurno(String Turno) {
        this.Turno = Turno;
    }

    public double getComisiones() {
        return Comisiones;
    }

    public void setComisiones(double Comisiones) {
        this.Comisiones = Comisiones;
    }

    public int getTRABAJADOR_ID() {
        return TRABAJADOR_ID;
    }

    public void setTRABAJADOR_ID(int TRABAJADOR_ID) {
        this.TRABAJADOR_ID = TRABAJADOR_ID;
    }

    public int getDEPARTAMENTO_ID_departamento() {
        return DEPARTAMENTO_ID_departamento;
    }

    public void setDEPARTAMENTO_ID_departamento(int DEPARTAMENTO_ID_departamento) {
        this.DEPARTAMENTO_ID_departamento = DEPARTAMENTO_ID_departamento;
    }
}
