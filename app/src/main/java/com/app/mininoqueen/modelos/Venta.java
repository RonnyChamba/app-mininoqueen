package com.app.mininoqueen.modelos;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Venta {
    private String codigo;

    private String codigoTransaccion;

    private boolean estado;
    private Timestamp fecha;

    private Timestamp fechaVenta;

    private Map<String, Object> idCliente;
    private Map<String, Object> idVendedor;
    private Double impuesto;
    private String metodoPago;
    private Double neto;
    private List<Map<String, Object>> producto = new ArrayList<>();
    // define un campo de tipo timestamp
    private Double total;
    private String uid;

    private Double valorImpuesto;

    public Venta() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Map<String, Object> getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Map<String, Object> idCliente) {
        this.idCliente = idCliente;
    }

    public Map<String, Object> getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Map<String, Object> idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Double getNeto() {
        return neto;
    }

    public void setNeto(Double neto) {
        this.neto = neto;
    }

    public List<Map<String, Object>> getProducto() {
        return producto;
    }

    public void setProducto(List<Map<String, Object>> producto) {
        this.producto = producto;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getValorImpuesto() {
        return valorImpuesto;
    }

    public void setValorImpuesto(Double valorImpuesto) {
        this.valorImpuesto = valorImpuesto;
    }
}
