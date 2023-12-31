package com.app.mininoqueen.modelos;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;

    private String codigo;

    private boolean estado;

    private Map<String, Object> idCliente;
    private Map<String, Object> idVendedor;
    private Double impuesto;

    private String metodoPago;
    private Double neto;
    private Double total;
    private List<Map<String, Object>> producto = new ArrayList<>();
    // define un campo de tipo timestamp
    private Timestamp fecha;

    public Pedido() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<Map<String, Object>> getProducto() {
        return producto;
    }

    public void setProducto(List<Map<String, Object>> producto) {
        this.producto = producto;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "uid='" + uid + '\'' +
                ", codigo='" + codigo + '\'' +
                ", estado=" + estado +
                ", idCliente=" + idCliente +
                ", idVendedor=" + idVendedor +
                ", impuesto=" + impuesto +
                ", metodoPago='" + metodoPago + '\'' +
                ", neto=" + neto +
                ", total=" + total +
                ", producto=" + producto +
                ", fecha=" + fecha +
                '}';
    }
}
