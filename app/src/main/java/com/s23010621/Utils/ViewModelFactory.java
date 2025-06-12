package com.s23010621.Utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory<T> implements ViewModelProvider.Factory {

    private final T dependency;

    public ViewModelFactory(T dependency) {
        this.dependency = dependency;
    }

    @NonNull
    @Override
    public <VM extends ViewModel> VM create(@NonNull Class<VM> modelClass) {
        try {
            return modelClass.getConstructor(dependency.getClass()).newInstance(dependency);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}