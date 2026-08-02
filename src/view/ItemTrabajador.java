package view;

public class ItemTrabajador {

    private int id;
    private String texto;

    public ItemTrabajador(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return texto;
    }
}