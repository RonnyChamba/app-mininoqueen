package com.app.mininoqueen.modelos;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSugerido extends Product implements Serializable {
    private Map<String, Object> sugerencias;

    public ProductSugerido() {
    }

    public Map<String, Object> getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(Map<String, Object> sugerencias) {
        this.sugerencias = sugerencias;
    }
}
