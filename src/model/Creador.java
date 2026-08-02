package model;

public class Creador {

    private int ID_creador;
    private String Nombre;
    private String Nacionalidad;
    private String Tipo_creador;
    private int EVENTO_ID_evento;

    public Creador() {
    }

    public Creador(int ID_creador, String Nombre, String Nacionalidad,
                   String Tipo_creador, int EVENTO_ID_evento) {
        this.ID_creador = ID_creador;
        this.Nombre = Nombre;
        this.Nacionalidad = Nacionalidad;
        this.Tipo_creador = Tipo_creador;
        this.EVENTO_ID_evento = EVENTO_ID_evento;
    }

    public int getID_creador() {
        return ID_creador;
    }

    public void setID_creador(int ID_creador) {
        this.ID_creador = ID_creador;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getNacionalidad() {
        return Nacionalidad;
    }

    public void setNacionalidad(String Nacionalidad) {
        this.Nacionalidad = Nacionalidad;
    }

    public String getTipo_creador() {
        return Tipo_creador;
    }

    public void setTipo_creador(String Tipo_creador) {
        this.Tipo_creador = Tipo_creador;
    }

    public int getEVENTO_ID_evento() {
        return EVENTO_ID_evento;
    }

    public void setEVENTO_ID_evento(int EVENTO_ID_evento) {
        this.EVENTO_ID_evento = EVENTO_ID_evento;
    }
}