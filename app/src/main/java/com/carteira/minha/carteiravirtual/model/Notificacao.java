package com.carteira.minha.carteiravirtual.model;

import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Notificacao {

    private String checked;
    private double valor;
    private boolean key;

    public Notificacao(){}

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public void salvar(){
        //        recuperando do banco o email
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
//        tranformando o email em id(base 64)
        String idUsuario = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

//        String datadigitada = DateCustom.DataEscolhida( dataEscolhida );

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("noticacao")
                .child( idUsuario )
                .push()
                .setValue(this);
    }





}
