package com.carteira.minha.carteiravirtual.model;

import com.carteira.minha.carteiravirtual.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;


public class Usuario {
    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Double rendaTotal = 0.00;
    private Double despesaTotal = 0.00;

    public Usuario() {
    }

    public String getIdUsuario() { return idUsuario; }

    @Exclude
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Double getRendaTotal() { return rendaTotal; }

    public void setRendaTotal(Double receitaTotal) { this.rendaTotal = receitaTotal; }

    public Double getDespesaTotal() { return despesaTotal; }

    public void setDespesaTotal(Double despesaTotal) { this.despesaTotal = despesaTotal; }

    //metodo salvar
    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child( this.idUsuario )
                .setValue( this );
        firebase.keepSynced(true);

    }
}
