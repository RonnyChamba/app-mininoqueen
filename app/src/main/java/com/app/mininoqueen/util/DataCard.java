package com.app.mininoqueen.util;

import com.app.mininoqueen.modelos.Cliente;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.modelos.Product;
import com.app.mininoqueen.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DataCard {

    private final static List<Product> listAddToCard = new ArrayList<>();

    public static Cliente cliente = new Cliente();

    public static Usuario usuario = new Usuario();

    public static Pedido pedido = null;

    public static void addProduct(Product product) {

        listAddToCard.add(product);
    }

    public static List<Product> getListAddToCard() {

        return listAddToCard;
    }


}
