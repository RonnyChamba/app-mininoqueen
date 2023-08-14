package com.app.mininoqueen.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.adapters.AdapterCategory;
import com.app.mininoqueen.adapters.AdapterCourse;
import com.app.mininoqueen.databinding.FragmentHomeBinding;
import com.app.mininoqueen.modelos.Category;
import com.app.mininoqueen.modelos.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private AdapterCategory adapterCategory;
    private List<Category> listCategory = new ArrayList<>();
    private HomeViewModel homeViewModel;
    private Context context;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setBindingWidgets();
        subscribeToModel();
        initValues();

        return root;
    }

    private void setBindingWidgets() {
        recyclerView = binding.recycleView;
        context = getContext();
    }

    private void initValues() {
        homeViewModel.getCategories();
    }

    private void subscribeToModel() {
        // Observe product data
        homeViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), listData -> {
            listCategory.clear();
            listCategory = listData;

            populateRecycler();
        });
    }

    private void populateRecycler() {

        adapterCategory = new AdapterCategory(getContext(), listCategory);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // 2 elementos por fila
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterCategory);
        adapterCategory.setOnButtonClickListener(this::selectCategory);
    }

    private void selectCategory(Category category) {
        Log.d("TAG", "selectCategory: " + category.getUid());
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoria", category);
        navController.navigate(R.id.nav_product, bundle);
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
}