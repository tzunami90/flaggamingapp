package com.beone.flagggaming.juegos;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Juego {

    private String idFlagg;
    private String idJuegoTienda;
    private String nombre;
    private String descripcionCorta;
    private String tienda;
    private String imagen;
    private String imagenMini;
    private String urlTienda;
    private String requisitos;
    private String estudio;
    private int contadorVistas;
    private String precioSteam;
    private String precioPostaSteam;
    private String precioPareSteam;

    public Juego(String idFlagg, String idJuegoTienda, String nombre, String descripcionCorta, String tienda, String imagen, String imagenMini, String urlTienda, String requisitos, String estudio, int contadorVistas) {
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

    public String getIdJuegoTienda() {
        return idJuegoTienda;
    }

    public void setIdJuegoTienda(String idJuegoTienda) {
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
        return stripHtml(requisitos);
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

    public String getPrecioSteam() {
        return precioSteam;
    }

    public void setPrecioSteam(String precioSteam) {
        this.precioSteam = precioSteam;
    }

    public String getPrecioPostaSteam() {
        return precioPostaSteam;
    }

    public void setPrecioPostaSteam(String precioPostaSteam) {
        this.precioPostaSteam = precioPostaSteam;
    }

    public String getPrecioPareSteam() {
        return precioPareSteam;
    }

    public void setPrecioPareSteam(String precioPareSteam) {
        this.precioPareSteam = precioPareSteam;
    }

    // Método para eliminar etiquetas HTML de un texto y reemplazar <br> con saltos de línea
    private String stripHtml(String html) {
        // Verificar si el HTML es nulo o vacío
        if (html == null || html.isEmpty()) {
            return "";
        }

        // Utilizar JSoup para parsear el HTML y extraer el texto sin etiquetas HTML
        Document doc = Jsoup.parse(html);
        String text = doc.text();

        // Reemplazar <br> con saltos de línea reales
        text = text.replaceAll("\\\\n", "\n");
        text = text.replaceAll("(?i)<br[^>]*>", "\n"); // Expresión regular para reemplazar <br> con \n

        return text;
    }
}
