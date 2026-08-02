package model;

public class Libro {

   
    private String Editorial;
    private int ISBN;
    private String Autor;
    private String Genero_literario;
    private int Num_paginas;
    private int PRODUCTO_ID_producto;
    private int PRODUCTO_TIENDA_ID_tienda;

   
    public Libro() {
    }

  
    public Libro(String Editorial, int ISBN, String Autor, String Genero_literario,
                 int Num_paginas, int PRODUCTO_ID_producto, int PRODUCTO_TIENDA_ID_tienda) {
        this.Editorial = Editorial;
        this.ISBN = ISBN;
        this.Autor = Autor;
        this.Genero_literario = Genero_literario;
        this.Num_paginas = Num_paginas;
        this.PRODUCTO_ID_producto = PRODUCTO_ID_producto;
        this.PRODUCTO_TIENDA_ID_tienda = PRODUCTO_TIENDA_ID_tienda;
    }

    public String getEditorial() {
        return Editorial;
    }

    public void setEditorial(String Editorial) {
        this.Editorial = Editorial;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String Autor) {
        this.Autor = Autor;
    }

    public String getGenero_literario() {
        return Genero_literario;
    }

    public void setGenero_literario(String Genero_literario) {
        this.Genero_literario = Genero_literario;
    }

    public int getNum_paginas() {
        return Num_paginas;
    }

    public void setNum_paginas(int Num_paginas) {
        this.Num_paginas = Num_paginas;
    }

    public int getPRODUCTO_ID_producto() {
        return PRODUCTO_ID_producto;
    }

    public void setPRODUCTO_ID_producto(int PRODUCTO_ID_producto) {
        this.PRODUCTO_ID_producto = PRODUCTO_ID_producto;
    }

    public int getPRODUCTO_TIENDA_ID_tienda() {
        return PRODUCTO_TIENDA_ID_tienda;
    }

    public void setPRODUCTO_TIENDA_ID_tienda(int PRODUCTO_TIENDA_ID_tienda) {
        this.PRODUCTO_TIENDA_ID_tienda = PRODUCTO_TIENDA_ID_tienda;
    }
}








