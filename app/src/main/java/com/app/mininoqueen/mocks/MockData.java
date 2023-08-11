package com.app.mininoqueen.mocks;


import com.app.mininoqueen.modelos.Subcategoria;

import java.util.List;

public class MockData {


    public static final List<Subcategoria> SUBCATEGORIA_LIST = List.of(
            new Subcategoria("pielgrasa", "Piel grasa"),
            new Subcategoria("pielmixta", "Piel mixta"),
            new Subcategoria("pielseca", "Piel seca"),
            new Subcategoria("pielnormal", "Piel normal"),
            new Subcategoria("pieldelicada", "Piel delicada")
    );
}
