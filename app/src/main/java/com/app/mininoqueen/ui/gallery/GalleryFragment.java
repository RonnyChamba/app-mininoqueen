package com.app.mininoqueen.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.adapters.AdapterCourse;
import com.app.mininoqueen.databinding.FragmentGalleryBinding;
import com.app.mininoqueen.modelos.Product;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    private RecyclerView recyclerView;

    private AdapterCourse adapterCourse;

    private List<Product> listaProductos = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setBindingWidgets();
        populateProduct();
        return root;
    }

    private void setBindingWidgets() {

        recyclerView = binding.recycleView;
    }

    private void populateProduct() {

        adapterCourse = new AdapterCourse(getContext(), listaProductos);


        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // 2 elementos por fila
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterCourse);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}