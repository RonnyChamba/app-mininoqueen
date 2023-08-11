package com.app.mininoqueen.ui.pedido;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.mininoqueen.databinding.FragmentGalleryBinding;
import com.app.mininoqueen.databinding.FragmentPedidosBinding;

public class PedidoFragment extends Fragment {

    private FragmentPedidosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PedidoViewModel pedidoViewModel =
                new ViewModelProvider(this).get(PedidoViewModel.class);

        binding = FragmentPedidosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        pedidoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}