package com.app.mininoqueen.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.mininoqueen.modelos.Category;
import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.util.DataCard;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final FirebaseFirestore db;

    private final static String NAME_COLLECTION = "categorias";

    private final MutableLiveData<List<Category>> listMutableLiveData;

    private Context context;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        listMutableLiveData = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getCategories() {
        db.collection(NAME_COLLECTION)
                //.whereEqualTo("intermediario", DataCard.usuario.getCodigo())
                .get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                List<Category> pedidos = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    pedidos.add(document.toObject(Category.class));
                                }
                                listMutableLiveData.setValue(pedidos);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                );
    }

    public MutableLiveData<List<Category>> getListMutableLiveData() {
        return listMutableLiveData;
    }
}