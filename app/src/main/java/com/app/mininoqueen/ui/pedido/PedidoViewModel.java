package com.app.mininoqueen.ui.pedido;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PedidoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PedidoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pedido fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}