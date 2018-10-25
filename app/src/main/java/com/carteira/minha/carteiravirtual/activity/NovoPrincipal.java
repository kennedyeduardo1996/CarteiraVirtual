package com.carteira.minha.carteiravirtual.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.carteira.minha.carteiravirtual.R;

public class NovoPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_principal);
    }

    public void adicionarMovimentacao(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void abrirResumo(View view){
        startActivity(new Intent(this, PrincipalActivity.class));
    }
}
