package com.example.componentstudy.viewmodule;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModuleFactory implements ViewModelProvider.Factory {

    public ViewModuleFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ScoreViewModel();
    }
}
