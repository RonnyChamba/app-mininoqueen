package com.app.mininoqueen.ui.card;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is card fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}