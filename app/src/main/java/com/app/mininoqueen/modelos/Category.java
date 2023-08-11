package com.app.mininoqueen.modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Category {

    private String category;

    private String fecha;

    private String uid;

    private List<Map<String, Object>> productos = new ArrayList<>();

    public Category() {
    }

    public Category(String category, String fecha, String uid, List<Map<String, Object>> productos) {
        this.category = category;
        this.fecha = fecha;
        this.uid = uid;
        this.productos = productos;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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
        return category;
    }
}
