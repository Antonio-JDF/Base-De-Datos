package model;

public class Gerente {


    private String Turno;
    private int TRABAJADOR_ID;

 
    public Gerente() {
    }

    public Gerente(String Turno, int TRABAJADOR_ID) {
        this.Turno = Turno;
        this.TRABAJADOR_ID = TRABAJADOR_ID;
    }

    public String getTurno() {
        return Turno;
    }

    public void setTurno(String Turno) {
        this.Turno = Turno;
    }

    public int getTRABAJADOR_ID() {
        return TRABAJADOR_ID;
    }

    public void setTRABAJADOR_ID(int TRABAJADOR_ID) {
        this.TRABAJADOR_ID = TRABAJADOR_ID;
    }
}