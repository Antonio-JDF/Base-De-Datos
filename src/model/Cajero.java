package model;

public class Cajero {

    
    private String Turno;
    private int TRABAJADOR_ID;
    private int CAJA_ID_caja;


    public Cajero() {
    }

 
    public Cajero(String Turno, int TRABAJADOR_ID, int CAJA_ID_caja) {
        this.Turno = Turno;
        this.TRABAJADOR_ID = TRABAJADOR_ID;
        this.CAJA_ID_caja = CAJA_ID_caja;
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

    public int getCAJA_ID_caja() {
        return CAJA_ID_caja;
    }

    public void setCAJA_ID_caja(int CAJA_ID_caja) {
        this.CAJA_ID_caja = CAJA_ID_caja;
    }
}