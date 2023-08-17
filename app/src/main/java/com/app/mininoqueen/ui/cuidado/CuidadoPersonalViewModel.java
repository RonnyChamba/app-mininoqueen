package com.app.mininoqueen.ui.cuidado;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.mininoqueen.modelos.Pedido;
import com.app.mininoqueen.modelos.Venta;
import com.app.mininoqueen.util.DataCard;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CuidadoPersonalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final MutableLiveData<List<Venta>> dataList;

    private final FirebaseFirestore db;

    private final static String NAME_COLLECTION = "ventas";

    private Context context;

    public CuidadoPersonalViewModel() {
        mText = new MutableLiveData<>();
        dataList = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        mText.setValue("This is gallery fragment");
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getVentas(Date date) {

        if (date == null) {
            db.collection(NAME_COLLECTION)
                    // obtiene los pedidos del usuario logueado
                    .whereEqualTo("idCliente.uid", DataCard.cliente.getUid())
                    .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<Venta> pedidos = new ArrayList<>();
                                    for (DocumentSnapshot document : task.getResult()) {
                                        pedidos.add(document.toObject(Venta.class));
                                    }
                                    dataList.setValue(pedidos);
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
                    .whereGreaterThanOrEqualTo("fecha", timestampStart)
                    //.whereLessThan("fecha", timestampEnd)
                    .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<Venta> pedidos = new ArrayList<>();
                                    for (DocumentSnapshot document : task.getResult()) {
                                        pedidos.add(document.toObject(Venta.class));
                                        Log.d("TAG", "Filtro fecha: " + document.getId() + " => " + document.getData());
                                    }
                                    dataList.setValue(pedidos);
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                    );
        }

    }

    public MutableLiveData<List<Venta>> getDataList() {
        return dataList;
    }
}