package com.beone.flagggaming.producto;

import java.math.BigDecimal;

public class Producto {
    private int idInternoProducto;
    private int idTienda;
    private int idCategoria;
    private String skuTienda;
    private String descTienda;
    private String marca;
    private BigDecimal precioVta;
    private boolean estatus;
    private Categoria categoria;

    public Producto(int idInternoProducto, int idTienda, int idCategoria, String skuTienda, String descTienda, String marca, BigDecimal precioVta, boolean estatus) {
        this.idInternoProducto = idInternoProducto;
        this.idTienda = idTienda;
        this.idCategoria = idCategoria;
        this.skuTienda = skuTienda;
        this.descTienda = descTienda;
        this.marca = marca;
        this.precioVta = precioVta;
        this.estatus = estatus;
    }

    public int getIdInternoProducto() {
        return idInternoProducto;
    }

    public void setIdInternoProducto(int idInternoProducto) {
        this.idInternoProducto = idInternoProducto;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getSkuTienda() {
        return skuTienda;
    }

    public void setSkuTienda(String skuTienda) {
        this.skuTienda = skuTienda;
    }

    public String getDescTienda() {
        return descTienda;
    }

    public void setDescTienda(String descTienda) {
        this.descTienda = descTienda;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public BigDecimal getPrecioVta() {
        return precioVta;
    }

    public void setPrecioVta(BigDecimal precioVta) {
        this.precioVta = precioVta;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public Categoria getCategoria() { return categoria; }

    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}