package com.app.mininoqueen.ui.pedido;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.adapters.AdapterPedido;
import com.app.mininoqueen.databinding.FragmentPedidosBinding;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.ui.components.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PedidoFragment extends Fragment implements View.OnClickListener {

    private FragmentPedidosBinding binding;

    private EditText etPlannedDate;

    private List<Pedido> listaPedidos = new ArrayList<>();

    private RecyclerView recyclerView;

    private AdapterPedido adapterPedido;

    private Context context;

    private TextView tvTotal;

    private PedidoViewModel pedidoViewModel;

    private LinearLayout layoutPedidosData;
    private LinearLayout layoutPedidosDataEmpty;

    private ImageButton iconButtonUpdate;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pedidoViewModel =
                new ViewModelProvider(this).get(PedidoViewModel.class);

        binding = FragmentPedidosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getContext();
        pedidoViewModel.setContext(context);
        initBinding();
        initValues();
        subscribeToModel();
        backToHome();
        return root;
    }

    private void initBinding() {
        tvTotal = binding.tvTotalPedidos;
        recyclerView = binding.recycleViewPedidos;
        etPlannedDate = binding.etPlannedDate;
        layoutPedidosData = binding.layoutPedidosData;
        layoutPedidosDataEmpty = binding.layoutPedidosDataEmpty;
        iconButtonUpdate = binding.iconButtonUpdate;
        etPlannedDate.setOnClickListener(this);
        iconButtonUpdate.setOnClickListener(this);
    }

    private void initValues() {
        pedidoViewModel.getPedidos(null);
    }

    private void subscribeToModel() {
        // Observe product data
        pedidoViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), pedidos -> {
            listaPedidos.clear();
            listaPedidos = pedidos;

            if (listaPedidos.size() > 0) {
                layoutPedidosData.setVisibility(View.VISIBLE);
                layoutPedidosDataEmpty.setVisibility(View.GONE);
            } else {
                layoutPedidosData.setVisibility(View.GONE);
                layoutPedidosDataEmpty.setVisibility(View.VISIBLE);
            }


            populateList();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Obtener la referencia al controlador de navegacion
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == etPlannedDate.getId()) {
            showDatePickerDialog();
        }

        if (v.getId() == iconButtonUpdate.getId()) {
            pedidoViewModel.getPedidos(null);
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            // +1 because January is zero
            final String selectedDate = day + " / " + (month + 1) + " / " + year;
            etPlannedDate.setText(selectedDate);

            Toast.makeText(context, "Fecha", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            pedidoViewModel.getPedidos(calendar.getTime());

        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void populateList() {

        Log.d("TAG", "populateList: " + listaPedidos.size());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPedido = new AdapterPedido(context, listaPedidos);
        recyclerView.setAdapter(adapterPedido);
        // click en el boton delete del item
        adapterPedido.setOnButtonClickListener(this::deletePedido);

        // click en el item
        adapterPedido.setOnClickListener(this::clickItemChild);
        sumTotalAllPedidos();

    }

    private void clickItemChild(View view) {

        Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();

        Pedido pedido = listaPedidos.get(recyclerView.getChildAdapterPosition(view));

        Bundle bundle = new Bundle();
        bundle.putSerializable("pedido", pedido);

        navController.navigate(R.id.action_pedido_to_details, bundle);


    }

    private void sumTotalAllPedidos() {

        if (listaPedidos != null) {

            double totalAllPedidos = listaPedidos
                    .stream()
                    .map(Pedido::getTotal)
                    .reduce(0.0, Double::sum);
            tvTotal.setText(String.valueOf(totalAllPedidos));
        }
    }

    private void deletePedido(Pedido pedido) {


        // Crear un cuadro de diálogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que deseas eliminar?");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            pedidoViewModel.deletePedido(pedido);

        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {

            //Toast.makeText(context, "Pedido cancelado", Toast.LENGTH_SHORT).show();
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void backToHome() {

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                // Not calling popBackStack() here, as it may have side effects
            }
        });
    }


}