package com.raffelberg.cr_ticker.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MatchViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public MatchViewModelFactory(Application application){this.application = application;}

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new MatchViewModel(application);
    }
}
