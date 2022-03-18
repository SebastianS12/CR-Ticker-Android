package com.raffelberg.cr_ticker.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class MatchViewModelFactory implements ViewModelProvider.Factory {

    Application application;

    public MatchViewModelFactory(Application application){
        this.application = application;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(Application.class).newInstance(application);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
