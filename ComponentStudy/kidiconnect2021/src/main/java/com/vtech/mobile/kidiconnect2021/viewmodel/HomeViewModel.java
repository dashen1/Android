package com.vtech.mobile.kidiconnect2021.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    public MutableLiveData<Boolean> getIsMaskVisible() {
        return isMaskVisible;
    }

    private MutableLiveData<Boolean> isMaskVisible = new MutableLiveData<>();

    public HomeViewModel() {
    }

    public void setMaskVisible(Boolean isVisible) {
        isMaskVisible.setValue(isVisible);
    }


}
