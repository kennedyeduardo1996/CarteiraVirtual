package com.carteira.minha.carteiravirtual.model;

import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.carteira.minha.carteiravirtual.helper.Base64Custom;
import com.carteira.minha.carteiravirtual.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;
    private String key;

    public Movimentacao() {
    }

    public void salvar(String dataEscolhida){

//        recuperando do banco o email
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
//        tranformando o email em id(base 64)
        String idUsuario = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

//        pega apenas o mes e ano da data
        String mesAno = DateCustom.mesAnoDataEscolhida( dataEscolhida );

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("movimentacao")
                .child( idUsuario )
                .child( mesAno )
                .push()
                .setValue(this);
        firebase.keepSynced(true);


    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}

