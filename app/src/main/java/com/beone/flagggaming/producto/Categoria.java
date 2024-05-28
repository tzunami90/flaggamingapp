package com.beone.flagggaming.producto;
import com.beone.flagggaming.R;
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

    public int getCategoriaImageResource() {
        switch (id_categoria) {
            case 1: return R.drawable.cat1;
            case 2: return R.drawable.cat2;
            case 3: return R.drawable.cat3;
            case 4: return R.drawable.cat4;
            case 5: return R.drawable.cat5;
            case 6: return R.drawable.cat6;
            case 7: return R.drawable.cat7;
            case 8: return R.drawable.cat8;
            case 9: return R.drawable.cat9;
            case 10: return R.drawable.cat10;
            case 11: return R.drawable.cat11;
            default: return R.drawable.nopic; // Imagen por defecto si no coincide ninguna categoría
        }
    }

    @Override
    public String toString() {
        return desc_categoria; // Esto es importante para que el Spinner muestre la descripción de la categoría en lugar del objeto completo
    }
}
