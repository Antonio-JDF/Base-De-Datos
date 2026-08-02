package model; 
public class Trabajador {

    private int id_trabajador;
    private String Nombre;
    private String Apellido1;
    private String Apellido2;
    private String Fecha_Nacimiento;
    private String Fecha_Contrato;
    private double Salario;
    private int Tienda_id;
    private int Trabajador_id;
    private String Tipo_Trabajador;

public Trabajador() {
	
}

public Trabajador(int id, String nombre, String apellido1, String apellido2, String fecha_n, String fecha_c, double salario, int tienda_id, int trabajador_id, String tipo_T) {
    this.id_trabajador = id;
    this.Nombre = nombre;
    this.Apellido1 = apellido1;
    this.Apellido2 = apellido2;
    this.Fecha_Nacimiento = fecha_n;
    this.Fecha_Contrato = fecha_c;
    this.Salario = salario;
    this.Tienda_id = tienda_id;
    this.Trabajador_id = trabajador_id;
    this.Tipo_Trabajador = tipo_T;
}

// Getters y Setter
public int getID_trabajor() {
    return id_trabajador;
}

public void setID_trabajador(int id_trabajador) {
    this.id_trabajador = id_trabajador;
}

public String getNombre() {
    return Nombre;
}

public void setNombre(String Nombre) {
    this.Nombre = Nombre;
}

public String getApellido1() {
    return Apellido1;
}

public void setApellido1(String Apellido1) {
    this.Apellido1 = Apellido1;
}

public String getApellido2() {
    return Apellido2;
}

public void setApellido2(String Apellido2) {
    this.Apellido2 = Apellido2;
}

public String getFecha_Nacimiento() {
    return Fecha_Nacimiento;
}

public void setFecha_Nacimiento(String Fecha_Nacimiento) {
    this.Fecha_Nacimiento = Fecha_Nacimiento;
}

public String getFecha_Contrato() {
    return Fecha_Contrato;
}

public void setFecha_Contrato(String Fecha_Contrato) {
    this.Fecha_Contrato = Fecha_Contrato;
}

public double getSalario() {
    return Salario;
}

public void setSalario(double Salario) {
    this.Salario = Salario;
}

public int getTienda_id() {
    return Tienda_id;
}

public void setTienda_id(int Tienda_id) {
    this.Tienda_id = Tienda_id;
}

public int getTrabajador_id() {
    return Trabajador_id;
}

public void setTrabajador_id(int Trabajador_id) {
    this.Trabajador_id = Trabajador_id;
}

public String getTipo_Trabajador() {
    return Tipo_Trabajador;
}

public void setTipo_Trabajador(String Tipo_Trabajador) {
    this.Tipo_Trabajador = Tipo_Trabajador;
}
}
