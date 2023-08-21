package com.app.mininoqueen.ui.cuidado;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.adapters.AdapterCompras;
import com.app.mininoqueen.adapters.AdapterPedido;
import com.app.mininoqueen.databinding.FragmentCuidadoPersonalBinding;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.modelos.Venta;

import java.util.ArrayList;
import java.util.List;

public class CuidadoPersonalFragment extends Fragment {

    private FragmentCuidadoPersonalBinding binding;

    private RecyclerView recyclerView;

    private AdapterCompras adapterCompras;

    private List<Venta> listaVentas = new ArrayList<>();

    private CuidadoPersonalViewModel cuidadoPersonalViewModel;

    private TextView tvTotal;

    private Context context;

    private LinearLayout layoutPedidosData;
    private LinearLayout layoutPedidosDataEmpty;

    private Button btnComprarAhora;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cuidadoPersonalViewModel =
                new ViewModelProvider(this).get(CuidadoPersonalViewModel.class);

        binding = FragmentCuidadoPersonalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setBindingWidgets();
        initValues();
        subscribeToModel();


        return root;
    }

    private void setBindingWidgets() {

        recyclerView = binding.recycleView;
        tvTotal = binding.tvTotalCompras;
        btnComprarAhora = binding.btnComprarAhora;
        layoutPedidosData = binding.layoutComprasData;
        layoutPedidosDataEmpty = binding.layoutComprasDataEmpty;
        context = getContext();

        btnComprarAhora.setOnClickListener(v -> {

            navController.navigate(R.id.nav_home);

        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Obtener la referencia al controlador de navegacion
        navController = Navigation.findNavController(view);
    }

    private void initValues() {
        cuidadoPersonalViewModel.getVentas(null);


    }

    private void subscribeToModel() {
        // Observe product data
        cuidadoPersonalViewModel.getDataList().observe(getViewLifecycleOwner(), listData -> {
            listaVentas.clear();
            listaVentas = listData;

            if (listaVentas.size() > 0) {
                layoutPedidosData.setVisibility(View.VISIBLE);
                layoutPedidosDataEmpty.setVisibility(View.GONE);
            } else {

                layoutPedidosDataEmpty.setVisibility(View.VISIBLE);
                layoutPedidosData.setVisibility(View.GONE);

            }

            populateProduct();
        });
    }

    private void populateProduct() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterCompras = new AdapterCompras(context, listaVentas);
        recyclerView.setAdapter(adapterCompras);

        // click en el boton delete del item
        adapterCompras.setOnButtonClickListener(this::viewDetails);
        sumTotalAllPedidos();

    }

    private void viewDetails(Venta venta) {

        Toast.makeText(context, "Ver detalles de la compras", Toast.LENGTH_SHORT).show();
    }

    private void sumTotalAllPedidos() {

        if (listaVentas != null) {

            double totalAllPedidos = listaVentas
                    .stream()
                    .map(Venta::getTotal)
                    .reduce(0.0, Double::sum);

            tvTotal.setText(String.valueOf(totalAllPedidos));
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}