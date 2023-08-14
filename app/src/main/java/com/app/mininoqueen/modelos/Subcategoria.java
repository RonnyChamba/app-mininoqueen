package com.app.mininoqueen.modelos;

public class Subcategoria {

    private String uid;
    private String nombre;

    public Subcategoria() {
    }

    public Subcategoria(String uid, String nombre) {
        this.uid = uid;
        this.nombre = nombre;
    }

    @Override
    public String toString() {

        return nombre;
    }

    public String getUid() {
        return uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
