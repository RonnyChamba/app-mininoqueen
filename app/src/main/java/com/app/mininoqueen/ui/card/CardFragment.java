package com.app.mininoqueen.ui.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.adapters.AdapterCard;
import com.app.mininoqueen.adapters.AdapterProductSugerencia;
import com.app.mininoqueen.databinding.FragmentCardBinding;
import com.app.mininoqueen.databinding.FragmentPedidosBinding;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.util.DataCard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CardFragment extends Fragment implements View.OnClickListener {


    private FragmentCardBinding binding;

    private RecyclerView recyclerView;

    private Context context;

    private NavController navController;

    private Button btnComprar;

    private Button btnCancelar;

    private FirebaseFirestore db = null;

    private LinearLayout linearLayoutData;
    private LinearLayout linearLayoutDataEmpty;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CardViewModel cardViewModel =
                new ViewModelProvider(this).get(CardViewModel.class);

        binding = FragmentCardBinding.inflate(inflater, container, false);
        context = getContext();

        recyclerView = binding.recycleViewVenta;
        db = FirebaseFirestore.getInstance();
        View root = binding.getRoot();
        setBindingWithViewModel();
        configActionBar();
        backToHome();
        enableButton();
        drawerProductSuggered();
        return root;
    }

    private void enableButton() {
        if (DataCard.pedido != null && DataCard.pedido.getProducto().size() > 0) {
            btnComprar.setEnabled(true);
            btnCancelar.setEnabled(true);
        } else {
            btnComprar.setEnabled(false);
            btnCancelar.setEnabled(false);
        }
    }

    private void backToHome() {

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.nav_home);
            }
        });
    }

    private void configActionBar() {

        // Habilitar el botón de retroceso en la barra de herramientas
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            // cambiar el icono del boton de retroceso
            activity.getSupportActionBar().setHomeAsUpIndicator(
                    getResources().getDrawable(R.drawable.baseline_home_24));


        }
    }

    private void setBindingWithViewModel() {

        linearLayoutData = binding.linearLayoutData;
        linearLayoutDataEmpty = binding.linearLayoutDataEmpty;
        btnComprar = binding.btnComprar;
        btnCancelar = binding.btnCancelar;
        btnComprar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Obtener la referencia al controlador de navegacion
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void drawerProductSuggered() {

        // set the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (DataCard.pedido != null) {

            AdapterCard productAdapter = new AdapterCard(context, DataCard.pedido.getProducto());
//        adapterPlanning = new AdapterPlanning(getContext(), planifications);
            recyclerView.setAdapter(productAdapter);

            // click en el boton subir del item
            productAdapter.setOnButtonClickListener(this::deleteItem);
        } else {

            linearLayoutData.setVisibility(View.GONE);
            linearLayoutDataEmpty.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Eliminar un item del pedido actual
     *
     * @param mapItem
     */
    private void deleteItem(Map<String, Object> mapItem) {
        if (DataCard.pedido != null) {

            List<Map<String, Object>> listProduct = DataCard.pedido.getProducto();

            for (int i = 0; i < listProduct.size(); i++) {

                Map<String, Object> mapProduct = listProduct.get(i);

                if (Objects.equals(mapProduct.get("uid"), mapItem.get("uid"))) {
                    listProduct.remove(i);
                    break;
                }
            }
            DataCard.pedido.setProducto(listProduct);

            if (DataCard.pedido.getProducto().size() < 1) {
                btnComprar.setEnabled(false);
                btnCancelar.setEnabled(false);
                linearLayoutData.setVisibility(View.GONE);
                linearLayoutDataEmpty.setVisibility(View.VISIBLE);

                DataCard.pedido = null;
            }

            drawerProductSuggered();


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(context, "home" + item.getItemId(), Toast.LENGTH_SHORT).show();
//        if (item.getItemId() == R.id.h) {
//
//            Toast.makeText(context, "home", Toast.LENGTH_SHORT).show();
//            navController.navigate(R.id.nav_home);
//            return true; // Indica que la acción fue manejada
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == btnComprar.getId()) {
            savePedido();
        } else if (v.getId() == btnCancelar.getId()) {
            clearCard();
        }
    }

    private void clearCard() {

        if (DataCard.pedido != null) {
            DataCard.pedido = null;

            Toast.makeText(context, "Pedido Cancelado", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.nav_home);
        }
    }

    private void savePedido() {

        if (DataCard.pedido != null) {
            if (DataCard.pedido.getProducto().size() > 0) {
                db.collection("pedidos")
                        .document(DataCard.pedido.getUid())
                        .set(DataCard.pedido).addOnSuccessListener(documentReference -> {
                            Toast.makeText(context, "Pedido realizado", Toast.LENGTH_SHORT).show();

                            // Actualizar el stock de los productos que se vendieron en el pedido

                            updateStockProduct();

                            DataCard.pedido = null;
                            navController.navigate(R.id.nav_home);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(context, "Error al realizar el pedido", Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(context, "No hay pedido activo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No hay productos en el pedido", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Una ves se haga el pedido se debe actualizar el stock de los productos que se vendieron
     */
    private void updateStockProduct() {


        Pedido pedido = DataCard.pedido;


        for (Map<String, Object> item : pedido.getProducto()) {

            String uidProduct = item.get("uid") == null ? "" : Objects.requireNonNull(item.get("uid")).toString();

            Integer stock = item.get("stock") == null ? 0 : Integer.parseInt(Objects.requireNonNull(item.get("stock")).toString());

            // actualizar el producto
            db.collection("productos")
                    .document(uidProduct)
                    .update("stock", stock)
                    .addOnSuccessListener(documentReference -> {


                    });

        }


    }
}