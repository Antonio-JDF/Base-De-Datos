package model;

public class ItemTrabajador {
    public int id;
    public String nombre;

    public ItemTrabajador(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
}