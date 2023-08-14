package com.app.mininoqueen.ui.product;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.mininoqueen.modelos.Category;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.modelos.Product;
import com.app.mininoqueen.util.DataCard;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final MutableLiveData<List<Product>> data;

    private final FirebaseFirestore db;

    private final static String NAME_COLLECTION = "productos";

    private Context context;


    public ProductViewModel() {
        mText = new MutableLiveData<>();
        data = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        mText.setValue("This is slideshow fragment");
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getProducts(Category category, String subCategoria) {

        Log.d("TAG", "getProducts param: " + category.getUid());
        Log.d("TAG", "getProducts subcategoria param: " + subCategoria);

        if (subCategoria != null && !subCategoria.isEmpty() && !subCategoria.equals("Todos")) {
            db.collection(NAME_COLLECTION)
                    .whereEqualTo("categoria.uid", category.getUid())
                    .whereEqualTo("categoria.subcategoria", subCategoria)
                    .whereEqualTo("intermediario", DataCard.usuario.getCodigo())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Product> productosList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                productosList.add(document.toObject(Product.class));
                            }

                            Log.d("TAG", "onComplete Fragment Product: " + productosList.size());
                            data.setValue(productosList);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                    });
        } else {
            db.collection(NAME_COLLECTION)
                    .whereEqualTo("categoria.uid", category.getUid())
                    .whereEqualTo("intermediario", DataCard.usuario.getCodigo())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Product> productosList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                productosList.add(document.toObject(Product.class));
                            }

                            Log.d("TAG", "onComplete Fragment Product: " + productosList.size());
                            data.setValue(productosList);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                    });
        }

    }


    public MutableLiveData<List<Product>> getData() {
        return data;
    }
}