package model;

public class Departamento {


    private int ID_departamento;
    private int Num_empleados;

  
    public Departamento() {
    }

   
    public Departamento(int ID_departamento, int Num_empleados) {
        this.ID_departamento = ID_departamento;
        this.Num_empleados = Num_empleados;
    }

    public int getID_departamento() {
        return ID_departamento;
    }

    public void setID_departamento(int ID_departamento) {
        this.ID_departamento = ID_departamento;
    }

    public int getNum_empleados() {
        return Num_empleados;
    }

    public void setNum_empleados(int Num_empleados) {
        this.Num_empleados = Num_empleados;
    }
}