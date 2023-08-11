package com.app.mininoqueen.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.adapters.AdapterCourse;
import com.app.mininoqueen.databinding.FragmentHomeBinding;
import com.app.mininoqueen.mocks.MockData;
import com.app.mininoqueen.modelos.Product;
import com.app.mininoqueen.modelos.Subcategoria;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private Spinner spCourses;

    private RecyclerView recyclerView;

    private AdapterCourse adapterCourse;

    private List<Product> listaProductos = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setBindingWidgets();
        populateData();
        populateProduct();

        return root;
    }

    private void setBindingWidgets() {
        spCourses = binding.spCourses;
        recyclerView = binding.recycleView;
    }

    private void populateData() {
        ArrayAdapter<Subcategoria> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line, MockData.SUBCATEGORIA_LIST);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourses.setAdapter(adapter);
    }

    private void populateProduct() {

        adapterCourse = new AdapterCourse(getContext(), listaProductos);


        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
        listaProductos.add(new Product());
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