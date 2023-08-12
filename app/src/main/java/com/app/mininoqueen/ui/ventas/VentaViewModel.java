package com.app.mininoqueen.ui.ventas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.mininoqueen.modelos.Product;

import java.util.List;

public class VentaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final MutableLiveData<List<Product>> data = new MutableLiveData<>();

    public VentaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is venta fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Product>> getData() {
        return data;
    }
}