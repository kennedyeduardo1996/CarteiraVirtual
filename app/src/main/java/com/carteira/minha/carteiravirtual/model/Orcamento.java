package com.carteira.minha.carteiravirtual.model;

import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Orcamento {

    private String data;
    private String categoria;
    private double valor;

    public Orcamento() {    }

    public void salvar(String dataEscolhida){
        //        recuperando do banco o email
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
//        tranformando o email em id(base 64)
        String idUsuario = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        String datadigitada = DateCustom.DataEscolhida( dataEscolhida );

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("orcamento")
                .child( idUsuario )
                .child( datadigitada )
                .push()
                .setValue(this);

    }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getValor() { return valor; }

    public void setValor(double valor) { this.valor = valor; }




}
