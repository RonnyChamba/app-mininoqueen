package com.app.mininoqueen.ui.cuidado;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CuidadoPersonalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CuidadoPersonalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}