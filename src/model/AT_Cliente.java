package model;
public class AT_Cliente {

    private String Idiomas;
    private String Turno;
    private int TRABAJADOR_ID;

    public AT_Cliente() {
    }

    public AT_Cliente(String Idiomas, String Turno, int TRABAJADOR_ID) {
        this.Idiomas = Idiomas;
        this.Turno = Turno;
        this.TRABAJADOR_ID = TRABAJADOR_ID;
    }

    public String getIdiomas() {
        return Idiomas;
    }

    public void setIdiomas(String Idiomas) {
        this.Idiomas = Idiomas;
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