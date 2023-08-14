package com.app.mininoqueen.ui.pedido;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.util.DataCard;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PedidoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<Pedido>> listMutableLiveData;

    private final FirebaseFirestore db;

    private final static String NAME_COLLECTION = "pedidos";

    private Context context;

    public PedidoViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is pedido fragment");
        listMutableLiveData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LiveData<String> getText() {
        return mText;
    }


    public void getPedidos(Date date) {


        if (date == null) {
            db.collection(NAME_COLLECTION)
                    // obtiene los pedidos del usuario logueado
                    .whereEqualTo("idCliente.uid", DataCard.cliente.getUid())
                    .whereEqualTo("estado", false)
                    .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<Pedido> pedidos = new ArrayList<>();
                                    for (DocumentSnapshot document : task.getResult()) {
                                        pedidos.add(document.toObject(Pedido.class));
                                    }
                                    listMutableLiveData.setValue(pedidos);
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                    );
        } else {


            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(date);
            calendarStart.set(Calendar.HOUR_OF_DAY, 0);
            calendarStart.set(Calendar.MINUTE, 0);


            Timestamp timestampStart = new Timestamp(calendarStart.getTime());

            Calendar calendarFinal = Calendar.getInstance();
            calendarFinal.setTime(date);
            calendarFinal.add(Calendar.DAY_OF_MONTH, 1);
            calendarFinal.set(Calendar.HOUR_OF_DAY, 0);
            calendarFinal.set(Calendar.MINUTE, 0);
            Timestamp timestampEnd = new Timestamp(calendarFinal.getTime());


            db.collection(NAME_COLLECTION)
                    .whereEqualTo("idCliente.uid", DataCard.cliente.getUid())
                    .whereEqualTo("estado", false)
                    .whereGreaterThanOrEqualTo("fecha", timestampStart)
                    //.whereLessThan("fecha", timestampEnd)
                    .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<Pedido> pedidos = new ArrayList<>();
                                    for (DocumentSnapshot document : task.getResult()) {
                                        pedidos.add(document.toObject(Pedido.class));
                                        Log.d("TAG", "Filtro fecha: " + document.getId() + " => " + document.getData());
                                    }

                                    listMutableLiveData.setValue(pedidos);
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                    );
        }


    }

    public void deletePedido(Pedido pedido) {

        /**
         * Steep:
         *  1). Delete pedido
         *  2) Update stock of products
         */

        db.collection(NAME_COLLECTION)
                .document(pedido.getUid())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");

                        Toast.makeText(context, "Pedido eliminado", Toast.LENGTH_SHORT).show();
                        updateStockProducts(pedido.getProducto());
                        getPedidos(null);

                    } else {
                        Log.w("TAG", "Error deleting document", task.getException());
                        Toast.makeText(context, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateStockProducts(List<Map<String, Object>> productsByPedido) {

        Log.i("TAG", "updateStockProducts: " + productsByPedido.size());
        for (Map<String, Object> product : productsByPedido) {

            int newStock = Integer.parseInt(product.get("stock").toString()) + Integer.parseInt(product.get("cantidad").toString());
            db.collection("productos")
                    .document(product.get("uid").toString())
                    .update("stock", newStock)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "DocumentSnapshot successfully updated!");
                        } else {
                            Log.w("TAG", "Error updating document", task.getException());

                            Toast.makeText(context, "No se pudo actualizar stock", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public MutableLiveData<List<Pedido>> getListMutableLiveData() {
        return listMutableLiveData;
    }


}