package com.app.mininoqueen.ui.detailpedido;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.adapters.AdapterDetailPedido;

import com.app.mininoqueen.databinding.FragmentDetailPedidosBinding;
import com.app.mininoqueen.modelos.Pedido;
public class DetailPedidoFragment extends Fragment implements View.OnClickListener {

    private FragmentDetailPedidosBinding binding;
    private Pedido pedido;

    private RecyclerView recyclerView;

    private AdapterDetailPedido adapterPedido;

    private Context context;

    private TextView tvTotal;

    private DetailPedidoViewModel detailPedidoViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        detailPedidoViewModel =
                new ViewModelProvider(this).get(DetailPedidoViewModel.class);

        binding = FragmentDetailPedidosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getContext();
        initBinding();
        getBundleArguments();
        return root;
    }

    private void initBinding() {
        tvTotal = binding.tvTotalPedidos;
        recyclerView = binding.recycleViewDetailPedidos;
    }

    private void getBundleArguments() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            pedido = (Pedido) bundle.getSerializable("pedido");
            if (pedido != null) populateList();
        }
    }


    @Override
    public void onClick(View v) {

    }

    private void populateList() {

        Log.d("TAG", "populateList: " + pedido.getProducto().size());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPedido = new AdapterDetailPedido(context, pedido.getProducto());
        recyclerView.setAdapter(adapterPedido);
        sumTotalAllPedidos();
    }


    private void sumTotalAllPedidos() {
        tvTotal.setText(String.valueOf(pedido.getTotal()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}