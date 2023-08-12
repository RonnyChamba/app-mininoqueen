package com.app.mininoqueen.ui.ventas;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mininoqueen.R;
import com.app.mininoqueen.adapters.AdapterProductSugerencia;
import com.app.mininoqueen.databinding.FragmentVentaBinding;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.modelos.Product;
import com.app.mininoqueen.util.DataCard;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class VentaFragment extends Fragment implements View.OnClickListener {


    private FragmentVentaBinding binding;

    private ImageView imageVentaPro;
    private TextView txtVentaTitle;
    private TextView txtVentaPro;
    private TextView txtVentaStock;
    private EditText txtVentaCantidad;
    private TextView txtVentaTotal;
    private Product product;

    private Button btnVentaAdd;

    private RecyclerView recyclerView;

    private FirebaseFirestore db = null;

    private List<Product> productListSugeridos = new ArrayList<>();

    private Context context;

    private VentaViewModel viewModel;


    public VentaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(VentaViewModel.class);

        binding = FragmentVentaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.recycleViewVenta;
        context = container.getContext();
        db = FirebaseFirestore.getInstance();

        setBindingWidgets();
        setValueFragments();
        eventkey();
        listProductSugeridos();

        viewModel.getData().observe(getViewLifecycleOwner(), listProducts -> {

            drawerProductSuggered();

        });

        setStockInitial();

        return root;
    }


    private void eventkey() {
        txtVentaCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método se llama antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método se llama mientras el texto está cambiando
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Este método se llama después de que el texto ha cambiado
                String newText = editable.toString();

                if (!newText.isBlank()) {
                    Integer cantidad = Integer.parseInt(newText);
                    Double total = cantidad * product.getPrecioVenta();
                    txtVentaTotal.setText("" + total);
                } else {
                    txtVentaTotal.setText("0.0");
                }
            }
        });

    }

    private void setBindingWidgets() {
        imageVentaPro = binding.imageVentaPro;
        txtVentaTitle = binding.txtVentaTitle;
        txtVentaPro = binding.txtVentaPrecio;
        txtVentaStock = binding.txtVentaStock;
        txtVentaCantidad = binding.txtVentCantidad;
        txtVentaTotal = binding.txtVentaTotal;
        btnVentaAdd = binding.btnVentaAdd;

        btnVentaAdd.setOnClickListener(this);
    }

    private void setValueFragments() {

        // Get the Bundle of arguments
        Bundle arguments = getArguments();

        if (arguments != null && arguments.containsKey("product")) {

            product = (Product) arguments.getSerializable("product");


            Glide.with(this)
                    .load(product.getImagen())
                    .into(imageVentaPro);

            txtVentaTitle.setText(product.getDescripcion());
            txtVentaPro.setText("" + product.getPrecioVenta());
            txtVentaStock.setText("" + product.getStock());
        } else {
            Toast.makeText(getContext(), "No se pudo obtener el producto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnVentaAdd.getId()) {

            String cantidadInput = txtVentaCantidad.getText().toString();
            boolean resp = addProductToCart(v, product, cantidadInput);
            if (resp) {
                txtVentaCantidad.setText("0");
                txtVentaTotal.setText("0.0");

                int stock = product.getStock() - Integer.parseInt(cantidadInput);
                product.setStock(stock);
                txtVentaStock.setText("" + stock);
            }
        }
    }

    private boolean addProductToCart(View view, Product product, String cantidadInput) {

        if (cantidadInput.isBlank()) {
            Snackbar.make(view, "Ingrese una cantidad", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return false;
        }
        Integer amount = Integer.parseInt(cantidadInput);

        if (amount > product.getStock()) {
            Snackbar.make(view, "No hay suficiente stock", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return false;
        }

        Map<String, Object> detailItem = createDetails(product, amount);


        if (DataCard.pedido != null) {
            // agregar primero producto a la venta o el producto es nuevo
            if (DataCard.pedido.getProducto().size() < 1 || !alreadyExists(product.getUid())) {
                DataCard.pedido.getProducto().add(detailItem);
                Snackbar.make(view, "Producto agregado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                // verificar si el producto ya existe en la venta
                for (Map<String, Object> item : DataCard.pedido.getProducto()) {

                    // Producto ya existe en la venta
                    if (Objects.equals(item.get("uid"), product.getUid())) {
                        // actualizar cantidad y total

                        // cantidad actual del producto
                        Integer cantidad = Integer.parseInt(item.get("cantidad").toString());
                        Integer stock = Integer.parseInt(item.get("stock").toString());
                        item.put("cantidad", cantidad + amount);
                        item.put("total", (cantidad + amount) * product.getPrecioVenta());
//                        item.put("stock", product.getStock() - (cantidad + amount));
                        item.put("stock", stock - amount);

                        Snackbar.make(view, "Producto agregado", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }

            }


        } else {

            // crear venta
            DataCard.pedido = new Pedido();
            String uid = UUID.randomUUID().toString();
            DataCard.pedido.setFecha(Timestamp.now());
            DataCard.pedido.setCodigo(uid);
            DataCard.pedido.setUid(uid);
            DataCard.pedido.setIdCliente(Map.of("uid", DataCard.cliente.getUid(),
                    "nombre", DataCard.cliente.getNombre()));
            DataCard.pedido.setIdVendedor(Map.of("uid", DataCard.usuario.getUid(),
                    "nombre", DataCard.usuario.getNombre()));

            DataCard.pedido.getProducto().add(detailItem);
            Snackbar.make(view, "Producto agregado  al carrito", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

//        Snackbar.make(view, "Productos " + DataCard.pedido.getProducto().size(), Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();

        // ir calculando el total de la suma de los productos

        double total = 0.0;
        for (Map<String, Object> item : DataCard.pedido.getProducto()) {
            total += Double.parseDouble(Objects.requireNonNull(item.get("total")).toString());
        }
        DataCard.pedido.setTotal(total);

        Log.i("TAG", "addProductToCart: " + DataCard.pedido);

        return true;
    }

    /**
     * Load the suggested products
     */
    private void listProductSugeridos() {

        productListSugeridos.clear();
        int amountTask = product.getProductosSugeridos().size();
        AtomicInteger count = new AtomicInteger();

        if (amountTask > 0) {
            for (String uid : product.getProductosSugeridos()) {
                Log.i("TAG", "listProductSugeridos UID: " + uid);
                db.collection("productos")
                        .document(uid)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Product product = task.getResult().toObject(Product.class);
                                if (product != null) {
                                    product.setUid(task.getResult().getId());
                                    Log.i("TAG", "listProductSugeridos: " + product.getDescripcion());
                                    productListSugeridos.add(product);
                                } else {
                                    Log.i("TAG", "listProductSugeridos: " + "es nulo");
                                }
                            }
                            count.set(count.get() + 1);

                            if (count.get() == amountTask) {
                                viewModel.getData().postValue(productListSugeridos);
                            }
                            // Marcar una consulta como completada
//                            latch.countDown();
                        })
                        .addOnFailureListener(e -> {
                                    Log.i("TAG", "listProductSugeridos error: " + e.getMessage());
                                    count.set(count.get() + 1);
                                }
                        );
            }

        } else Log.i("TAG", "listProductSugeridos: " + "no hay sugeridos");


    }

    private void drawerProductSuggered() {

        // set the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterProductSugerencia productAdapter = new AdapterProductSugerencia(context, productListSugeridos);
//        adapterPlanning = new AdapterPlanning(getContext(), planifications);
        recyclerView.setAdapter(productAdapter);

        // click en el boton subir del item
        productAdapter.setOnButtonClickListener((productSelected, map, view) -> {


            addProductToCart(view, productSelected, Objects.requireNonNull(map.get("cantidad")).toString());

//            productSelected.setStock(0);

        });
    }

    private Map<String, Object> createDetails(Product product, Integer amount) {

        Map<String, Object> detailItem = new HashMap<>();
        detailItem.put("cantidad", amount);
        detailItem.put("descripcion", product.getDescripcion());
        detailItem.put("precio", product.getPrecioVenta());
        detailItem.put("total", amount * product.getPrecioVenta());
        detailItem.put("stock", product.getStock() - amount);
        detailItem.put("uid", product.getUid());
        detailItem.put("imagen", product.getImagen());

        return detailItem;
    }

    private boolean alreadyExists(String uid) {
        for (Map<String, Object> item : DataCard.pedido.getProducto()) {
            if (Objects.equals(item.get("uid"), uid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permite mostrar el stock inicial del producto, se verifica si ya esta el producto en el carrito
     * y se revisa el stock del producto
     */
    private void setStockInitial() {


        // verificar si el producto principal(seleccionado) y los sugeridos ya estan  en el carrito actual

        /**
         * 1) Verificar si ya existe creado un pedido
         *      * Si existe, verificar si el producto ya esta en el carrito
         */

        if (DataCard.pedido != null) {

            // verificar si el producto principal ya esta en el carrito
            if (alreadyExists(product.getUid())) {

                for (Map<String, Object> item : DataCard.pedido.getProducto()) {
                    if (Objects.equals(item.get("uid"), product.getUid())) {
                        txtVentaStock.setText(Objects.requireNonNull(item.get("stock")).toString());

                        // actualizar el stock del producto
                        product.setStock(Integer.parseInt(Objects.requireNonNull(item.get("stock")).toString()));
                        Log.i("TAG", "setStockInitial: " + product.getStock());
                    }
                }
            }  // no Hacer nada si no esta en el carrito

            verifyProductSugeridos();

        }

    }

    private void verifyProductSugeridos() {

        // verificar si los productos sugeridos ya estan en el carrito
        for (Product product : productListSugeridos) {
            if (alreadyExists(product.getUid())) {
                for (Map<String, Object> item : DataCard.pedido.getProducto()) {
                    if (Objects.equals(item.get("uid"), product.getUid())) {
                        product.setStock(Integer.parseInt(Objects.requireNonNull(item.get("stock")).toString()));
                        Log.i("TAG", "setStockInitial: " + product.getStock());
                    }
                }
            }
        }
    }
}
