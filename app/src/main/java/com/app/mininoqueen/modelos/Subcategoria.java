package com.app.mininoqueen.modelos;

public class Subcategoria {

    private String uid;
    private String nombre;

    public Subcategoria(String uid, String nombre) {
        this.uid = uid;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
