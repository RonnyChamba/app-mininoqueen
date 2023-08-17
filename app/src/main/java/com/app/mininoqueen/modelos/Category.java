package com.app.mininoqueen.modelos;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private String categoria;

    //private String fecha;
    private Timestamp fecha;

    private String uid;

    private List<Subcategoria> subcategorias = new ArrayList<>();
    private List<Map<String, Object>> productos = new ArrayList<>();


    public Category() {
    }

    public Category(String categoria, Timestamp fecha, String uid, List<Map<String, Object>> productos) {
        this.categoria = categoria;
        this.fecha = fecha;
        this.uid = uid;
        this.productos = productos;
    }

    public List<Subcategoria> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<Subcategoria> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Map<String, Object>> getProductos() {
        return productos;
    }

    public void setProductos(List<Map<String, Object>> productos) {
        this.productos = productos;
    }


    @Override
    public String toString() {
        return categoria;
    }
}
