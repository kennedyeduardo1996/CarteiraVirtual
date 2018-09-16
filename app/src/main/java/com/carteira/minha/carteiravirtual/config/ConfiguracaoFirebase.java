package com.carteira.minha.carteiravirtual.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;


public class ConfiguracaoFirebase {
    private static FirebaseAuth autentificacao;
    private static DatabaseReference firebase;



    //retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if ( firebase == null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }


    //retorna a instancia do firebase
    public static FirebaseAuth getFirebaseAutentificacao() {
        if (autentificacao == null){
            autentificacao = FirebaseAuth.getInstance();
        }

        return autentificacao;

    }
}
