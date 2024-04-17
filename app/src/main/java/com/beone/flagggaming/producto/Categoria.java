package com.beone.flagggaming.producto;

public class Categoria {
    int id_categoria;
    String desc_categoria;

    public Categoria(){

    }
    public Categoria(int id_categoria, String desc_categoria) {
        this.id_categoria = id_categoria;
        this.desc_categoria = desc_categoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getDesc_categoria() {
        return desc_categoria;
    }

    public void setDesc_categoria(String desc_categoria) {
        this.desc_categoria = desc_categoria;
    }

    @Override
    public String toString() {
        return desc_categoria; // Esto es importante para que el Spinner muestre la descripción de la categoría en lugar del objeto completo
    }
}
