package com.app.mininoqueen.ui.slideshow;

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
import com.app.mininoqueen.adapters.AdapterCourse;
import com.app.mininoqueen.databinding.FragmentSlideshowBinding;
import com.app.mininoqueen.modelos.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerView;
    private AdapterCourse adapterCourse;
    private List<Product> listaProductos = new ArrayList<>();
    private FirebaseFirestore db = null;

    private NavController navController;
    public static final String COLLECTION_NAME = "productos";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();

        setBindingWidgets();
        loadingProducts();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Obtener la referencia al controlador de navegacion
        navController = Navigation.findNavController(view);
    }

    private void setBindingWidgets() {

        recyclerView = binding.recycleView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadingProducts() {


        db.collection(COLLECTION_NAME)
                .whereEqualTo("categoria.uid", "maquillajeojos")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            listaProductos.clear();

                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {

                                Product planning = documentSnapshot.toObject(Product.class);

                                if (planning != null) {

                                    // Toast.makeText(getContext(), "timestamp: " + planning.getTimestampDate() + planifications.size(), Toast.LENGTH_SHORT).show();
                                    planning.setUid(documentSnapshot.getId());
                                    // Log.i("Planning", planning.getTitle());
                                    listaProductos.add(planning);

                                } else Log.i("Producto item", "ES NULL");
                            }

                            populateProduct();


                        } else {

                            // Toast.makeText(getContext(), "No hay planificaciones para la semana seleccionado", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Maneja el error
                        Exception exception = task.getException();
                        if (exception != null) {
                            Log.e("Firestore", "Error al obtener documentos planificaciones: " + exception.getMessage());

                            Toast.makeText(getContext(), "Error al cargar las planificaciones", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void populateProduct() {

        adapterCourse = new AdapterCourse(getContext(), listaProductos);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2); // 2 elementos por fila
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterCourse);


        // click en el boton subir del item
        adapterCourse.setOnButtonClickListener(productSelected -> {

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

}