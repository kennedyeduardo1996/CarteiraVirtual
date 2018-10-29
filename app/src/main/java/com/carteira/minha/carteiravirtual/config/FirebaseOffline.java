package com.carteira.minha.carteiravirtual.config;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseOffline extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
