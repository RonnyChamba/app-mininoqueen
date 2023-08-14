package com.app.mininoqueen.ui.product;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.adapters.AdapterCourse;
import com.app.mininoqueen.databinding.FragmentProductoBinding;
import com.app.mininoqueen.modelos.Category;
import com.app.mininoqueen.modelos.Product;
import com.app.mininoqueen.modelos.Subcategoria;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentProductoBinding binding;
    private RecyclerView recyclerView;
    private AdapterCourse adapterModelRecicly;
    private List<Product> listaProductos = new ArrayList<>();
    private NavController navController;
    private Category category;
    private ProductViewModel productViewModel;
    private LinearLayout layoutDataEmpty;

    private Spinner spSubcategorias;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        productViewModel =
                new ViewModelProvider(this).get(ProductViewModel.class);

        binding = FragmentProductoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setBindingWidgets();
        getArgumentBundle();
        subscribeToModel();
        initValue();
        return root;
    }

    private void getArgumentBundle() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            category = (Category) bundle.getSerializable("categoria");
            if (category != null) {
                ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(category.getCategoria());
                }
            }
        }
    }

    private void setBindingWidgets() {
        recyclerView = binding.recycleView;
        layoutDataEmpty = binding.layoutProductEmpty;
        spSubcategorias = binding.spSucategorias;
        productViewModel.setContext(getContext());
    }

    private void initValue() {
        populateSubcategorias();
        productViewModel.getProducts(category, null);
    }

    private void populateSubcategorias() {


        if (category.getSubcategorias().size() > 0) {
            spSubcategorias.setVisibility(View.VISIBLE);
        } else {
            spSubcategorias.setVisibility(View.GONE);
        }

        List<Subcategoria> subcategorias = new ArrayList<>();

        subcategorias.add(new Subcategoria(null, "Todos"));
        subcategorias.addAll(category.getSubcategorias());
        ArrayAdapter<Subcategoria> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line, subcategorias);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategorias.setAdapter(adapter);
        spSubcategorias.setOnItemSelectedListener(this);
    }

    private void subscribeToModel() {
        // Observe product data
        productViewModel.getData().observe(getViewLifecycleOwner(), productos -> {
            listaProductos.clear();
            listaProductos = productos;

            if (listaProductos.size() > 0) {
                layoutDataEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
//                spSubcategorias.setVisibility(View.GONE);

                Log.i("TAG", "subscribeToModel: " + category.getSubcategorias().size());
                layoutDataEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            }

            populateProduct();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void populateProduct() {

        adapterModelRecicly = new AdapterCourse(getContext(), listaProductos);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // 2 elementos por fila
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterModelRecicly);

        // click en el boton subir del item
        adapterModelRecicly.setOnButtonClickListener(productSelected -> {

            // Aquí se ejecutará el evento cuando se presione el botón en el RecyclerView
            // Puedes acceder a los datos del objeto "planification" y realizar la acción deseada


            // Crear un Bundle para pasar el curso como argumento
            Bundle bundle = new Bundle();

            // Pasar el objeto como argumento
            bundle.putSerializable("product", productSelected);

            //int idNav = ConstantApp.isAdmin ? R.id.nav_review_planning : R.id.nav_upload_planning;
            navController.navigate(R.id.nav_review_checkout, bundle);
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Obtener la referencia al controlador de navegacion
        navController = Navigation.findNavController(view);
    }

    /**
     * Callback method to be invoked when an item in this view has been selected.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Subcategoria subCategoriaItem = (Subcategoria) parent.getItemAtPosition(position);

        // Se agrego un item subcategoria vacio para que se muestre todos los productos, donde el uid
        // es null, por eso se valida si es null para mostrar todos los productos
        productViewModel.getProducts(category, subCategoriaItem.getUid());


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}