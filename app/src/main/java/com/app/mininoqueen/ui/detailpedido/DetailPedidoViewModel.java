package com.app.mininoqueen.ui.detailpedido;

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

public class DetailPedidoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<Pedido>> listMutableLiveData;

    public DetailPedidoViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is pedido fragment");
        listMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Pedido>> getListMutableLiveData() {
        return listMutableLiveData;
    }


}