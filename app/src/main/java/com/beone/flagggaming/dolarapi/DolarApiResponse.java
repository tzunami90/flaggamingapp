package com.beone.flagggaming.dolarapi;

import com.google.gson.annotations.SerializedName;

public class DolarApiResponse {
    @SerializedName("moneda")
    private String moneda;

    @SerializedName("casa")
    private String casa;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("compra")
    private double compra;

    @SerializedName("venta")
    private double venta;

    @SerializedName("fechaActualizacion")
    private String fechaActualizacion;

    // Getters y setters
    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCompra() {
        return compra;
    }

    public void setCompra(double compra) {
        this.compra = compra;
    }

    public double getVenta() {
        return venta;
    }

    public void setVenta(double venta) {
        this.venta = venta;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}