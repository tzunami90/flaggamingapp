package com.beone.flagggaming.juegos;

public class Juego {

    private String idFlagg;
    private int idJuegoTienda;
    private String nombre;
    private String descripcionCorta;
    private String tienda;
    private String imagen;
    private String imagenMini;
    private String urlTienda;
    private String requisitos;
    private String estudio;
    private int contadorVistas;

    public Juego(String idFlagg, int idJuegoTienda, String nombre, String descripcionCorta, String tienda, String imagen, String imagenMini, String urlTienda, String requisitos, String estudio, int contadorVistas) {
        this.idFlagg = idFlagg;
        this.idJuegoTienda = idJuegoTienda;
        this.nombre = nombre;
        this.descripcionCorta = descripcionCorta;
        this.tienda = tienda;
        this.imagen = imagen;
        this.imagenMini = imagenMini;
        this.urlTienda = urlTienda;
        this.requisitos = requisitos;
        this.estudio = estudio;
        this.contadorVistas = contadorVistas;
    }

    public String getIdFlagg() {
        return idFlagg;
    }

    public void setIdFlagg(String idFlagg) {
        this.idFlagg = idFlagg;
    }

    public int getIdJuegoTienda() {
        return idJuegoTienda;
    }

    public void setIdJuegoTienda(int idJuegoTienda) {
        this.idJuegoTienda = idJuegoTienda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagenMini() {
        return imagenMini;
    }

    public void setImagenMini(String imagenMini) {
        this.imagenMini = imagenMini;
    }

    public String getUrlTienda() {
        return urlTienda;
    }

    public void setUrlTienda(String urlTienda) {
        this.urlTienda = urlTienda;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    public int getContadorVistas() {
        return contadorVistas;
    }

    public void setContadorVistas(int contadorVistas) {
        this.contadorVistas = contadorVistas;
    }
}
