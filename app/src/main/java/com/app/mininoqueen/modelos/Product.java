package com.app.mininoqueen.modelos;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;
    private Map<String, Object> categoria;
    private String codigo;
    private String descripcion;
    private String imagen;
    private Double precioCompra;
    private Double precioVenta;
    private int stock;
//    private Date fecha;

    private Timestamp fecha;
    private int ventas;

    private List<String> productosSugeridos = new ArrayList<>();

    // Cada producto pertenece a un intermediario
    private String intermedio;

    public Product() {
    }

    public Product(String uid, Map<String, Object> categoria, String codigo,
                   String descripcion, String imagen, Double
                           precioCompra, Double precioVenta, int stock, Timestamp fecha, int ventas) {
        this.uid = uid;
        this.categoria = categoria;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.fecha = fecha;
        this.ventas = ventas;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> getCategoria() {
        return categoria;
    }

    public void setCategoria(Map<String, Object> categoria) {
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public int getVentas() {
        return ventas;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }

    public List<String> getProductosSugeridos() {
        return productosSugeridos;
    }

    public void setProductosSugeridos(List<String> productosSugeridos) {
        this.productosSugeridos = productosSugeridos;
    }

    public String getIntermedio() {
        return intermedio;
    }

    public void setIntermedio(String intermedio) {
        this.intermedio = intermedio;
    }
}
