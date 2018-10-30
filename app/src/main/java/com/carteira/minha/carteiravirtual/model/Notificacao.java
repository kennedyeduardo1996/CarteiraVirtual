package com.carteira.minha.carteiravirtual.model;

import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Notificacao {

    private boolean check;
    private double valor;


    public Notificacao(){}

    public void salvar(){


//        recuperando do banco o email
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
//        tranformando o email em id(base 64)
        String idUsuario = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );


        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("notificacao")
                .child( idUsuario )
                .setValue(this);
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }




}
